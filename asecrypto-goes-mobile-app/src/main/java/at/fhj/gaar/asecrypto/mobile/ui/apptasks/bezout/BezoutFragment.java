package at.fhj.gaar.asecrypto.mobile.ui.apptasks.bezout;

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
 * Implements the bezout algorithm iterative and recursive (Lab1_Task4)
 */
public class BezoutFragment extends BaseFragment
        implements View.OnClickListener, TaskFinishedCallable<BezoutResult> {

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

    private ProgressBar progressBar;

    private TextView lblFirstTargetNumber;

    private TextView lblSecondTargetNumber;

    private TextView lblResultNumberGcd;

    private TextView lblResultNumberX;

    private TextView lblResultNumberY;

    private TextView lblTimeMeasurement;

    private ScrollView scrollView;

    private AsyncTask<AseInteger, Void, BezoutResult> bezoutTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_bezout, container, false);

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

        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblFirstTargetNumber = (TextView) viewRoot.findViewById(R.id.lblFirstTargetNumber);
        lblSecondTargetNumber = (TextView) viewRoot.findViewById(R.id.lblSecondTargetNumber);
        lblResultNumberGcd = (TextView) viewRoot.findViewById(R.id.lblResultNumberGcd);
        lblResultNumberX = (TextView) viewRoot.findViewById(R.id.lblResultNumberX);
        lblResultNumberY = (TextView) viewRoot.findViewById(R.id.lblResultNumberY);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);
        scrollView = (ScrollView) viewRoot.findViewById(R.id.scrollView);

        btnIterative.setOnClickListener(this);
        btnRecursive.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (bezoutTask != null && !bezoutTask.isCancelled()) {
            bezoutTask.cancel(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (btnIterative.equals(view)) {
            startBezoutIterative();
        } else if (btnRecursive.equals(view)) {
            startBezoutRecursive();
        }
    }

    private void startBezoutIterative() {
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

        bezoutTask = new BezoutIterativeTask(this);
        bezoutTask.execute(firstNumber, secondNumber);
    }

    private void startBezoutRecursive() {
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

        if (firstNumber.bitLength() > 285 || secondNumber.bitLength() > 285) {
            Toast.makeText(getActivity(), "The maximum of allowed bits are 285. Otherwise, a " +
                            "stack overflow would occur.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        closeSoftKeyboard();
        doPostCalculationStartSetup(firstNumber, secondNumber);

        bezoutTask = new BezoutRecursiveTask(this);
        bezoutTask.execute(firstNumber, secondNumber);
    }

    private void doPostCalculationStartSetup(AseInteger firstNumber, AseInteger secondNumber) {
        progressBar.setVisibility(View.VISIBLE);

        lblFirstTargetNumber.setVisibility(View.VISIBLE);
        lblFirstTargetNumber.setText("First number: " + firstNumber.toString()); // TODO use StringBuilder
        lblSecondTargetNumber.setVisibility(View.VISIBLE);
        lblSecondTargetNumber.setText("Second number: " + secondNumber.toString()); // TODO use StringBuilder

        lblResultNumberGcd.setVisibility(View.INVISIBLE);
        lblResultNumberX.setVisibility(View.INVISIBLE);
        lblResultNumberY.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.INVISIBLE);

        btnIterative.setEnabled(false);
        btnRecursive.setEnabled(false);
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task, BezoutResult result) {
        Toast.makeText(getActivity(), "Bezout has been calculated", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.VISIBLE);
        lblResultNumberGcd.setVisibility(View.VISIBLE);
        lblResultNumberX.setVisibility(View.VISIBLE);
        lblResultNumberY.setVisibility(View.VISIBLE);

        btnIterative.setEnabled(true);
        btnRecursive.setEnabled(true);

        if (result.getGcd().equals(AseInteger.ZERO)) {
            lblResultNumberGcd.setText("Bezout: Stack overflow error");
        } else {
            lblResultNumberGcd.setText("GCD: " + result.getGcd().toString()); // TODO use StringBuilder
            lblResultNumberX.setText("X: " + result.getX().toString()); // TODO use StringBuilder
            lblResultNumberY.setText("Y: " + result.getY().toString()); // TODO use StringBuilder
        }

        lblTimeMeasurement.setText("Time taken: " + result.getMilliseconds() + " milliseconds"); // TODO use StringBuilder

        // scroll to bottom
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
