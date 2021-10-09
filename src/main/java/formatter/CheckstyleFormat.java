package formatter;


import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultLogger;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.checks.header.HeaderCheck;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "touch", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class CheckstyleFormat extends AbstractMojo {

  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject mavenProject;

  @Parameter(defaultValue = "${project.basedir}", readonly = true)
  private File projectDirectory;

  public void execute() throws MojoExecutionException {
    getLog().info("Hello, world.");

    getLog().info(projectDirectory.getAbsolutePath());

    Set<File> allFilesToBeChecked = new HashSet<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(projectDirectory.getAbsolutePath() + "/src/main/java"))) {
      for (Path path : stream) {
        if (!Files.isDirectory(path)) {
          String absolutePath = path.toAbsolutePath().toString();
          getLog().info(absolutePath);
          allFilesToBeChecked.add(new File(absolutePath));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    DebugAuditAdapter auditAdapter = new DebugAuditAdapter();
    Checker checker = new Checker();
    checker.configure();
    checker.addListener(auditAdapter);

    try {
      int process = checker.process(new ArrayList<>(allFilesToBeChecked));
      for (AuditEvent addedError : auditAdapter.getAddedErrors()) {
        getLog().info("asd " + addedError.getFileName());
      }
    } catch (CheckstyleException e) {
      e.printStackTrace();
    }


    getLog().info("Hello, world. Done");
  }
}
