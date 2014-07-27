package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

/**
 * Saves the arguments for a new Miller-Rabin task.
 */
public class RSAAlgorithmArguments {

    private AseInteger numberToTest;

    private long numberOfTestRuns;

    public RSAAlgorithmArguments(AseInteger numberToTest, long numberOfTestRuns) {
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
