# dynamic-sequence-diagrams
Creates a sequence diagram based on a program execution

*Warning: This is just a prototype. It's purpose is to try out Javas' instrumentation capabilities and javaassist. It therefore does not have any automated tests and is not supposed to be stable.*


# Build
Download the source and create the jar using gradle:

```./gradlew jar```

This will create a jar in `build/libs/CallGraph-1.0.jar`.

# Usage
When executing your java code pass the CallGraph jar as a java agent and specify the package you want to have analyzed in the package parameter:

```
java -javaagent:CallGraph-1.0.jar="package=com/your/project" -classpath path/to/your/application.jar com.your.project.MainClass```
```

This creates a `sequence.txt` file that describes the sequence diagram.
You can now paste the content of that file to `https://www.websequencediagrams.com/`.
