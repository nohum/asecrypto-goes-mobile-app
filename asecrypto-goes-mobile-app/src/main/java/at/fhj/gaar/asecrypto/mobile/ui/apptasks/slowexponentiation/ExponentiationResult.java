package at.fhj.gaar.asecrypto.mobile.ui.apptasks.slowexponentiation;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

/**
 * Slow exponentiation result
 */
public class ExponentiationResult {

    private long watchTime;

    private AseInteger exponentiationResult;

    public ExponentiationResult(long watchTime, AseInteger exponentiationResult) {
        this.watchTime = watchTime;
        this.exponentiationResult = exponentiationResult;
    }

    public long getWatchTime() {
        return watchTime;
    }

    public AseInteger getExponentiationResult() {
        return exponentiationResult;
    }
}
