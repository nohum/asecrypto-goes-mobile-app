package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

public class RSAEncryptionParameters {

    private AseInteger n;

    private AseInteger e;

    private AseInteger message;

    public RSAEncryptionParameters(AseInteger n, AseInteger e, AseInteger message) {
        this.n = n;
        this.e = e;
        this.message = message;
    }

    public AseInteger getN() {
        return n;
    }

    public AseInteger getE() {
        return e;
    }

    public AseInteger getMessage() {
        return message;
    }
}
