package at.fhj.gaar.asecrypto.mobile.ui.apptasks.primitiveroots;

import android.os.AsyncTask;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

/**
 * Executes the search for a primitive root.
 */
public class PrimitiveRootLookupTask extends AsyncTask<FinderArguments, Void, PrimitiveRootResult> {

    private final TaskFinishedCallable<PrimitiveRootResult> finishedCallable;

    private StopWatch watch;

    public PrimitiveRootLookupTask(TaskFinishedCallable<PrimitiveRootResult> finishedCallable) {
        this.finishedCallable = finishedCallable;

        watch = new StopWatch();
    }


    @Override
    protected PrimitiveRootResult doInBackground(FinderArguments... parameters) {
        if (parameters.length != 1) {
            throw new RuntimeException("supply one instance of FinderArguments");
        }

        FinderArguments args = parameters[0];
        if (args == null) {
            throw new NullPointerException("invald args");
        }

        watch.start();
        AseInteger result[] = findPrimitiveRoot(args.getBitCount(), args.getNumberOfTestRuns());
        watch.stop();

        if (result == null) {
            return null;
        }

        return new PrimitiveRootResult(watch.getElapsedTime(), result[0], result[1]);
    }

    private AseInteger[] findPrimitiveRoot(int bits, int testRuns) {
        AseInteger[] result = findPrimeCandidate(bits, testRuns);

        if (result == null) {
            return null;
        }

        AseInteger finalModulus = result[0];
        AseInteger finalPrimeQ = result[1];

        return calculateFinalRoot(bits, finalModulus, finalPrimeQ);
    }

    private AseInteger[] calculateFinalRoot(int bits, AseInteger modulus, AseInteger prime) {
        while (true) {
            if (isCancelled()) {
                return null;
            }

            // create a random AseInteger as a candidate
            AseInteger r = new AseInteger(bits, new Random());

            // make sure the candidate is smaller than the modulus number
            if (r.compareTo(modulus) >= 0) {
                continue;
            }

            // check the order of the candidate
            if (r.modPow(AseInteger.TWO, modulus).compareTo(AseInteger.ONE) == 0) {
                continue;
            }

            if (r.modPow(prime, modulus).compareTo(AseInteger.ONE) == 0) {
                continue;
            }

            return new AseInteger[] {
                    modulus,
                    r
            };
        }
    }

    private AseInteger[] findPrimeCandidate(int bits, int testRuns) {
        // do until you found a matching prime
        while (true) {
            if (isCancelled()) {
                return null;
            }

            // generate a random prime (using a suitable AseInteger constructor)
            AseInteger mod = new AseInteger(bits, testRuns, new Random());

            // check if this (prime-1)/2 is another prime (AseInteger provides the isProbablePrime() method for this)
            AseInteger q = mod.subtract(AseInteger.ONE).divide(AseInteger.TWO);
            if (q.isProbablePrime(testRuns)) {
                // verify the contract that modulus - 1 = 2*primeQ
                if (mod.subtract(AseInteger.ONE).divide(AseInteger.TWO).compareTo(q) != 0) {
                    throw new IllegalArgumentException("Your primes do not stick to the formula:" +
                            " \"modulus - 1 = 2*primeQ\"");
                }

                return new AseInteger[] {
                        mod,
                        mod.subtract(AseInteger.ONE).divide(AseInteger.TWO)
                };
            }
        }
    }

    @Override
    protected void onPostExecute(PrimitiveRootResult primitiveRootResultData) {
        super.onPostExecute(primitiveRootResultData);

        finishedCallable.onAsyncTaskFinished(this, primitiveRootResultData);
    }
}
