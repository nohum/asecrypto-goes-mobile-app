package at.fhj.gaar.asecrypto.mobile.crypto;


public class RSAHelper {

    public static AseInteger calculateN(AseInteger p, AseInteger q) {
        return p.multiply(q);
    }

    public static AseInteger calculatePhiOfN(AseInteger p, AseInteger q) {
        return p.subtract(AseInteger.ONE).multiply(q.subtract(AseInteger.ONE));
    }

}
