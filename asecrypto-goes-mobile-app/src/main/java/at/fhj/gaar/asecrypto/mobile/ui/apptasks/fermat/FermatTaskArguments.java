package at.fhj.gaar.asecrypto.mobile.ui.apptasks.fermat;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

/**
 * Saves the arguments for a new Fermat task.
 */
public class FermatTaskArguments {

    private AseInteger numberToTest;

    private int numberOfTestRuns;

    public FermatTaskArguments(AseInteger numberToTest, int numberOfTestRuns) {
        this.numberToTest = numberToTest;
        this.numberOfTestRuns = numberOfTestRuns;
    }

    public AseInteger getNumberToTest() {
        return numberToTest;
    }

    public int getNumberOfTestRuns() {
        return numberOfTestRuns;
    }
}
