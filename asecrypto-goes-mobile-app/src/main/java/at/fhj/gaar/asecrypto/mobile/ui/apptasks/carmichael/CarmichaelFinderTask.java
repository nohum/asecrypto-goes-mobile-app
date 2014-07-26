package at.fhj.gaar.asecrypto.mobile.ui.apptasks.carmichael;

import android.os.AsyncTask;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.util.StopWatch;

/**
 * Executes the Carmichael number generation.
 */
public class CarmichaelFinderTask extends AsyncTask<Integer, Void, CarmichaelResult> {

    private static int numOfPrimeTests = 40; // how often the primality test has to be passed in order to be accepted as a prime

    private final TaskFinishedCallable<CarmichaelResult> finishedCallable;

    private StopWatch watch;

    public CarmichaelFinderTask(TaskFinishedCallable<CarmichaelResult> finishedCallable) {
        this.finishedCallable = finishedCallable;

        watch = new StopWatch();
    }

    @Override
    protected CarmichaelResult doInBackground(Integer... numbers) {
        if (numbers.length != 1) {
            throw new RuntimeException("supply only one Integer for the bit number");
        }

        if (numbers[0] == null) {
            throw new NullPointerException("numbers[0] must be non-null");
        }

        watch.start();
        AseInteger[] result = createCMNumber(numbers[0]);

        watch.stop();

        if (result == null) { // only if cancelled
            return null;
        }

        return new CarmichaelResult(watch.getElapsedTime(), result[0], result[1], result[2], result[3]);
    }

    public AseInteger[] createCMNumber(int bits) {

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // How to produce a Carmichael Number?
        // Use the method of Chernick:
        //
        // If 6m + 1, 12m + 1 und 18m + 1 are prime numbers, then (6m + 1)(12m + 1)(18m + 1) is a Carmichael-Number!
        //
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        AseInteger six = AseInteger.valueOf(6);
        AseInteger twelve = AseInteger.valueOf(12);
        AseInteger eighteen = AseInteger.valueOf(18);

        AseInteger m = new AseInteger(bits, new Random());
        AseInteger counter = AseInteger.ZERO; // used to determine when to output information
        AseInteger p1, p2, p3, phiCm;

        // increase m by one until all terms are prime
        do {
            counter = counter.add(AseInteger.ONE);
            m = m.add(AseInteger.ONE);

            p1 = six.multiply(m).add(AseInteger.ONE);
            p2 = twelve.multiply(m).add(AseInteger.ONE);
            p3 = eighteen.multiply(m).add(AseInteger.ONE);

            if (isCancelled()) {
                return null; // get out straight away
            }

        } while (!(
                p1.isProbablePrime(numOfPrimeTests) && p2.isProbablePrime(numOfPrimeTests)
                        && p3.isProbablePrime(numOfPrimeTests)
        ));

        // the carmichael number is found!
        AseInteger carmichael = p1.multiply(p2).multiply(p3);
        watch.stop();

        // calculate phi(carmichael) and charmichael-phi to output probability information
        phiCm = p1.subtract(AseInteger.ONE).multiply(p2.subtract(AseInteger.ONE))
                .multiply(p3.subtract(AseInteger.ONE));
        AseInteger antiPhiCm = carmichael.subtract(AseInteger.ONE).subtract(phiCm);

        return new AseInteger[] {
                carmichael,
                antiPhiCm,
                phiCm,
                carmichael.subtract(AseInteger.ONE).divide(antiPhiCm)
        };
    }

    @Override
    protected void onPostExecute(CarmichaelResult carmichaelResult) {
        super.onPostExecute(carmichaelResult);
        this.finishedCallable.onAsyncTaskFinished(this, carmichaelResult);
    }

    @Override
    protected void onCancelled(CarmichaelResult carmichaelResult) {
        super.onCancelled(carmichaelResult);
        this.finishedCallable.onAsyncTaskFinished(this, null);
    }
}
