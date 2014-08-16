package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import android.os.AsyncTask;

import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;

/**
 * Does RSA encryption
 */
public class RSAEncryptionTask extends AsyncTask<RSAEncryptionParameters, Void, RSAEncryptionResult> {

    private TaskFinishedCallable<RSAEncryptionResult> callable;

    public RSAEncryptionTask(TaskFinishedCallable<RSAEncryptionResult> callable) {
        this.callable = callable;
    }

    @Override
    protected RSAEncryptionResult doInBackground(RSAEncryptionParameters... parameters) {

        // check gcd(message, phi(n)) == 1

        return null;
    }

    @Override
    protected void onPostExecute(RSAEncryptionResult rsaEncryptionResult) {
        super.onPostExecute(rsaEncryptionResult);
        callable.onAsyncTaskFinished(this, rsaEncryptionResult);
    }
}
