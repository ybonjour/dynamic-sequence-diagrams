package ch.yvu.callgraph.output;

import ch.yvu.callgraph.MethodInvocation;

public class CallGraphPrinter implements CallGraphOutput {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";

    @Override
    public void call(MethodInvocation callingMethod, MethodInvocation receivingMethod) {
        colorPrint(
                ANSI_PURPLE,
                callingMethod.getFullMethodName() + " --[" + receivingMethod.getMethodWithArguments() + "]--> " + receivingMethod.getObjectOrClassName());
    }

    @Override
    public void resultCall(MethodInvocation returningMethod, MethodInvocation receivingMethod) {
        colorPrint(
                ANSI_CYAN,
                receivingMethod.getFullMethodName() + " <--[" + returningMethod.getResultType() + "]-- " + returningMethod.getFullMethodName());
    }

    private static void colorPrint(String color, String message) {
        System.out.println(color + message + ANSI_RESET);
    }
}
