package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

public class RSAResult {

    private AseInteger result;

    private long milliseconds;

    public RSAResult(AseInteger result, long milliseconds) {
        this.result = result;
        this.milliseconds = milliseconds;
    }

    public AseInteger getResult() {
        return result;
    }

    public long getMilliseconds() {
        return milliseconds;
    }
}
