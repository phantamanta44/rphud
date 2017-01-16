package io.github.phantamanta44.rphud.hud;

import com.fathzer.soft.javaluator.*;
import io.github.phantamanta44.rphud.util.inventory.Inventories;
import io.github.phantamanta44.rphud.util.inventory.ItemSig;
import io.github.phantamanta44.rphud.util.function.Lambdas;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.registry.GameData;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.ToDoubleBiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpressionEngine extends DoubleEvaluator {

    private static final long FULL_ALPHA = 255L << 24;
    private static final Pattern FMT_REGEX = Pattern.compile("\\$\\{(.+?)}");
    private static final Map<String, Pair<Function, ToDoubleBiFunction<Iterator<Double>, ExpressionEngine>>> funcs;
    private static final Map<String, Pair<Operator, ToDoubleBiFunction<Iterator<Double>, ExpressionEngine>>> ops;

    protected final List<Pair<String, String>> defined = new LinkedList<>();
    protected final Map<Long, Double> register = new HashMap<>();

    private StaticVariableSet<Double> vars = null;
    private ExprContext gctx = null;

    public ExpressionEngine() {
        super(getParameters());
    }

    public void context(ExprContext ctx) {
        gctx = ctx;
        vars = new StaticVariableSet<>();
        ctx.vars.forEach((n, s) -> vars.set(n, s.getAsDouble()));
        defined.forEach(v -> vars.set(v.getLeft(), evaluate(v.getRight(), vars)));
    }

    public void exitContext() {
        vars = null;
        gctx = null;
    }

    public double eval(String expr) {
        if (vars == null)
            throw new IllegalStateException("No context available!");
        return evaluate(expr, vars);
    }

    public int evalInt(String expr) {
        return Long.valueOf(evalLong(expr)).intValue();
    }

    public long evalLong(String expr) {
        return Double.valueOf(Math.floor(eval(expr))).longValue();
    }

    public float evalFloat(String expr) {
        return Double.valueOf(eval(expr)).floatValue();
    }

    public String evalStr(String expr) {
        return Long.toString(evalLong(expr));
    }

    public String formatStr(String text) {
        Matcher m = FMT_REGEX.matcher(text);
        StringBuffer buf = new StringBuffer();
        while (m.find())
            m.appendReplacement(buf, evalStr(m.group(1)));
        m.appendTail(buf);
        return buf.toString();
    }
    
    static {
        funcs = Stream.<Pair<Function, ToDoubleBiFunction<Iterator<Double>, ExpressionEngine>>>of(
                Pair.of(new Function("hsb", 3), (a, c) ->
                        Color.HSBtoRGB(f(a), f(a), f(a))
                ),
                Pair.of(new Function("rgb", 3), (a, c) ->
                        (l(a) << 16) | (l(a) << 8) | l(a) | FULL_ALPHA
                ),
                Pair.of(new Function("rgba", 4), (a, c) ->
                        (l(a) << 16) | (l(a) << 8) | l(a) | (l(a) << 24)
                ),
                Pair.of(new Function("lt", 2), (a, c) ->
                        (d(a) < d(a)) ? 1D : 0D
                ),
                Pair.of(new Function("lte", 2), (a, c) ->
                        (d(a) <= d(a)) ? 1D : 0D
                ),
                Pair.of(new Function("gt", 2), (a, c) ->
                        (d(a) > d(a)) ? 1D : 0D
                ),
                Pair.of(new Function("gte", 2), (a, c) ->
                        (d(a) >= d(a)) ? 1D : 0D
                ),
                Pair.of(new Function("eq", 2), (a, c) ->
                        (d(a) == d(a)) ? 1D : 0D
                ),
                Pair.of(new Function("ne", 2), (a, c) ->
                        (d(a) != d(a)) ? 1D : 0D
                ),
                Pair.of(new Function("xor", 2), (a, c) ->
                        l(a) ^ l(a)
                ),
                Pair.of(new Function("deg2rad", 1), (a, c) ->
                        Math.PI * d(a) / 180
                ),
                Pair.of(new Function("rad2deg", 1), (a, c) ->
                        180 * d(a) / Math.PI
                ),
                Pair.of(new Function("itemcount", 1, 2), (a, c) -> {
                    ItemSig sig = new ItemSig(i(a), a.hasNext() ? i(a) : -1);
                    return Inventories.stream(c.gctx.pl.inventory)
                            .filter(sig::matches)
                            .mapToInt(is -> is.stackSize)
                            .sum();
                }),
                Pair.of(new Function("potactive", 1), (a, c) ->
                        c.gctx.pl.isPotionActive(i(a)) ? 1D : 0D
                ),
                Pair.of(new Function("pottime", 1), (a, c) -> {
                    PotionEffect pot = c.gctx.pl.getActivePotionEffect(GameData.getPotionRegistry().getObjectById(i(a)));
                    return pot != null ? pot.getDuration() : -1;
                }),
                Pair.of(new Function("potlevel", 1), (a, c) -> {
                    PotionEffect pot = c.gctx.pl.getActivePotionEffect(GameData.getPotionRegistry().getObjectById(i(a)));
                    return pot != null ? pot.getAmplifier() : -1;
                }),
                Pair.of(new Function("put", 2), (a, c) -> {
                    Double val = c.register.put(l(a), d(a));
                    return val != null ? val : 0;
                }),
                Pair.of(new Function("has", 1), (a, c) ->
                        c.register.containsKey(l(a)) ? 1 : 0
                ),
                Pair.of(new Function("get", 1), (a, c) -> {
                    Double val = c.register.get(l(a));
                    return val != null ? val : 0;
                })
        ).collect(Collectors.toMap(f -> f.getLeft().getName(), Lambdas.identity()));
        ops = Stream.<Pair<Operator, ToDoubleBiFunction<Iterator<Double>, ExpressionEngine>>>of(
                Pair.of(new Operator("|", 2, Operator.Associativity.LEFT, 3), (a, c) ->
                        l(a) | l(a)
                ),
                Pair.of(new Operator("&", 2, Operator.Associativity.LEFT, 3), (a, c) ->
                        l(a) & l(a)
                ),
                Pair.of(new Operator("~", 1, Operator.Associativity.RIGHT, 4), (a, c) ->
                        ~l(a)
                ),
                Pair.of(new Operator("<<", 2, Operator.Associativity.LEFT, 3), (a, c) ->
                        l(a) << l(a)
                ),
                Pair.of(new Operator(">>", 2, Operator.Associativity.LEFT, 3), (a, c) ->
                        l(a) >> l(a)
                )
        ).collect(Collectors.toMap(f -> f.getLeft().getSymbol(), Lambdas.identity()));
    }

    @Override
    protected Double evaluate(Function function, Iterator<Double> args, Object ctx) {
        Pair<Function, ToDoubleBiFunction<Iterator<Double>, ExpressionEngine>> func = funcs.get(function.getName());
        return func != null ? func.getRight().applyAsDouble(args, this) : super.evaluate(function, args, ctx);
    }

    @Override
    protected Double evaluate(Operator operator, Iterator<Double> args, Object ctx) {
        Pair<Operator, ToDoubleBiFunction<Iterator<Double>, ExpressionEngine>> op = ops.get(operator.getSymbol());
        return op != null ? op.getRight().applyAsDouble(args, this) : super.evaluate(operator, args, ctx);
    }

    private static double d(Iterator<Double> d) {
        return d.next();
    }
    
    private static float f(Iterator<Double> d) {
        return d.next().floatValue();
    }
    
    private static long l(Iterator<Double> d) {
        return (long)Math.floor(d.next());
    }

    private static int i(Iterator<Double> d) {
        return Long.valueOf(l(d)).intValue();
    }

    private static Parameters getParameters() {
        Parameters params = DoubleEvaluator.getDefaultParameters();
        funcs.values().stream().map(Pair::getLeft).forEach(params::add);
        ops.values().stream().map(Pair::getLeft).forEach(params::add);
        return params;
    }

}