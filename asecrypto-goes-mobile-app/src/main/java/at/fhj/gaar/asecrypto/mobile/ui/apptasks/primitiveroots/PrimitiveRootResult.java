package at.fhj.gaar.asecrypto.mobile.ui.apptasks.primitiveroots;


import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

public class PrimitiveRootResult {

    private long milliseconds;

    private AseInteger modulus;

    private AseInteger prime;

    public PrimitiveRootResult(long milliseconds, AseInteger modulus, AseInteger prime) {
        this.milliseconds = milliseconds;
        this.modulus = modulus;
        this.prime = prime;
    }

    public AseInteger getModulus() {
        return modulus;
    }

    public AseInteger getPrime() {
        return prime;
    }

    public long getMilliseconds() {
        return milliseconds;
    }
}
