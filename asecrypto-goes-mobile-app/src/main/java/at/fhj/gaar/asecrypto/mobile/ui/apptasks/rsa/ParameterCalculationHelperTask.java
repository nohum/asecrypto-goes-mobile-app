package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import android.os.AsyncTask;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
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
        if (primes.length != 2) {
            throw new IllegalStateException("exactly two AseIntegers are expected (primes p and q)");
        }

        AseInteger p = primes[0];
        if (p == null) {
            throw new IllegalStateException("AseInteger p is null");
        }

        AseInteger q = primes[1];
        if (q == null) {
            throw new IllegalStateException("AseInteger q is null");
        }

        return new CalculationResult(calculateN(p, q), calculatePhiOfN(p, q));
    }

    private AseInteger calculateN(AseInteger p, AseInteger q) {
        return p.multiply(q);
    }

    private AseInteger calculatePhiOfN(AseInteger p, AseInteger q) {
        return p.subtract(AseInteger.ONE).multiply(q.subtract(AseInteger.ONE));
    }

    @Override
    protected void onPostExecute(CalculationResult calculationResult) {
        super.onPostExecute(calculationResult);

        callable.onAsyncTaskFinished(this, calculationResult);
    }

    public class CalculationResult {

        private AseInteger n;

        private AseInteger phiOfN;

        public CalculationResult(AseInteger n, AseInteger phiOfN) {
            this.n = n;
            this.phiOfN = phiOfN;
        }

        public AseInteger getN() {
            return n;
        }

        public AseInteger getPhiOfN() {
            return phiOfN;
        }
    }
}
