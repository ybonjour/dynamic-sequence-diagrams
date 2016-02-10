package ch.yvu.callgraph.output;

import ch.yvu.callgraph.MethodInvocation;

public interface CallGraphOutput {
    void call(MethodInvocation callingMethod, MethodInvocation receivingMethod);

    void resultCall(MethodInvocation returningMethod, MethodInvocation receivingMethod);
}
