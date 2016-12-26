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
        return (int)Math.floor(evaluate(expr));
    }

    public long evalLong(String expr) {
        return (long)Math.floor(evaluate(expr));
    }

    public float evalFloat(String expr) {
        return (float)eval(expr);
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
                        (long)Math.floor(args.next())
                );
            case "rgba":
                return (double)(
                        ((long)Math.floor(args.next()) << 16) |
                        ((long)Math.floor(args.next()) << 8) |
                        (long)Math.floor(args.next()) |
                        ((long)Math.floor(args.next()) << 24)
                );
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
        return params;
    }

}