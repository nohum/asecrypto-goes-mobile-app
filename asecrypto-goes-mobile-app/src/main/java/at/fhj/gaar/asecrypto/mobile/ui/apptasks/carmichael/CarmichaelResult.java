package at.fhj.gaar.asecrypto.mobile.ui.apptasks.carmichael;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

public class CarmichaelResult {

    private long milliseconds;

    private AseInteger carmichaelNumber;

    public CarmichaelResult(long milliseconds, AseInteger carmichaelNumber) {
        this.milliseconds = milliseconds;
        this.carmichaelNumber = carmichaelNumber;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public AseInteger getCarmichaelNumber() {
        return carmichaelNumber;
    }
}
