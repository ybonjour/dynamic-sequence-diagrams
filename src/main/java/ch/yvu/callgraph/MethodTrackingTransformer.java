package ch.yvu.callgraph;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Optional;

import static java.util.Arrays.stream;

public class MethodTrackingTransformer implements ClassFileTransformer {

    private final String packagePrefix;

    public MethodTrackingTransformer(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    public static void premain(String findArguments, Instrumentation instrumentation) {
        Optional<String> packagePrefix = findArgument(findArguments, "package");
        if (!packagePrefix.isPresent()) {
            System.err.println("no package provided");
            return;
        }

        instrumentation.addTransformer(new MethodTrackingTransformer(packagePrefix.get()));
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class clazz, ProtectionDomain domain, byte[] originalBytes) throws IllegalClassFormatException {
        if (!className.startsWith(packagePrefix)) {
            return originalBytes;
        }

        CtClass ctClass = readClass(originalBytes);
        if (ctClass == null) return originalBytes;

        try {
            enhanceMethods(ctClass);
            return ctClass.toBytecode();
        } catch (IOException | CannotCompileException e) {
            System.err.println("Couldn't enhance class " + className);
            e.printStackTrace();
            return originalBytes;
        }
    }

    private void enhanceMethods(CtClass ctClass) throws CannotCompileException {
        if (ctClass.isInterface()) return;

        CtBehavior[] constructorsAndMethods = ctClass.getDeclaredBehaviors();

        for (CtBehavior method : constructorsAndMethods) {
            if (method.isEmpty()) continue;

            method.insertBefore(methodStarted(method));
            method.insertAfter(methodFinished());
        }
    }

    private static boolean isStatic(CtBehavior method) {
        return (method.getModifiers() & Modifier.STATIC) > 0;
    }

    private static Optional<String> findArgument(String arguments, String name) {
        return stream(arguments.split(";")).filter(argument -> name.equals(argument.split("=")[0])).map(
                argument -> argument.split(
                        "=")[1]).findFirst();
    }

    private static String methodStarted(CtBehavior method) {
        return "ch.yvu.callgraph.MethodNotifications.getInstance().methodStarted(\"" + method.getDeclaringClass().getName() + "\", \"" + method.getName() + "\", $args, $type, " + (isStatic(
                method) ? "null" : "this") + ");";
    }

    private static String methodFinished() {
        return "ch.yvu.callgraph.MethodNotifications.getInstance().methodFinished();";
    }

    private static CtClass readClass(byte[] bytes) {
        try {
            ClassPool pool = ClassPool.getDefault();
            return pool.makeClass(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            System.err.println("Error while reading class");
            e.printStackTrace();
            return null;
        }
    }
}
