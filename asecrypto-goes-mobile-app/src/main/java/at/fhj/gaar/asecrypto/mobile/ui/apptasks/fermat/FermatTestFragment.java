package at.fhj.gaar.asecrypto.mobile.ui.apptasks.fermat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.TaskIntermediateCallable;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;

/**
 * Implements: Lab3_Task1 - Fermat test
 */
public class FermatTestFragment extends BaseFragment implements View.OnClickListener,
        TaskFinishedCallable<FermatResult>, TaskIntermediateCallable<FermatProgress> {

    private String defaultConcreteTestNumber;

    private RadioButton rdbBits;

    private EditText txtBitsForNumber;

    private RadioButton rdbManually;

    private EditText txtConcreteNumber;

    private EditText txtNumberOfRuns;

    private Button btnStartTest;

    private Button btnCancel;

    private ProgressBar progressBar;

    private TextView lblTestResult;

    private TextView lblTimeMeasurement;

    private TextView lblTestNumber;

    private AsyncTask<FermatTaskArguments, FermatProgress, FermatResult> fermatTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_fermat, container, false);

        rdbBits = (RadioButton) viewRoot.findViewById(R.id.rdbBits);
        txtBitsForNumber = (EditText) viewRoot.findViewById(R.id.txtBitsForNumber);
        rdbManually = (RadioButton) viewRoot.findViewById(R.id.rdbManually);
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
    public void onStart() {
        super.onStart();

        // insert a specified test number (if given by MainActivity or any other caller)
        if (defaultConcreteTestNumber != null) {
            rdbManually.setChecked(true);
            txtConcreteNumber.setText(defaultConcreteTestNumber);

            defaultConcreteTestNumber = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        cancelTesting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTesting();
    }

    private void cancelTesting() {
        if (fermatTask != null && !fermatTask.isCancelled()) {
            fermatTask.cancel(true);
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
        AseInteger numberToTest = retrieveAseInteger(rdbBits, txtBitsForNumber, rdbManually,
                txtConcreteNumber, "Target number");
        if (numberToTest == null) {
            return;
        }

        Long numberOfRuns = retrieveLong(txtNumberOfRuns, "Number of runs");
        if (numberOfRuns == null) {
            return;
        }

        closeSoftKeyboard();
        doPostCalculationStartSetup(numberToTest);

        fermatTask = new FermatTask(this, this);
        fermatTask.execute(new FermatTaskArguments(numberToTest, numberOfRuns));
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

    @Override
    public void onAsyncTaskFinished(AsyncTask task, FermatResult fermatResult) {
        Toast.makeText(getActivity(), "Finished Fermat test", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.VISIBLE);

        btnStartTest.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);

        if (fermatResult == null) { // Cancelled?
            lblTestResult.setText("Final result: Test cancelled");
            lblTimeMeasurement.setText("");

            return;
        }

        lblTestResult.setText("Final result: "
                + (fermatResult.hasTestSucceeded() ? "Number is prime" : "Number is composite")); // TODO use StringBuilder
        lblTimeMeasurement.setText("Total milliseconds: " + fermatResult.getMilliseconds()); // TODO use StringBuilder
    }

    @Override
    public void onAsyncTaskUpdate(AsyncTask task, FermatProgress fermatProgress) {
        lblTestResult.setText("Current test count: " + fermatProgress.getCurrentTestCount()); // TODO use StringBuilder
        lblTimeMeasurement.setText("Current milliseconds: " + fermatProgress.getCurrentMilliseconds()); // TODO use StringBuilder
    }

    public void setConcreteTestNumber(String number) {
        if (txtConcreteNumber != null) {
            txtConcreteNumber.setText(number);
            return;
        }

        defaultConcreteTestNumber = number;
    }
}
