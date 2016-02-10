package ch.yvu.callgraph;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class MethodInvocation {

    private static MethodInvocation UNKNOWN = new MethodInvocation(null, "unknown", "unknown", new Object[0], Object.class);

    private final Object object;
    private final String className;
    private final String methodName;
    private final Object[] arguments;
    private final Class resultType;

    public MethodInvocation(Object object, String className, String methodName, Object[] arguments, Class resultType) {
        this.object = object;
        this.className = className;
        this.methodName = methodName;
        this.arguments = arguments;
        this.resultType = resultType;
    }

    public static MethodInvocation unknown() {
        return UNKNOWN;
    }

    public String getObjectOrClassName() {
        return object == null ? className : object.getClass().getName() + "(" + String.valueOf(object.hashCode()) + ")";
    }

    public String getFullMethodName() {
        return getObjectOrClassName() + "." + getMethodWithArguments();
    }

    public String getMethodWithArguments() {
        List<String> arguments = stream(this.arguments).map(MethodInvocation::argumentToString).collect(toList());
        return methodName + "(" + String.join(", ", arguments) + ")";
    }

    public String getResultType() {
        return this.resultType.getName();
    }

    @Override
    public String toString() {
        return getFullMethodName()  + ":" + getResultType();
    }

    private static String argumentToString(Object argument) {
        if(argument == null) return "null";
        return argument.getClass().getName() + "(" + argument.toString() + ")";
    }
}
