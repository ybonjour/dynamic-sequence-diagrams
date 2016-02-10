package ch.yvu.callgraph.output;

import ch.yvu.callgraph.MethodInvocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.util.Arrays.asList;

public class SequenceDiagramOutput implements CallGraphOutput {

    private final Path outputFilePath;
    private final CallGraphPrinter printer = new CallGraphPrinter();

    public SequenceDiagramOutput(String outputFilePath) throws IOException {
        this.outputFilePath = Paths.get(outputFilePath);
        Files.deleteIfExists(this.outputFilePath);
        Files.createFile(this.outputFilePath);
    }

    @Override
    public void call(MethodInvocation from, MethodInvocation to) {
        printer.call(from, to);
        String output = from.getObjectOrClassName() + "->+" + to.getObjectOrClassName() + ": " + to.getMethodWithArguments();
        writeToFile(output);
    }

    @Override
    public void resultCall(MethodInvocation from, MethodInvocation to) {
        printer.call(from, to);
        String output = from.getObjectOrClassName() + "-->-" + to.getObjectOrClassName() + ": " + from.getResultType();
        writeToFile(output);
    }

    private synchronized void writeToFile(String output) {
        try {
            Files.write(outputFilePath, asList(output), APPEND);
        } catch (IOException e) {
            System.err.println("Could not write to file " + outputFilePath);
            e.printStackTrace();
        }
    }
}
