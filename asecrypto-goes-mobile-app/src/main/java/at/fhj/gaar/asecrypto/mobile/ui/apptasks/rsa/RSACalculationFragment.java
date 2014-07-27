package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

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
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;
import at.fhj.gaar.asecrypto.mobile.util.NumberHelper;

/**
 * Exercise 15: RSA speed up with chinse remainder
 */
public class RSACalculationFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_BITS = "bits";

    private static final String ARG_CONCRETE_NUMBER = "concrete_number";

    private static final String ARG_NUMBER_OF_RUNS = "number_of_runs";

    private EditText txtBitsForNumber;

    private EditText txtConcreteNumber;

    private EditText txtNumberOfRuns;

    private Button btnStartTest;

    private Button btnCancel;

    private ProgressBar progressBar;

    private TextView lblTestResult;

    private TextView lblTimeMeasurement;

    private TextView lblTestNumber;

    private AsyncTask<RSAAlgorithmArguments, Void, Void> millerrabinTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_millerrabin, container, false);

        txtBitsForNumber = (EditText) viewRoot.findViewById(R.id.txtBitsForNumber);
        txtNumberOfRuns = (EditText) viewRoot.findViewById(R.id.txtNumberOfRuns);
        txtConcreteNumber = (EditText) viewRoot.findViewById(R.id.txtConcreteNumber);
        btnStartTest = (Button) viewRoot.findViewById(R.id.btnStartTest);
        btnCancel = (Button) viewRoot.findViewById(R.id.btnCancel);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblTestResult = (TextView) viewRoot.findViewById(R.id.lblTestResult);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);
        lblTestNumber = (TextView) viewRoot.findViewById(R.id.lblTestNumber);

        btnStartTest.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            return;
        }

        restoreTextFieldString(savedInstanceState, ARG_BITS, txtBitsForNumber);
        restoreTextFieldString(savedInstanceState, ARG_CONCRETE_NUMBER, txtConcreteNumber);
        restoreTextFieldInteger(savedInstanceState, ARG_NUMBER_OF_RUNS, txtNumberOfRuns);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveTextFieldString(outState, ARG_BITS, txtBitsForNumber);
        saveTextFieldString(outState, ARG_CONCRETE_NUMBER, txtConcreteNumber);
        saveTextFieldInteger(outState, ARG_NUMBER_OF_RUNS, txtNumberOfRuns);
    }

    @Override
    public void onPause() {
        super.onPause();

        cancelTesting();
    }

    private void cancelTesting() {
        if (millerrabinTask != null && !millerrabinTask.isCancelled()) {
            millerrabinTask.cancel(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (btnStartTest.equals(view)) {
            startTesting();
        } else if (btnCancel.equals(view)) {
            cancelTesting();
        }
    }

    private void startTesting() {
        AseInteger numberToTest = retrieveAndDisplayNumber(txtBitsForNumber, txtConcreteNumber,
                "Target number");
        if (numberToTest == null) {
            return;
        }

        long numberOfRuns;
        try {
            numberOfRuns = Long.parseLong(txtNumberOfRuns.getText().toString());
            if (numberOfRuns <= 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) { // also catches NumberFormatException
            Toast.makeText(getActivity(), "Number of runs"
                    + ": You have to input a valid number larger than zero!", Toast.LENGTH_LONG)
                    .show(); // TODO use StringBuilder
            return;
        }

        closeSoftKeyboard();
        doPostCalculationStartSetup(numberToTest);

    }

    private AseInteger retrieveAndDisplayNumber(EditText bitField, EditText numberField,
                                                String nameOfNumberField) {
        String concreteNumber = numberField.getText().toString();
        String bitFieldText = bitField.getText().toString();

        AseInteger targetNumber;
        if (NumberHelper.isValidBitNumberInTextView(bitField)) {
            int bits = Integer.valueOf(bitFieldText);

            targetNumber = new AseInteger(bits, new Random());
            targetNumber = targetNumber.setBit(bits - 1);

            // Display number for the user in the edit element
            numberField.setText(targetNumber.toString());
        } else if (concreteNumber.length() > 0) {
            targetNumber = new AseInteger(concreteNumber);
        } else {

            Toast.makeText(getActivity(), nameOfNumberField
                    + ": You have to input either bits or a target number!", Toast.LENGTH_LONG)
                    .show(); // TODO use StringBuilder
            return null;
        }

        return targetNumber;
    }

    private void doPostCalculationStartSetup(AseInteger testNumber) {
        progressBar.setVisibility(View.VISIBLE);
        lblTestResult.setVisibility(View.VISIBLE);
        lblTestResult.setText("");
        lblTimeMeasurement.setVisibility(View.VISIBLE);
        lblTimeMeasurement.setText("");
        lblTestNumber.setVisibility(View.VISIBLE);
        lblTestNumber.setText("Number to test: " + testNumber.toString()); // TODO use StringBuilder

        btnStartTest.setEnabled(false);
        btnCancel.setVisibility(View.VISIBLE);
    }

}
