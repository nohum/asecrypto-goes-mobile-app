package at.fhj.gaar.asecrypto.mobile.ui.apptasks.millerrabin;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

/**
 * Saves the arguments for a new Miller-Rabin task.
 */
public class MillerRabinArguments {

    private AseInteger numberToTest;

    private long numberOfTestRuns;

    public MillerRabinArguments(AseInteger numberToTest, long numberOfTestRuns) {
        this.numberToTest = numberToTest;
        this.numberOfTestRuns = numberOfTestRuns;
    }

    public AseInteger getNumberToTest() {
        return numberToTest;
    }

    public long getNumberOfTestRuns() {
        return numberOfTestRuns;
    }
}
