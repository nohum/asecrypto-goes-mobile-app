package at.fhj.gaar.asecrypto.mobile.ui.apptasks.euclid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;

/**
 * Implements the euclidean algorithm iterative and recursive (Lab1_Task2)
 */
public class EuclidFragment extends BaseFragment
        implements View.OnClickListener, TaskFinishedCallable<EuclidResult> {

    private ScrollView scrollView;

    private RadioButton rdbFirstBits;

    private RadioButton rdbFirstNumber;

    private EditText txtFirstNumber;

    private EditText txtFirstBits;

    private RadioButton rdbSecondBits;

    private RadioButton rdbSecondNumber;

    private EditText txtSecondNumber;

    private EditText txtSecondBits;

    private Button btnIterative;

    private Button btnRecursive;

    private Button btnFactorial;

    private ProgressBar progressBar;

    private TextView lblFirstTargetNumber;

    private TextView lblSecondTargetNumber;

    private TextView lblResultNumber;

    private TextView lblTimeMeasurement;

    private AsyncTask<AseInteger, Void, EuclidResult> euclidTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_euclid, container, false);

        scrollView = (ScrollView) viewRoot.findViewById(R.id.scrollView);

        rdbFirstBits = (RadioButton) viewRoot.findViewById(R.id.rdbFirstBits);
        rdbFirstNumber = (RadioButton) viewRoot.findViewById(R.id.rdbFirstNumber);
        rdbSecondBits = (RadioButton) viewRoot.findViewById(R.id.rdbSecondBits);
        rdbSecondNumber = (RadioButton) viewRoot.findViewById(R.id.rdbSecondNumber);

        txtFirstNumber = (EditText) viewRoot.findViewById(R.id.txtFirstNumber);
        txtSecondNumber = (EditText) viewRoot.findViewById(R.id.txtSecondNumber);
        txtFirstBits = (EditText) viewRoot.findViewById(R.id.txtFirstBits);
        txtSecondBits = (EditText) viewRoot.findViewById(R.id.txtSecondBits);

        btnIterative = (Button) viewRoot.findViewById(R.id.btnIterative);
        btnRecursive = (Button) viewRoot.findViewById(R.id.btnRecursive);
        btnFactorial = (Button) viewRoot.findViewById(R.id.btnFactorial);

        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblFirstTargetNumber = (TextView) viewRoot.findViewById(R.id.lblFirstTargetNumber);
        lblSecondTargetNumber = (TextView) viewRoot.findViewById(R.id.lblSecondTargetNumber);
        lblResultNumber = (TextView) viewRoot.findViewById(R.id.lblResultNumber);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);

        btnIterative.setOnClickListener(this);
        btnRecursive.setOnClickListener(this);
        btnFactorial.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTask();
    }

    private void cancelTask() {
        if (euclidTask != null && !euclidTask.isCancelled()) {
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
        AseInteger firstNumber = retrieveAseInteger(rdbFirstBits, txtFirstBits, rdbFirstNumber,
                txtFirstNumber, "First number");
        if (firstNumber == null) {
            return;
        }

        AseInteger secondNumber = retrieveAseInteger(rdbSecondBits, txtSecondBits, rdbSecondNumber,
                txtSecondNumber, "Second number");
        if (secondNumber == null) {
            return;
        }

        closeSoftKeyboard();
        doPostCalculationStartSetup(firstNumber, secondNumber);

        euclidTask = new EuclidIterativeTask(this);
        euclidTask.execute(firstNumber, secondNumber);
    }

    private void startEuclidRecursive() {
        AseInteger firstNumber = retrieveAseInteger(rdbFirstBits, txtFirstBits, rdbFirstNumber,
                txtFirstNumber, "First number");
        if (firstNumber == null) {
            return;
        }

        AseInteger secondNumber = retrieveAseInteger(rdbSecondBits, txtSecondBits, rdbSecondNumber,
                txtSecondNumber, "Second number");
        if (secondNumber == null) {
            return;
        }

        if (firstNumber.bitLength() > 512 || secondNumber.bitLength() > 512) {
            Toast.makeText(getActivity(), "The maximum of allowed bits are 512. Otherwise, a " +
                            "stack overflow would occur.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        closeSoftKeyboard();
        doPostCalculationStartSetup(firstNumber, secondNumber);

        euclidTask = new EuclidRecursiveTask(this);
        euclidTask.execute(firstNumber, secondNumber);
    }

    private void startEuclidFactorial() {
        AseInteger firstNumber = retrieveAseInteger(rdbFirstBits, txtFirstBits, rdbFirstNumber,
                txtFirstNumber, "First number");
        if (firstNumber == null) {
            return;
        }

        AseInteger secondNumber = retrieveAseInteger(rdbSecondBits, txtSecondBits, rdbSecondNumber,
                txtSecondNumber, "Second number");
        if (secondNumber == null) {
            return;
        }

        closeSoftKeyboard();
        doPostCalculationStartSetup(firstNumber, secondNumber);

        euclidTask = new EuclidFactorialTask(this);
        euclidTask.execute(firstNumber, secondNumber);
    }

    private void doPostCalculationStartSetup(AseInteger firstNumber, AseInteger secondNumber) {
        progressBar.setVisibility(View.VISIBLE);

        lblFirstTargetNumber.setVisibility(View.VISIBLE);
        lblFirstTargetNumber.setText("First number: " + firstNumber.toString()); // TODO use StringBuilder
        lblSecondTargetNumber.setVisibility(View.VISIBLE);
        lblSecondTargetNumber.setText("Second number: " + secondNumber.toString()); // TODO use StringBuilder

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

        lblResultNumber.setText("Greatest common divisor: " + result.getGcd().toString()); // TODO use StringBuilder
        lblTimeMeasurement.setText("Time taken: " + result.getMilliseconds() +
                " milliseconds"); // TODO use StringBuilder

        // scroll to bottom
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
