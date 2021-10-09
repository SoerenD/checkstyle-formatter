package checker;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.PackageObjectFactory;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.checks.header.HeaderCheck;
import com.puppycrawl.tools.checkstyle.checks.whitespace.FileTabCharacterCheck;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import formatter.DebugAuditAdapter;

public class CheckerTest {

    @Test
    public void check() throws CheckstyleException {
        DebugAuditAdapter auditAdapter = new DebugAuditAdapter();
        Checker checker = new Checker();
        PackageObjectFactory a = new PackageObjectFactory(Collections.singleton("a"), Thread.currentThread().getContextClassLoader());
        checker.setModuleFactory(a);
        checker.configure(new DefaultConfiguration("asd"));
        checker.addFileSetCheck(new FileTabCharacterCheck());
        checker.addListener(auditAdapter);

        File file = new File("src/test/java/checker/Test.java");

        checker.process(Collections.singletonList(file));
        System.out.println(auditAdapter.getAddedErrors().get(0).getMessage());
    }
}
