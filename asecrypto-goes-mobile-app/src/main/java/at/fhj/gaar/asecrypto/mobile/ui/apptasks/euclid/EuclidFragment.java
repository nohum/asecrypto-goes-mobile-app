package at.fhj.gaar.asecrypto.mobile.ui.apptasks.euclid;

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
 * Implements the euclidean algorithm iterative and recursive (Lab1_Task2)
 */
public class EuclidFragment extends BaseFragment
        implements View.OnClickListener, TaskFinishedCallable<EuclidResult> {

    private static final String ARG_FIRST_NUMBER = "first_number";

    private static final String ARG_SECOND_NUMBER = "second_number";

    private static final String ARG_FIRST_BITS = "first_bits";

    private static final String ARG_SECOND_BITS = "second_bits";

    private EditText txtFirstNumber;

    private EditText txtFirstBits;

    private EditText txtSecondNumber;

    private EditText txtSecondBits;

    private Button btnIterative;

    private Button btnRecursive;

    private Button btnFactorial;

    private ProgressBar progressBar;

    private TextView lblResultNumber;

    private TextView lblTimeMeasurement;

    private AsyncTask<AseInteger, Void, EuclidResult> euclidTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_euclid, container, false);

        txtFirstNumber = (EditText) viewRoot.findViewById(R.id.txtFirstNumber);
        txtSecondNumber = (EditText) viewRoot.findViewById(R.id.txtSecondNumber);
        txtFirstBits = (EditText) viewRoot.findViewById(R.id.txtFirstBits);
        txtSecondBits = (EditText) viewRoot.findViewById(R.id.txtSecondBits);
        btnIterative = (Button) viewRoot.findViewById(R.id.btnIterative);
        btnRecursive = (Button) viewRoot.findViewById(R.id.btnRecursive);
        btnFactorial = (Button) viewRoot.findViewById(R.id.btnFactorial);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblResultNumber = (TextView) viewRoot.findViewById(R.id.lblResultNumber);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);

        btnIterative.setOnClickListener(this);
        btnRecursive.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            return;
        }

        restoreTextFieldString(savedInstanceState, ARG_FIRST_NUMBER, txtFirstNumber);
        restoreTextFieldString(savedInstanceState, ARG_SECOND_NUMBER, txtSecondNumber);
        restoreTextFieldInteger(savedInstanceState, ARG_FIRST_BITS, txtFirstBits);
        restoreTextFieldInteger(savedInstanceState, ARG_SECOND_BITS, txtSecondBits);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveTextFieldString(outState, ARG_FIRST_NUMBER, txtFirstNumber);
        saveTextFieldString(outState, ARG_SECOND_NUMBER, txtSecondNumber);
        saveTextFieldInteger(outState, ARG_FIRST_BITS, txtFirstBits);
        saveTextFieldInteger(outState, ARG_SECOND_BITS, txtSecondBits);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (euclidTask != null && euclidTask.isCancelled()) {
            euclidTask.cancel(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (btnIterative.equals(view)) {
            startEuclidIterative();
        } else if (btnRecursive.equals(view)) {
            startEuclidRecursive();
        } else if (btnFactorial.equals(view)) {
            startEuclidFactorial();
        }
    }

    private void startEuclidIterative() {
        AseInteger firstNumber = retrieveAndDisplayNumber(txtFirstBits, txtFirstNumber,
                "First number");
        if (firstNumber == null) {
            return;
        }

        AseInteger secondNumber = retrieveAndDisplayNumber(txtSecondBits, txtSecondNumber,
                "Second number");
        if (secondNumber == null) {
            return;
        }

        euclidTask = new EuclidIterativeTask(this);
        euclidTask.execute(firstNumber, secondNumber);

        doPostCalculationStartSetup();
    }

    private void startEuclidRecursive() {
        AseInteger firstNumber = retrieveAndDisplayNumber(txtFirstBits, txtFirstNumber,
                "First number");
        if (firstNumber == null) {
            return;
        }

        AseInteger secondNumber = retrieveAndDisplayNumber(txtSecondBits, txtSecondNumber,
                "Second number");
        if (secondNumber == null) {
            return;
        }

        if (firstNumber.bitLength() > 512 || secondNumber.bitLength() > 512) {
            Toast.makeText(getActivity(), "The maximum of allowed bits are 512. Otherwise, a " +
                            "stack overflow would occur.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        euclidTask = new EuclidRecursiveTask(this);
        euclidTask.execute(firstNumber, secondNumber);

        doPostCalculationStartSetup();
    }

    private void startEuclidFactorial() {
        AseInteger firstNumber = retrieveAndDisplayNumber(txtFirstBits, txtFirstNumber,
                "First number");
        if (firstNumber == null) {
            return;
        }

        AseInteger secondNumber = retrieveAndDisplayNumber(txtSecondBits, txtSecondNumber,
                "Second number");
        if (secondNumber == null) {
            return;
        }

        euclidTask = new EuclidFactorialTask(this);
        euclidTask.execute(firstNumber, secondNumber);

        doPostCalculationStartSetup();
    }

    private AseInteger retrieveAndDisplayNumber(EditText bitField, EditText numberField,
                                                String whichNumberField) {
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

            Toast.makeText(getActivity(), whichNumberField
                    + ": You have to input either bits or a target number!", Toast.LENGTH_LONG)
                    .show(); // TODO use StringBuilder
            return null;
        }

        return targetNumber;
    }

    private void doPostCalculationStartSetup() {
        progressBar.setVisibility(View.VISIBLE);
        lblResultNumber.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.INVISIBLE);
        btnIterative.setEnabled(false);
        btnRecursive.setEnabled(false);
        btnFactorial.setEnabled(false);
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task, EuclidResult result) {
        Toast.makeText(getActivity(), "The GCD has been calculated", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.VISIBLE);
        lblResultNumber.setVisibility(View.VISIBLE);

        btnIterative.setEnabled(true);
        btnRecursive.setEnabled(true);
        btnFactorial.setEnabled(true);

        if (result.getGcd().equals(AseInteger.ZERO)) {
            lblResultNumber.setText("Greatest common divisor: Stack overflow error");
        } else {
            lblResultNumber.setText("Greatest common divisor: " + result.getGcd().toString()); // TODO use StringBuilder
        }
        lblTimeMeasurement.setText("Time taken: " + result.getMilliseconds() + "ms"); // TODO use StringBuilder
    }
}
