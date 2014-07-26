package at.fhj.gaar.asecrypto.mobile.ui.apptasks.carmichael;

import android.os.AsyncTask;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

/**
 * Executes the Carmichael generation.
 */
public class CarmichaelFinderTask extends AsyncTask<Integer, Void, CarmichaelResult> {

    private final TaskFinishedCallable<CarmichaelResult> finishedCallable;

    private StopWatch watch;

    public CarmichaelFinderTask(TaskFinishedCallable<CarmichaelResult> finishedCallable) {
        this.finishedCallable = finishedCallable;

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
    protected CarmichaelResult doInBackground(Integer... numbers) {
        if (numbers.length != 1) {
            throw new RuntimeException("supply only one Integer for the bit number");
        }

        watch.start();

        watch.stop();
        return new CarmichaelResult(watch.getElapsedTime(), null);
    }

    @Override
    protected void onPostExecute(CarmichaelResult carmichaelResult) {
        super.onPostExecute(carmichaelResult);
        this.finishedCallable.onAsyncTaskFinished(this, carmichaelResult);
    }

    @Override
    protected void onCancelled(CarmichaelResult carmichaelResult) {
        super.onCancelled(carmichaelResult);
        this.finishedCallable.onAsyncTaskFinished(this, null);
    }
}
