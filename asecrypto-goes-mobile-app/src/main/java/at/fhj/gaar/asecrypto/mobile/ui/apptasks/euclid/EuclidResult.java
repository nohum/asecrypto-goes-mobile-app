package at.fhj.gaar.asecrypto.mobile.ui.apptasks.euclid;

import java.math.BigInteger;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

/**
 * Represents a Euclidean algorithm result (of the AsyncTasks).
 */
public class EuclidResult {

    private long milliseconds;

    private AseInteger gcd;

    public EuclidResult(long milliseconds, AseInteger gcd) {
        this.milliseconds = milliseconds;
        this.gcd = gcd;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public AseInteger getGcd() {
        return gcd;
    }
}
