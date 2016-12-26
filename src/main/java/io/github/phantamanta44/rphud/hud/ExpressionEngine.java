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

    public ExpressionEngine() {
        super(getParameters());
    }

    public double evaluate(String expr, ExprContext ctx) {
        StaticVariableSet<Double> vars = new StaticVariableSet<>();
        ctx.vars.forEach((n, s) -> vars.set(n, s.getAsDouble()));
        defined.forEach(v -> vars.set(v.getLeft(), evaluate(v.getRight(), vars)));
        return evaluate(expr, vars);
    }

    public int evaluateInt(String expr, ExprContext ctx) {
        return (int)Math.floor(evaluate(expr, ctx));
    }

    public long evaluateLong(String expr, ExprContext ctx) {
        return (long)Math.floor(evaluate(expr, ctx));
    }

    public float evaluateFloat(String expr, ExprContext ctx) {
        return (float)evaluate(expr, ctx);
    }

    public String evaluateStr(String expr, ExprContext ctx) {
        return Long.toString(evaluateLong(expr, ctx));
    }

    public String formatStr(String text, ExprContext ctx) {
        Matcher m = FMT_REGEX.matcher(text);
        StringBuffer buf = new StringBuffer();
        while (m.find())
            m.appendReplacement(buf, evaluateStr(m.group(1), ctx));
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