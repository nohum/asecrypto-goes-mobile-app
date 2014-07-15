package at.fhj.gaar.asecrypto.mobile.ui.apptasks.numbercounter;

import android.os.AsyncTask;

import java.math.BigInteger;

import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

public class NumberCounterTask extends AsyncTask<BigInteger, Void, Long> {

    private final TaskFinishedCallable<Long> callable;

    public NumberCounterTask(TaskFinishedCallable<Long> callable) {
        this.callable = callable;
    }

    @Override
    protected Long doInBackground(BigInteger... numbers) {
        if (numbers.length > 1 || numbers.length == 0) {
            throw new RuntimeException("supply one BigInteger at a time");
        }

        BigInteger target = numbers[0];

        StopWatch watch = new StopWatch();
        watch.start();

        BigInteger counter = BigInteger.ZERO;
        while (target.compareTo(counter) > 0) {
            counter = counter.add(BigInteger.ONE);

            if (isCancelled()) {
                break;
            }
        }

        watch.stop();
        return watch.getElapsedTime();
    }

    @Override
    protected void onPostExecute(Long elapsedTime) {
        super.onPostExecute(elapsedTime);
        callable.onAsyncTaskFinished(this, elapsedTime);
    }
}
