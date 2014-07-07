package at.fhj.gaar.asecrypto.mobile.ui.apptasks.bezout;

import android.os.AsyncTask;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

public class BezoutRecursiveTask extends AsyncTask<AseInteger, Void, BezoutResult> {

    private final TaskFinishedCallable<BezoutResult> callable;

    public BezoutRecursiveTask(TaskFinishedCallable<BezoutResult> callable) {
        this.callable = callable;
    }

    @Override
    protected BezoutResult doInBackground(AseInteger... numbers) {
        if (numbers.length != 2) {
            throw new RuntimeException("supply exact two AseIntegers to calculate the Bezout");
        }

        AseInteger firstNumber = numbers[0];
        AseInteger secondNumber = numbers[1];
        AseInteger[] result;

        StopWatch watch = new StopWatch();
        watch.start();

        result = firstNumber.getBezoutRec(secondNumber);

        watch.stop();
        return new BezoutResult(watch.getElapsedTime(), result[0], result[1], result[2]);
    }

    @Override
    protected void onPostExecute(BezoutResult bezoutResult) {
        super.onPostExecute(bezoutResult);
        this.callable.onAsyncTaskFinished(this, bezoutResult);
    }
}
