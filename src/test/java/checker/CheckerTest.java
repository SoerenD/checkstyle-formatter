package checker;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.checks.whitespace.FileTabCharacterCheck;

import formatter.DebugAuditAdapter;

public class CheckerTest {

    @Test
    public void check() {
        DebugAuditAdapter auditAdapter = new DebugAuditAdapter();
        Checker checker = new Checker();
        checker.addFileSetCheck(new FileTabCharacterCheck());
        checker.addListener(auditAdapter);
    }
}
