package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

public class RSADecryptionParameters {

    private AseInteger p;

    private AseInteger q;

    private AseInteger d;

    private AseInteger message;

    private boolean useChineseRemainderTheorem;

    public RSADecryptionParameters(AseInteger p, AseInteger q, AseInteger d, AseInteger message,
                                   boolean useCRT) {
        this.p = p;
        this.q = q;
        this.d = d;
        this.message = message;
        this.useChineseRemainderTheorem = useCRT;
    }

    public AseInteger getP() {
        return p;
    }

    public AseInteger getQ() {
        return q;
    }

    public AseInteger getD() {
        return d;
    }

    public AseInteger getMessage() {
        return message;
    }

    public boolean isUseChineseRemainderTheorem() {
        return useChineseRemainderTheorem;
    }
}
