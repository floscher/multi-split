## MultiSplit

This is a copy of MultiSplitPane and MultiSplitLayout as found [in a Java.net blog post by Hans Muller](https://today.java.net/pub/a/today/2006/03/23/multi-split-pane.html) (you can also download the original source code [from there](https://today.java.net/pub/a/today/2006/03/23/multi-split-pane.html#resources)).
You'll also find MultiSplitPane, MultiSplitLayout in the SwingLabs project, see https://java.net/projects/swingx.

## Gradle build
The build system of the master branch has been switched over from Ant to [Gradle](https://gradle.org), so you need Gradle installed (tested with Gradle 2.2.1, but should also work with much lower versions).

As this is a Java project, you of course also need to have the Java Development Kit (JDK) installed. By default this project builds for Java 1.7, but if you want you can change that in `build.gradle`.

### Command line
In the command line you can simply use the following to build into the `build/` folder (`build/lib`):
```shell
gradle build
```

### Eclipse
If you want to open the project in Eclipse, just type the following in the command line, which generates some Eclipse project files:
```shell
gradle eclipse
```
Now you can open the project as an Eclipse project.

### IntelliJ IDEA
For opening the project in IntelliJ IDEA, you can use the following command from the command line:
```shell
gradle idea
```
Now you can open the project as an IDEA project.
