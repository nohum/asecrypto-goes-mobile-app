package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import android.os.AsyncTask;
import android.util.Log;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.crypto.RSAHelper;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;

/**
 * Calculates n and phi(n) given to primes p and q.
 */
public class ParameterCalculationHelperTask extends AsyncTask<AseInteger, Void, ParameterCalculationHelperTask.CalculationResult> {

    private TaskFinishedCallable<CalculationResult> callable;

    public ParameterCalculationHelperTask(TaskFinishedCallable<CalculationResult> callable) {
        this.callable = callable;
    }

    @Override
    protected CalculationResult doInBackground(AseInteger... primes) {
        if (primes.length != 3) {
            throw new IllegalStateException("exactly three AseIntegers are expected (primes" +
                    " p and q and cipher e)");
        }

        AseInteger p = primes[0];
        if (p == null) {
            throw new IllegalStateException("AseInteger p is null");
        }

        AseInteger q = primes[1];
        if (q == null) {
            throw new IllegalStateException("AseInteger q is null");
        }

        AseInteger e = primes[2]; // null is ok

        // check primalities
        boolean pIsPrime = p.isProbablePrime(50);
        boolean qIsPrime = q.isProbablePrime(50);

        AseInteger phiOfN = RSAHelper.calculatePhiOfN(p, q);

        boolean gcdIsOne = true;
        boolean eTooBig = false;
        AseInteger decryptionNumber = null;


        if (e != null && pIsPrime && qIsPrime) {
            eTooBig = e.compareTo(phiOfN) > -1;

            AseInteger[] bezoutResult = e.getBezout(phiOfN);
            Log.d("AseCrypto", "ParameterCalculationHelperTask.doInBackground: bezout result = " +
                    bezoutResult);

            gcdIsOne = bezoutResult[2].compareTo(AseInteger.ONE) == 0;

            decryptionNumber = bezoutResult[0];
        }

        return new CalculationResult(RSAHelper.calculateN(p, q), phiOfN,
                pIsPrime, qIsPrime, decryptionNumber, gcdIsOne, eTooBig);
    }


    @Override
    protected void onPostExecute(CalculationResult calculationResult) {
        super.onPostExecute(calculationResult);

        callable.onAsyncTaskFinished(this, calculationResult);
    }

    public class CalculationResult {

        private AseInteger n;

        private AseInteger phiOfN;

        private boolean pPrime;

        private boolean qPrime;

        private AseInteger decryptionNumber;

        /**
         * Carries an error state.
         */
        private boolean gcdOfEAndPhiOfNEqualsOne;

        private boolean eTooBig;

        public CalculationResult(AseInteger n, AseInteger phiOfN, boolean pPrime, boolean qPrime,
                                 AseInteger decryptionNumber, boolean gcdOfEAndPhiOfNEqualsOne,
                                 boolean eTooBig) {
            this.n = n;
            this.phiOfN = phiOfN;
            this.pPrime = pPrime;
            this.qPrime = qPrime;
            this.decryptionNumber = decryptionNumber;
            this.gcdOfEAndPhiOfNEqualsOne = gcdOfEAndPhiOfNEqualsOne;
            this.eTooBig = eTooBig;
        }

        public AseInteger getN() {
            return n;
        }

        public AseInteger getPhiOfN() {
            return phiOfN;
        }

        public boolean isPPrime() {
            return pPrime;
        }

        public boolean isQPrime() {
            return qPrime;
        }

        public AseInteger getDecryptionNumber() {
            return decryptionNumber;
        }

        public boolean isGcdOfEAndPhiOfNEqualsOne() {
            return gcdOfEAndPhiOfNEqualsOne;
        }

        public boolean isETooBig() {
            return eTooBig;
        }
    }
}
