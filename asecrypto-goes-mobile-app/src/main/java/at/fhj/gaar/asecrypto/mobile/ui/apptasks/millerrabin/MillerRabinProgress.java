package at.fhj.gaar.asecrypto.mobile.ui.apptasks.millerrabin;

/**
 * Used to report intermediate progress.
 */
public class MillerRabinProgress {

    private long currentMilliseconds;

    private long currentTestCount;

    public MillerRabinProgress(long currentMilliseconds, long currentTestCount) {
        this.currentMilliseconds = currentMilliseconds;
        this.currentTestCount = currentTestCount;
    }

    public long getCurrentMilliseconds() {
        return currentMilliseconds;
    }

    public long getCurrentTestCount() {
        return currentTestCount;
    }
}
