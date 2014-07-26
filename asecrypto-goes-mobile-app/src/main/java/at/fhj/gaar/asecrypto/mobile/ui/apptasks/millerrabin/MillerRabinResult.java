package at.fhj.gaar.asecrypto.mobile.ui.apptasks.millerrabin;

public class MillerRabinResult {

    private MillerRabinTask.TestResult testResult;

    private long milliseconds;

    public MillerRabinResult(MillerRabinTask.TestResult testResult, long milliseconds) {
        this.testResult = testResult;
        this.milliseconds = milliseconds;
    }

    public MillerRabinTask.TestResult getTestResult() {
        return testResult;
    }

    public long getMilliseconds() {
        return milliseconds;
    }
}
