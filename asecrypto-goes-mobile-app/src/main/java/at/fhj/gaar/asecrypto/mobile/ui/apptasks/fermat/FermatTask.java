package at.fhj.gaar.asecrypto.mobile.ui.apptasks.fermat;

import android.os.AsyncTask;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.TaskIntermediateCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

/**
 * Executes the Fermat test.
 */
public class FermatTask extends AsyncTask<FermatTaskArguments, FermatProgress, FermatResult> {

    private static long informUserAfterNTests = 10000;

    private final TaskFinishedCallable<FermatResult> finishedCallable;

    private final TaskIntermediateCallable<FermatProgress> progressCallable;
    private StopWatch watch;

    public FermatTask(TaskFinishedCallable<FermatResult> finishedCallable,
                      TaskIntermediateCallable<FermatProgress> progressCallable) {
        this.finishedCallable = finishedCallable;
        this.progressCallable = progressCallable;

        watch = new StopWatch();
    }

    /**
     * This function creates a random AseInteger between 0 and modulus
     * You can use this number as a witness in a Fermat test to check the primality of modulus
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
    protected FermatResult doInBackground(FermatTaskArguments... numbers) {
        if (numbers.length != 1) {
            throw new RuntimeException("supply only one FermatTaskArguments");
        }

        FermatTaskArguments arguments = numbers[0];
        if (arguments == null) {
            throw new NullPointerException("invald FermatTaskArguments");
        }


        watch.start();

        boolean testResult = run(arguments.getNumberToTest(), arguments.getNumberOfTestRuns());

        watch.stop();
        return new FermatResult(testResult, watch.getElapsedTime());
    }

    /**
     * @param possiblePrime is the number to check for primality
     * @param numOfTests    is the number of times you want the test to run
     * @return gives true if possiblePrime passed all tests, else false
     */
    public boolean run(AseInteger possiblePrime, final long numOfTests) {
        if (numOfTests <= 0) {
            return false;
        }

        // if possiblePrime is really prime, its phi is its value-1
        AseInteger exponent = possiblePrime.subtract(AseInteger.ONE);

        ////////////////////////////////////////////////////////////////////
        // start a series of numOfTests FermatTests
        // that is exponentiate a test number
        long counter;
        for (counter = 1; counter <= numOfTests; counter++) {

            // create a random AseInteger as witness
            AseInteger a = createRandomTestNumber(possiblePrime);

            // check if a^(p-1) mod p <> 1
            if (a.modPow(exponent, possiblePrime).compareTo(AseInteger.ONE) != 0) {
                publishProgress(new FermatProgress(watch.getElapsedTime(), counter));

                // fail
                return false;
            }

            // inform the user of current state
            if (counter % informUserAfterNTests == 0) {
                publishProgress(new FermatProgress(watch.getElapsedTime(), counter));
            }

            if (isCancelled()) {
                break; // handle like success
            }
        }

        // adjust counter to the number of tests actually done for correct output
        counter--;

        // all Fermat tests succeeded
        publishProgress(new FermatProgress(watch.getElapsedTime(), counter));
        return true;
    }

    @Override
    protected void onProgressUpdate(FermatProgress... fermatProgresses) {
        super.onProgressUpdate(fermatProgresses);

        if (fermatProgresses[0] == null) {
            throw new IllegalStateException("first element must be non-null");
        }

        if (fermatProgresses.length != 1) {
            throw new IllegalStateException("supply exactly one progress element");
        }

        this.progressCallable.onAsyncTaskUpdate(this, fermatProgresses[0]);
    }

    @Override
    protected void onPostExecute(FermatResult fermatResult) {
        super.onPostExecute(fermatResult);
        this.finishedCallable.onAsyncTaskFinished(this, fermatResult);
    }

}
