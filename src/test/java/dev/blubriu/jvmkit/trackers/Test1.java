package dev.blubriu.jvmkit.trackers;

import dev.blubriu.jvmkit.helpers.HTTPConnectionHelper;
import dev.blubriu.jvmkit.trackers.reports.FixedStreamTransferReport;
import dev.blubriu.jvmkit.utils.FileUtil;
import dev.blubriu.jvmkit.utils.MathUtil;
import dev.blubriu.jvmkit.utils.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class Test1 {
    @Test
    public void BufferedStreamReadTracker(){
        File file = new File("./src/test/resources/rsp.zip");
        HTTPConnectionHelper conn = new HTTPConnectionHelper("https://tichcucquaytayvanmayseden.000webhostapp.com/abm-1.15.zip").connect();
        BufferedStreamReadTracker tracker = new BufferedStreamReadTracker(4096, conn.getInput());
        FixedStreamTransferReport report = new FixedStreamTransferReport(conn.getContentLength());
        tracker.setBufferCallback(bytes -> {
            if(RandomUtil.randomInt(0, 30) == 0) {
                System.out.println("=============================================================================");
                System.out.println("Current size: " + report.getTransferredMBytes() + " MB/" + report.getTotalMBytes() + " MB (Remaining: " + report.getRemainingMBytes() + " MB)");
                System.out.println("Speed: " + MathUtil.round(report.getBytesPerSecs(), 3) + "b/s (" + MathUtil.round(report.getMBytesPerSecs(), 3) + " MB/s)");
                System.out.println("Elapsed time: " + MathUtil.round(report.getElapsedSecs(), 3) + "s");
                System.out.println("Remaining time: " + MathUtil.round(report.getRemainingSecs(), 3) + "s");
                System.out.println("Progress: " + MathUtil.round(report.getProgress(), 3) + "%");
                System.out.println("=============================================================================");
                System.out.println();
            }
            FileUtil.append(file, bytes);
        });
        tracker.start(report, () -> Assert.assertTrue(report.validate()));
        //file.delete();
    }
}
