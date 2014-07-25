package at.fhj.gaar.asecrypto.mobile.ui.apptasks.fermat;

/**
 * Used to report intermediate progress.
 */
public class FermatProgress {

    private long currentMilliseconds;

    private long currentTestCount;

    public FermatProgress(long currentMilliseconds, long currentTestCount) {
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
