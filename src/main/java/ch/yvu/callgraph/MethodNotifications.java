package ch.yvu.callgraph;

import ch.yvu.callgraph.output.CallGraphOutput;
import ch.yvu.callgraph.output.SequenceDiagramOutput;

import java.io.IOException;
import java.util.Stack;

public class MethodNotifications {

    private final CallGraphOutput graphOutput;
    private Stack<MethodInvocation> calledMethods = new Stack<>();

    private static MethodNotifications instance;

    static {
        try {
            instance = new MethodNotifications(new SequenceDiagramOutput("sequence.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MethodNotifications(CallGraphOutput graphOutput) {
        this.graphOutput = graphOutput;
    }

    public static MethodNotifications getInstance() {
        return instance;
    }

    public void methodStarted(String className, String methodName, Object[] arguments, Class resultType, Object object) {
        MethodInvocation method = new MethodInvocation(object, className, methodName, arguments, resultType);

        graphOutput.call(getCurrentMethod(), method);

        calledMethods.push(method);
    }

    public void methodFinished() {
        MethodInvocation method = calledMethods.pop();
        graphOutput.resultCall(method, getCurrentMethod());
    }

    private MethodInvocation getCurrentMethod() {
        if (calledMethods.isEmpty()) return MethodInvocation.unknown();

        return calledMethods.peek();
    }
}
