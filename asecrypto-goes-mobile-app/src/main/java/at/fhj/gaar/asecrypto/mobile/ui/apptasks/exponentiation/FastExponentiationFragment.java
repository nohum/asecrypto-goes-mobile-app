package at.fhj.gaar.asecrypto.mobile.ui.apptasks.exponentiation;

import android.widget.Toast;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.util.NumberHelper;

/**
 * Implements a slow exponentation (Lab2_Task2).
 */
public class FastExponentiationFragment extends SlowExponentiationFragment {

    public FastExponentiationFragment() {
    }

    protected void startExponentiation() {
        String basisNumberAsText = txtBasisNumber.getText().toString();
        String exponentNumberAsText = txtExponentNumber.getText().toString();
        String modulusNumberAsText = txtModulusNumber.getText().toString();

        AseInteger basisNumber;
        AseInteger exponentNumber;
        AseInteger modulusNumber;

        if (NumberHelper.isValidBitNumberInTextView(txtBitsOfNumber)) {
            int bits = Integer.valueOf(txtBitsOfNumber.getText().toString());

            basisNumber = new AseInteger(bits, new Random());
            basisNumber = basisNumber.setBit(bits - 1); // assure it is a high number

            // exponent is 2^n-1
            exponentNumber = new AseInteger("2");
            exponentNumber = exponentNumber.pow(bits).subtract(AseInteger.ONE);

            // generate a random modulus
            modulusNumber = new AseInteger(bits, new Random());
            modulusNumber = modulusNumber.setBit(bits - 1); // assure it is a high number

            // show the numbers on the UI
            txtBasisNumber.setText(basisNumber.toString());
            txtExponentNumber.setText(exponentNumber.toString());
            txtModulusNumber.setText(modulusNumber.toString());
        } else if (basisNumberAsText.length() > 0 && exponentNumberAsText.length() > 0
                && modulusNumberAsText.length() > 0) {
            basisNumber = new AseInteger(basisNumberAsText);
            exponentNumber = new AseInteger(exponentNumberAsText);
            modulusNumber = new AseInteger(modulusNumberAsText);

            // TODO sanity checks?
        } else {
            Toast.makeText(getActivity(),
                    "You have to input either a bit number or your desired numbers!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        exponentiationTask = new FastExponentiationTask(this);
        exponentiationTask.execute(basisNumber, exponentNumber, modulusNumber);

        putUIInWorkingState();
        Toast.makeText(getActivity(), "Fast Exponentiation has been started", Toast.LENGTH_SHORT)
                .show();
    }

}
