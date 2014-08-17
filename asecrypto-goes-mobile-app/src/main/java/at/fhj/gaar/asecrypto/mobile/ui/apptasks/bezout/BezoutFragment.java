package at.fhj.gaar.asecrypto.mobile.ui.apptasks.bezout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;
import at.fhj.gaar.asecrypto.mobile.util.NumberHelper;

/**
 * Implements the bezout algorithm iterative and recursive (Lab1_Task4)
 */
public class BezoutFragment extends BaseFragment
        implements View.OnClickListener, TaskFinishedCallable<BezoutResult> {

    private EditText txtFirstNumber;

    private EditText txtFirstBits;

    private EditText txtSecondNumber;

    private EditText txtSecondBits;

    private Button btnIterative;

    private Button btnRecursive;

    private ProgressBar progressBar;

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

        txtFirstNumber = (EditText) viewRoot.findViewById(R.id.txtFirstNumber);
        txtSecondNumber = (EditText) viewRoot.findViewById(R.id.txtSecondNumber);
        txtFirstBits = (EditText) viewRoot.findViewById(R.id.txtFirstBits);
        txtSecondBits = (EditText) viewRoot.findViewById(R.id.txtSecondBits);
        btnIterative = (Button) viewRoot.findViewById(R.id.btnIterative);
        btnRecursive = (Button) viewRoot.findViewById(R.id.btnRecursive);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
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

        closeSoftKeyboard();
        doPostCalculationStartSetup();

        bezoutTask = new BezoutIterativeTask(this);
        bezoutTask.execute(firstNumber, secondNumber);
    }

    private void startBezoutRecursive() {
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

        if (firstNumber.bitLength() > 285 || secondNumber.bitLength() > 285) {
            Toast.makeText(getActivity(), "The maximum of allowed bits are 285. Otherwise, a " +
                            "stack overflow would occur.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        closeSoftKeyboard();
        doPostCalculationStartSetup();

        bezoutTask = new BezoutRecursiveTask(this);
        bezoutTask.execute(firstNumber, secondNumber);
    }

    private void doPostCalculationStartSetup() {
        progressBar.setVisibility(View.VISIBLE);
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

        lblTimeMeasurement.setText("Time taken: " + result.getMilliseconds() + "ms"); // TODO use StringBuilder

        // scroll to bottom
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
