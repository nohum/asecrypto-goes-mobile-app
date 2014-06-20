package at.fhj.gaar.asecrypto.mobile.ui.apptasks.euclid;

import android.os.AsyncTask;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

public class EuclidRecursiveTask extends AsyncTask<AseInteger, Void, EuclidResult> {

    private final TaskFinishedCallable<EuclidResult> callable;

    public EuclidRecursiveTask(TaskFinishedCallable<EuclidResult> callable) {
        this.callable = callable;
    }

    @Override
    protected EuclidResult doInBackground(AseInteger... numbers) {
        if (numbers.length != 2) {
            throw new RuntimeException("supply exact two AseIntegers to calculate the GCD of them");
        }

        AseInteger firstNumber = numbers[0];
        AseInteger secondNumber = numbers[1];
        AseInteger gcd;

        StopWatch watch = new StopWatch();
        watch.start();

        gcd = firstNumber.getGcdRec(secondNumber);

        watch.stop();
        return new EuclidResult(watch.getElapsedTime(), gcd);
    }

    @Override
    protected void onPostExecute(EuclidResult euclidResult) {
        super.onPostExecute(euclidResult);
        this.callable.onAsyncTaskFinished(this, euclidResult);
    }
}
