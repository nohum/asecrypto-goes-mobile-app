package at.fhj.gaar.asecrypto.mobile.ui.apptasks.exponentiation;

import android.os.AsyncTask;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

public class SlowExponentiationTask extends AsyncTask<AseInteger, Void, ExponentiationResult> {

    private final TaskFinishedCallable<ExponentiationResult> callable;

    public SlowExponentiationTask(TaskFinishedCallable<ExponentiationResult> callable) {
        this.callable = callable;
    }

    @Override
    protected ExponentiationResult doInBackground(AseInteger... numbers) {
        if (numbers.length != 3) {
            throw new RuntimeException("supply three AseIntegers at once (basis, exponent," +
                    " modulus)");
        }

        AseInteger basis = numbers[0];
        AseInteger exponent = numbers[1];
        AseInteger modulus = numbers[2];

        StopWatch watch = new StopWatch();
        watch.start();

        AseInteger result = AseInteger.ONE;
		for(AseInteger counter = AseInteger.ONE; counter.compareTo(exponent) <= 0;
                counter = counter.add(AseInteger.ONE)) {
			result = result.multiply(basis).mod(modulus);

            if (isCancelled()) {
                break;
            }
		}

        watch.stop();
        return new ExponentiationResult(watch.getElapsedTime(), result);
    }

    @Override
    protected void onPostExecute(ExponentiationResult exponentiationResult) {
        super.onPostExecute(exponentiationResult);
        callable.onAsyncTaskFinished(this, exponentiationResult);
    }
}
