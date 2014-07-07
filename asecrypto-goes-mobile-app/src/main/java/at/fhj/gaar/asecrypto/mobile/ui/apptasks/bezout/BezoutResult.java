package at.fhj.gaar.asecrypto.mobile.ui.apptasks.bezout;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

/**
 * Represents a Bezout algorithm result (of the AsyncTasks).
 */
public class BezoutResult {

    private long milliseconds;

    private AseInteger gcd;

    private AseInteger x;

    private AseInteger y;

    public BezoutResult(long milliseconds, AseInteger x, AseInteger y, AseInteger gcd) {
        this.milliseconds = milliseconds;
        this.x = x;
        this.y = y;
        this.gcd = gcd;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public AseInteger getX() {
        return x;
    }

    public AseInteger getY() {
        return y;
    }

    public AseInteger getGcd() {
        return gcd;
    }
}
