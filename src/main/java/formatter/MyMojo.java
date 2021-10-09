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
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "touch", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class MyMojo extends AbstractMojo {

  @Parameter( defaultValue = "${project}", readonly = true )
  private MavenProject mavenProject;

  @Parameter( defaultValue = "${session}", readonly = true )
  private MavenSession mavenSession;

  @Component
  private BuildPluginManager pluginManager;

  /**
   * Location of the file.
   */
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
            element(name("consoleOutput"), "true"),
            element(name("outputFile"), "${project.basedir}/target/formatter/errors.xml")
        ),
        executionEnvironment(
            mavenProject,
            mavenSession,
            pluginManager
        )
    );

    getLog().info("Hello, world. Done");
  }
}
