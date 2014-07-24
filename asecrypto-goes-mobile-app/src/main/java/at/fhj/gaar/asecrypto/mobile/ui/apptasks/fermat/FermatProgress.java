package at.fhj.gaar.asecrypto.mobile.ui.apptasks.fermat;

/**
 * Used to report intermediate progress.
 */
public class FermatProgress {

    private long currentMilliseconds;

    private int currentTestCount;

    public FermatProgress(long currentMilliseconds, int currentTestCount) {
        this.currentMilliseconds = currentMilliseconds;
        this.currentTestCount = currentTestCount;
    }

    public long getCurrentMilliseconds() {
        return currentMilliseconds;
    }

    public int getCurrentTestCount() {
        return currentTestCount;
    }
}
