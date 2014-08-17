package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import android.os.AsyncTask;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

/**
 * Does RSA decryption
 */
public class RSADecryptionTask extends AsyncTask<RSADecryptionParameters, Void, RSAResult> {

    private TaskFinishedCallable<RSAResult> callable;

    private StopWatch watch;

    public RSADecryptionTask(TaskFinishedCallable<RSAResult> callable) {
        this.callable = callable;

        watch = new StopWatch();
    }

    @Override
    protected RSAResult doInBackground(RSADecryptionParameters... parameters) {
        if (parameters.length != 1) {
            throw new RuntimeException("supply one instance of RSADecryptionParameters");
        }

        RSADecryptionParameters args = parameters[0];
        if (args == null) {
            throw new NullPointerException("invald args");
        }

        watch.start();
        AseInteger decrypted = decrypt(args.getMessage(), args.getP(), args.getQ(), args.getD(),
                args.isUseChineseRemainderTheorem());
        watch.stop();

        return new RSAResult(decrypted, watch.getElapsedTime());
    }

    private AseInteger decrypt(AseInteger message, AseInteger p, AseInteger q, AseInteger d,
                               boolean useChineseRemainderTheorem) {
        if (!useChineseRemainderTheorem) {
            return message.modPow(d, p.multiply(q));
        }

        return message.modPowChinese(d, p, q);
    }

    @Override
    protected void onPostExecute(RSAResult rsaResult) {
        super.onPostExecute(rsaResult);
        callable.onAsyncTaskFinished(this, rsaResult);
    }
}
