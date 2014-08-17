package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import android.os.AsyncTask;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

/**
 * Does RSA encryption
 */
public class RSAEncryptionTask extends AsyncTask<RSAEncryptionParameters, Void, RSAResult> {

    private TaskFinishedCallable<RSAResult> callable;

    private StopWatch watch;

    public RSAEncryptionTask(TaskFinishedCallable<RSAResult> callable) {
        this.callable = callable;

        watch = new StopWatch();
    }

    @Override
    protected RSAResult doInBackground(RSAEncryptionParameters... parameters) {
        if (parameters.length != 1) {
            throw new RuntimeException("supply one instance of RSAEncryptionParameters");
        }

        RSAEncryptionParameters args = parameters[0];
        if (args == null) {
            throw new NullPointerException("invald args");
        }

        watch.start();
        AseInteger encrypted = encrypt(args.getMessage(), args.getN(), args.getE());
        watch.stop();

        return new RSAResult(encrypted, watch.getElapsedTime());
    }

    private AseInteger encrypt(AseInteger message, AseInteger n, AseInteger e) {
        return message.modPow(e, n);
    }

    @Override
    protected void onPostExecute(RSAResult rsaResult) {
        super.onPostExecute(rsaResult);
        callable.onAsyncTaskFinished(this, rsaResult);
    }
}
