package at.fhj.gaar.asecrypto.mobile.ui.apptasks.primitiveroots;

public class FinderArguments {

    private int bitCount;

    private int numberOfTestRuns;

    public FinderArguments(int bitCount, int numberOfTestRuns) {
        this.bitCount = bitCount;
        this.numberOfTestRuns = numberOfTestRuns;
    }

    public int getBitCount() {
        return bitCount;
    }

    public int getNumberOfTestRuns() {
        return numberOfTestRuns;
    }
}