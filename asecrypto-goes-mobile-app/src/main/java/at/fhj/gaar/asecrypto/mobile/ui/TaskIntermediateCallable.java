package at.fhj.gaar.asecrypto.mobile.ui;

import android.os.AsyncTask;

/**
 * Interface for classes that want to be notified if an AsyncTask supplied a progress update.
 */
public interface TaskIntermediateCallable<Result> {

    void onAsyncTaskUpdate(AsyncTask task, Result result);
}
