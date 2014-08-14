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

    /**
     * TODO: refactoring
     *
     * @param bits
     * @param testRuns
     * @return
     */
    private AseInteger[] findPrimitiveRoot(int bits, int testRuns) {
        AseInteger finalModulus;
        AseInteger finalPrimeQ;
        int candidateFoundCounter;

        // do until you found a matching prime
        for (int i = 1; true; i++) {
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

                candidateFoundCounter = i;
                finalModulus = mod;
                finalPrimeQ = mod.subtract(AseInteger.ONE).divide(AseInteger.TWO);
                break;
            }
        }

        int trails = 0;
        while (true) {
            if (isCancelled()) {
                return null;
            }

            // create a random AseInteger as a candidate
            AseInteger r = new AseInteger(bits, new Random());

            // make sure the candidate is smaller than the modulus number
            if (r.compareTo(finalModulus) >= 0) {
                continue;
            }

            // only count trails with an suitable random candidate
            trails++;

            // check the order of the candidate
            if (r.modPow(AseInteger.TWO, finalModulus).compareTo(AseInteger.ONE) == 0) {
                continue;
            }

            if (r.modPow(finalPrimeQ, finalModulus).compareTo(AseInteger.ONE) == 0) {
                continue;
            }

            return new AseInteger[] {
                    finalModulus,
                    r
            };
        }
    }

    @Override
    protected void onPostExecute(PrimitiveRootResult primitiveRootResultData) {
        super.onPostExecute(primitiveRootResultData);

        finishedCallable.onAsyncTaskFinished(this, primitiveRootResultData);
    }
}
