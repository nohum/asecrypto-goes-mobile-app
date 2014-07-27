package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import android.os.AsyncTask;

/**
 * Does RSA encryption
 */
public class RSAEncryptionTask extends AsyncTask<RSAEncryptionParameters, Void, RSAEncryptionResult> {

    public RSAEncryptionTask() {

    }

    @Override
    protected RSAEncryptionResult doInBackground(RSAEncryptionParameters... rsaEncryptionParameters) {
        return null;
    }

    @Override
    protected void onPostExecute(RSAEncryptionResult rsaEncryptionResult) {
        super.onPostExecute(rsaEncryptionResult);
    }
}
