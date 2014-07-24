package at.fhj.gaar.asecrypto.mobile.ui.apptasks.fermat;

public class FermatResult {

    private boolean testSucceeded;

    private long milliseconds;

    public FermatResult(boolean hasTestSucceeded, long milliseconds) {
        this.testSucceeded = hasTestSucceeded;
        this.milliseconds = milliseconds;
    }

    public boolean hasTestSucceeded() {
        return testSucceeded;
    }

    public long getMilliseconds() {
        return milliseconds;
    }
}
