package at.fhj.gaar.asecrypto.mobile.ui.apptasks.exponentiation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;
import at.fhj.gaar.asecrypto.mobile.util.NumberHelper;

/**
 * Implements a slow exponentation (Lab2_Task1).
 */
public class SlowExponentiationFragment extends BaseFragment
        implements View.OnClickListener, TaskFinishedCallable<ExponentiationResult> {

    private static final String ARG_BIT_NUMBER = "bit_number";

    private static final String ARG_BASIS_NUMBER = "concrete_number";

    private static final String ARG_EXPONENT_NUMBER = "exponent_number";

    private static final String ARG_MODULUS_NUMBER = "modulus_number";

    protected EditText txtBitsOfNumber;

    protected EditText txtBasisNumber;

    protected EditText txtExponentNumber;

    protected EditText txtModulusNumber;

    private Button btnCalculate;

    private ProgressBar progressBar;

    private TextView lblResultNumber;

    private TextView lblTimeMeasurement;

    protected AsyncTask<AseInteger, Void, ExponentiationResult> exponentiationTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_exponentiation, container, false);

        txtBitsOfNumber = (EditText) viewRoot.findViewById(R.id.txtBitsOfNumber);
        txtBasisNumber = (EditText) viewRoot.findViewById(R.id.txtBasisNumber);
        txtExponentNumber = (EditText) viewRoot.findViewById(R.id.txtExponentNumber);
        txtModulusNumber = (EditText) viewRoot.findViewById(R.id.txtModulusNumber);
        btnCalculate = (Button) viewRoot.findViewById(R.id.btnCalculate);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblResultNumber = (TextView) viewRoot.findViewById(R.id.lblResultNumber);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);

        btnCalculate.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            return;
        }

        restoreTextFieldString(savedInstanceState, ARG_BIT_NUMBER, txtBitsOfNumber);

        restoreTextFieldInteger(savedInstanceState, ARG_BASIS_NUMBER, txtBasisNumber);
        restoreTextFieldInteger(savedInstanceState, ARG_EXPONENT_NUMBER, txtExponentNumber);
        restoreTextFieldInteger(savedInstanceState, ARG_MODULUS_NUMBER, txtModulusNumber);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveTextFieldInteger(outState, ARG_BIT_NUMBER, txtBitsOfNumber);

        saveTextFieldString(outState, ARG_BASIS_NUMBER, txtBasisNumber);
        saveTextFieldString(outState, ARG_EXPONENT_NUMBER, txtExponentNumber);
        saveTextFieldString(outState, ARG_MODULUS_NUMBER, txtModulusNumber);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exponentiationTask != null && exponentiationTask.isCancelled()) {
            exponentiationTask.cancel(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (btnCalculate.equals(view)) {
            startExponentiation();
        }
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

        exponentiationTask = new SlowExponentiationTask(this);
        exponentiationTask.execute(basisNumber, exponentNumber, modulusNumber);

        closeSoftKeyboard();
        putUIInWorkingState();
    }

    protected void putUIInWorkingState() {
        progressBar.setVisibility(View.VISIBLE);
        lblResultNumber.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.INVISIBLE);
        btnCalculate.setEnabled(false);
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task, ExponentiationResult exponentiationResult) {
        Toast.makeText(getActivity(), "Exponentiation has been finished",
                Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.VISIBLE);
        lblResultNumber.setVisibility(View.VISIBLE);
        btnCalculate.setEnabled(true);

        lblResultNumber.setText("Result: " + exponentiationResult.getExponentiationResult()); // TODO use StringBuilder
        lblTimeMeasurement.setText("Time taken: " + exponentiationResult.getWatchTime() + "ms"); // TODO use StringBuilder
    }
}
