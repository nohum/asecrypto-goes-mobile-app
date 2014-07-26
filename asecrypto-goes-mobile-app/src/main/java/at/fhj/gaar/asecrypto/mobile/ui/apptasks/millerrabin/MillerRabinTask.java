package at.fhj.gaar.asecrypto.mobile.ui.apptasks.millerrabin;

import android.os.AsyncTask;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.TaskIntermediateCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

/**
 * Executes the Miller-Rabin test.
 */
public class MillerRabinTask extends AsyncTask<MillerRabinArguments, MillerRabinProgress, MillerRabinResult> {

    private static long informUserAfterNTests = 3000;

    private final TaskFinishedCallable<MillerRabinResult> finishedCallable;

    private final TaskIntermediateCallable<MillerRabinProgress> progressCallable;
    private StopWatch watch;

    public MillerRabinTask(TaskFinishedCallable<MillerRabinResult> finishedCallable,
                           TaskIntermediateCallable<MillerRabinProgress> progressCallable) {
        this.finishedCallable = finishedCallable;
        this.progressCallable = progressCallable;

        watch = new StopWatch();
    }

    /**
     * This function creates a random AseInteger between 0 and modulus You can use
     * this number as a witness in a Fermat test to check the primality of modulus
     *
     * @param modulus is an upper bound for your test number
     * @return a random AseInteger guaranteed to be >0 and <modulus
     */
    private AseInteger createRandomTestNumber(AseInteger modulus) {
        // get a random number
        AseInteger testNumber = new AseInteger(modulus.bitLength(), new Random());

        // assure that it is positive and smaller modulus
        testNumber = testNumber.mod(modulus);

        // assure that it is non zero
        if (testNumber.compareTo(AseInteger.ZERO) == 0) {
            testNumber = modulus.subtract(AseInteger.ONE);
        }

        return testNumber;
    }

    @Override
    protected MillerRabinResult doInBackground(MillerRabinArguments... numbers) {
        if (numbers.length != 1) {
            throw new RuntimeException("supply only one FermatTaskArguments");
        }

        MillerRabinArguments arguments = numbers[0];
        if (arguments == null) {
            throw new NullPointerException("invald FermatTaskArguments");
        }

        watch.start();

        TestResult testResult = run(arguments.getNumberToTest(), arguments.getNumberOfTestRuns());

        watch.stop();
        return new MillerRabinResult(testResult, watch.getElapsedTime());
    }

    /**
     * @param possiblePrime is the number to check for primality
     * @param numOfTests    is the number of times you want the test to run
     * @return gives true if possiblePrime passed all tests, else false
     */
    public TestResult run(AseInteger possiblePrime, final long numOfTests) {
        if (numOfTests <= 0) {
            return TestResult.CANCELLED;
        }

        // if possiblePrime is really prime, its phi is its value-1
        AseInteger exponent = possiblePrime.subtract(AseInteger.ONE);

        publishProgress(new MillerRabinProgress(watch.getElapsedTime(), 0));

        ////////////////////////////////////////////////////////////////////
        // start a series of numOfTests FermatTests
        // that is exponentiate a test number
        long counter;
        for (counter = 1; counter <= numOfTests; counter++) {
            // create a random AseInteger as witness
            AseInteger a = createRandomTestNumber(possiblePrime);
            exponent = possiblePrime.subtract(AseInteger.ONE);
            AseInteger cmp = a.modPow(exponent, possiblePrime);

            // check if a^(p-1) mod p <> 1
            for (int j = 1; j < numOfTests; j++) {
                cmp = a.modPow(exponent.multiply(AseInteger.valueOf(2).pow(j)), possiblePrime);

                if (cmp.compareTo(AseInteger.valueOf(-1)) == 0) {
                    return TestResult.LIKELY_PRIME;
                }

                if (cmp.compareTo(AseInteger.ONE) != 0) {
                    return TestResult.NOT_A_PRIME;
                }

                // inform the user of current state
                if (j % informUserAfterNTests == 0 && counter % informUserAfterNTests == 0) {
                    publishProgress(new MillerRabinProgress(watch.getElapsedTime(), counter));
                }

                if (isCancelled()) {
                    return TestResult.CANCELLED;
                }

                exponent = exponent.divide(AseInteger.TWO);
                if (exponent.mod(AseInteger.TWO).compareTo(AseInteger.ZERO) != 0) {
                    j = Long.valueOf(numOfTests).intValue() + 1;
                }
            }
        }

        // adjust counter to the number of tests actually done for correct output
        counter--;

        // all Fermat tests succeeded
        publishProgress(new MillerRabinProgress(watch.getElapsedTime(), counter));
        return TestResult.LIKELY_PRIME;
    }

    @Override
    protected void onProgressUpdate(MillerRabinProgress... millerRabinProgresses) {
        super.onProgressUpdate(millerRabinProgresses);

        if (millerRabinProgresses[0] == null) {
            throw new IllegalStateException("first element must be non-null");
        }

        if (millerRabinProgresses.length != 1) {
            throw new IllegalStateException("supply exactly one progress element");
        }

        this.progressCallable.onAsyncTaskUpdate(this, millerRabinProgresses[0]);
    }

    @Override
    protected void onPostExecute(MillerRabinResult millerRabinResult) {
        super.onPostExecute(millerRabinResult);
        this.finishedCallable.onAsyncTaskFinished(this, millerRabinResult);
    }

    @Override
    protected void onCancelled(MillerRabinResult millerRabinResult) {
        super.onCancelled(millerRabinResult);
        this.finishedCallable.onAsyncTaskFinished(this,
                new MillerRabinResult(TestResult.CANCELLED, 0));
    }

    public enum TestResult {
        LIKELY_PRIME,

        NOT_A_PRIME,

        CANCELLED
    }
}
