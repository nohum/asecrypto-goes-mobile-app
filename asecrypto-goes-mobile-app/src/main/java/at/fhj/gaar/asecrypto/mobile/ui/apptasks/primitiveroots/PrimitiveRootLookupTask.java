package at.fhj.gaar.asecrypto.mobile.ui.apptasks.primitiveroots;

import android.os.AsyncTask;

import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

/**
 * Executes the search for a primitive root.
 */
public class PrimitiveRootLookupTask extends AsyncTask<Long, Void, PrimitiveRootResult> {

    private static long informUserAfterNTests = 3000;

    private final TaskFinishedCallable<PrimitiveRootResult> finishedCallable;

    private StopWatch watch;

    public PrimitiveRootLookupTask(TaskFinishedCallable<PrimitiveRootResult> finishedCallable) {
        this.finishedCallable = finishedCallable;

        watch = new StopWatch();
    }


    @Override
    protected PrimitiveRootResult doInBackground(Long... parameters) {
        if (parameters.length != 2) {
            throw new RuntimeException("supply two Longs (the bit count of the numbers, number of" +
                    " test runs)");
        }

        Long bitCount = parameters[0];
        if (bitCount == null) {
            throw new NullPointerException("invald bit count");
        }

        Long testRuns = parameters[1];
        if (testRuns == null) {
            throw new NullPointerException("invald run count");
        }

        watch.start();



        watch.stop();
        return null;
    }

    @Override
    protected void onPostExecute(PrimitiveRootResult primitiveRootResultData) {
        super.onPostExecute(primitiveRootResultData);

        finishedCallable.onAsyncTaskFinished(this, primitiveRootResultData);
    }
}
