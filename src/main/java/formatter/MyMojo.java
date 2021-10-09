package formatter;


import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import static formatter.ErrorClasses.LEFT_CURLY_CHECK;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "touch", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class MyMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject mavenProject;

  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession mavenSession;

  @Parameter(defaultValue = "${session}", readonly = true)
  private File projectDirectory;

  @Component
  private BuildPluginManager pluginManager;

  @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
  private File outputDirectory;

  public void execute() throws MojoExecutionException {
    getLog().info("Hello, world.");

    executeMojo(
        plugin(
            groupId("org.apache.maven.plugins"),
            artifactId("maven-checkstyle-plugin"),
            version("3.1.1")
        ),
        goal("checkstyle"),
        configuration(
            element(name("configLocation"), "${project.basedir}/src/main/checkstyle/configuration.xml"),
            element(name("headerLocation"), "${project.basedir}/src/main/checkstyle/java.header"),
            element(name("outputFile"), "${project.basedir}/target/formatter/errors.xml")
        ),
        executionEnvironment(
            mavenProject,
            mavenSession,
            pluginManager
        )
    );

    File errorFile = new File(mavenProject.getBasedir().getAbsolutePath() + "/target/formatter/errors.xml");
    getLog().info("Error File exists " + errorFile.exists());

    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Checkstyle.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      Checkstyle checkstyle = (Checkstyle) jaxbUnmarshaller.unmarshal(errorFile);

      for (CheckedFile checkedFile : checkstyle.getCheckedFile()) {
        for (CheckstyleError checkstyleError : checkedFile.getCheckstyleErrors()) {
          if (LEFT_CURLY_CHECK.equals(checkstyleError.getErrorClass())) {
            Path filePath = Paths.get(checkedFile.getFilePath());
            getLog().info("Unmarshalled " + filePath);
            getLog().info("Unmarshalled " + checkstyleError);

            Supplier<Stream<String>> fileSupplier = () -> {
              try {
                return Files.lines(filePath);
              } catch (IOException e) {
                e.printStackTrace();
              }

              return null;
            };

            if (fileSupplier.get() != null) {
              int lineTobeReplaced = Integer.parseInt(checkstyleError.getLine());
              int linesToSkip = lineTobeReplaced - 1;
              String lineData = fileSupplier.get().skip(linesToSkip).findFirst().get();
              getLog().info("Line: " + lineData);

              int column = Integer.parseInt(checkstyleError.getColumn());
              StringBuilder sb = new StringBuilder(lineData);
              sb.insert(column, System.lineSeparator());
              getLog().info("New line: " + sb);

              List<String> classAsString = fileSupplier.get().collect(Collectors.toList());
              getLog().info("Line(" + lineTobeReplaced + ") to be replaced: " + classAsString.get(linesToSkip));
              classAsString.set(linesToSkip, sb.toString());

              Files.write(filePath, classAsString, StandardCharsets.UTF_8);
            }
          }
        }
      }
    } catch (JAXBException | IOException e) {
      e.printStackTrace();
    }

    getLog().info("Hello, world. Done");
  }
}
