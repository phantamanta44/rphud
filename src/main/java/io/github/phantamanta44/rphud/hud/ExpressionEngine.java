package io.github.phantamanta44.rphud.hud;

import com.fathzer.soft.javaluator.*;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEngine extends DoubleEvaluator {

    private static final long FULL_ALPHA = 255L << 24;
    private static final Pattern FMT_REGEX = Pattern.compile("\\$\\{(.+?)}");

    protected final List<Pair<String, String>> defined = new LinkedList<>();

    private StaticVariableSet<Double> vars = null;

    public ExpressionEngine() {
        super(getParameters());
    }

    public void context(ExprContext ctx) {
        vars = new StaticVariableSet<>();
        ctx.vars.forEach((n, s) -> vars.set(n, s.getAsDouble()));
        defined.forEach(v -> vars.set(v.getLeft(), evaluate(v.getRight(), vars)));
    }

    public void exitContext() {
        vars = null;
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

    @Override
    protected Double evaluate(Function function, Iterator<Double> args, Object ctx) {
        switch (function.getName()) {
            case "hsb":
                return (double)Color.HSBtoRGB(
                        args.next().floatValue(),
                        args.next().floatValue(),
                        args.next().floatValue()
                );
            case "rgb":
                return (double)(
                        ((long)Math.floor(args.next()) << 16) |
                        ((long)Math.floor(args.next()) << 8) |
                        (long)Math.floor(args.next()) |
                        FULL_ALPHA
                );
            case "rgba":
                return (double)(
                        ((long)Math.floor(args.next()) << 16) |
                        ((long)Math.floor(args.next()) << 8) |
                        (long)Math.floor(args.next()) |
                        ((long)Math.floor(args.next()) << 24)
                );
            case "lt":
                return (args.next() < args.next()) ? 1D : 0D;
            case "gt":
                return (args.next() > args.next()) ? 1D : 0D;
            case "lte":
                return (args.next() <= args.next()) ? 1D : 0D;
            case "gte":
                return (args.next() >= args.next()) ? 1D : 0D;
            case "eq":
                return args.next().equals(args.next()) ? 1D : 0D;
            case "ne":
                return !args.next().equals(args.next()) ? 1D : 0D;
            default:
                return super.evaluate(function, args, ctx);
        }
    }

    @Override
    protected Double evaluate(Operator operator, Iterator<Double> args, Object ctx) {
        switch (operator.getSymbol()) {
            case "|":
                return (double)((long)Math.floor(args.next()) | (long)Math.floor(args.next()));
            case "&":
                return (double)((long)Math.floor(args.next()) & (long)Math.floor(args.next()));
            case "~":
                return (double)~((long)Math.floor(args.next()));
            case "<<":
                return (double)((long)Math.floor(args.next()) << (long)Math.floor(args.next()));
            case ">>":
                return (double)((long)Math.floor(args.next()) >> (long)Math.floor(args.next()));
            default:
                return super.evaluate(operator, args, ctx);
        }
    }

    private static Parameters getParameters() {
        Parameters params = DoubleEvaluator.getDefaultParameters();
        params.add(new Operator("|", 2, Operator.Associativity.LEFT, 3));
        params.add(new Operator("&", 2, Operator.Associativity.LEFT, 3));
        params.add(new Operator("~", 1, Operator.Associativity.RIGHT, 4));
        params.add(new Operator("<<", 2, Operator.Associativity.LEFT, 3));
        params.add(new Operator(">>", 2, Operator.Associativity.LEFT, 3));
        params.add(new Function("hsb", 3));
        params.add(new Function("rgb", 3));
        params.add(new Function("rgba", 4));
        params.add(new Function("lt", 2));
        params.add(new Function("gt", 2));
        params.add(new Function("lte", 2));
        params.add(new Function("gte", 2));
        params.add(new Function("eq", 2));
        params.add(new Function("ne", 2));
        return params;
    }

}