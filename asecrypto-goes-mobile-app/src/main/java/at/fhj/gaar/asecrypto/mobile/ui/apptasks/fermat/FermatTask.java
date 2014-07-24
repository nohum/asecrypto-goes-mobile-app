package at.fhj.gaar.asecrypto.mobile.ui.apptasks.fermat;

import android.os.AsyncTask;

import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.TaskIntermediateCallable;

/**
 * Executes the Fermat test.
 */
public class FermatTask extends AsyncTask<FermatTaskArguments, FermatProgress, FermatResult> {

    private final TaskFinishedCallable<FermatResult> finishedCallable;

    private final TaskIntermediateCallable<FermatProgress> progressCallable;

    public FermatTask(TaskFinishedCallable<FermatResult> finishedCallable,
                      TaskIntermediateCallable<FermatProgress> progressCallable) {
        this.finishedCallable = finishedCallable;
        this.progressCallable = progressCallable;
    }

    @Override
    protected FermatResult doInBackground(FermatTaskArguments... numbers) {
        if (numbers.length != 1) {
            throw new RuntimeException("supply only one FermatTaskArguments");
        }

        return null;
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
