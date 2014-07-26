package at.fhj.gaar.asecrypto.mobile.ui.apptasks.carmichael;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

public class CarmichaelResult {

    private long milliseconds;

    private AseInteger carmichaelNumber;

    private AseInteger failNumbersCount;

    private AseInteger successNumbersCount;

    private AseInteger failChance;

    public CarmichaelResult(long milliseconds, AseInteger carmichaelNumber,
                            AseInteger failNumbersCount, AseInteger successNumbersCount,
                            AseInteger failChance) {
        this.milliseconds = milliseconds;
        this.carmichaelNumber = carmichaelNumber;
        this.failNumbersCount = failNumbersCount;
        this.successNumbersCount = successNumbersCount;
        this.failChance = failChance;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public AseInteger getCarmichaelNumber() {
        return carmichaelNumber;
    }

    public AseInteger getFailNumbersCount() {
        return failNumbersCount;
    }

    public AseInteger getSuccessNumbersCount() {
        return successNumbersCount;
    }

    public AseInteger getFailChance() {
        return failChance;
    }
}
