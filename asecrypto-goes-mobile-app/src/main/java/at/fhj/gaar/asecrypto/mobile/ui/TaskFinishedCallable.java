package at.fhj.gaar.asecrypto.mobile.ui;

import android.os.AsyncTask;

/**
 * Interface for classes that want to be notified if an AsyncTask has finished its execution.
 */
public interface TaskFinishedCallable<Result> {

    void onAsyncTaskFinished(AsyncTask task, Result result);
}
