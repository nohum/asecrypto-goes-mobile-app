package at.fhj.gaar.asecrypto.mobile.ui.apptasks.carmichael;

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

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;
import at.fhj.gaar.asecrypto.mobile.util.NumberHelper;

/**
 * Implements: Lab3_Task1 - Fermat test
 */
public class CarmichaelGeneratorFragment extends BaseFragment implements View.OnClickListener,
        TaskFinishedCallable<CarmichaelResult> {

    private static final String ARG_BITS = "bits";

    private EditText txtBitsForNumbers;

    private Button btnStartGeneration;

    private Button btnCancel;

    private Button btnDoFermatTest;

    private ProgressBar progressBar;

    private TextView lblResultNumber;

    private TextView lblTimeMeasurement;

    private AsyncTask<Integer, Void, CarmichaelResult> carmichaelTask;

    private AseInteger generatedCarmichaelNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_carmichael, container, false);

        txtBitsForNumbers = (EditText) viewRoot.findViewById(R.id.txtBitsForNumbers);
        btnStartGeneration = (Button) viewRoot.findViewById(R.id.btnStartGeneration);
        btnCancel = (Button) viewRoot.findViewById(R.id.btnCancel);
        btnDoFermatTest = (Button) viewRoot.findViewById(R.id.btnDoFermatTest);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblResultNumber = (TextView) viewRoot.findViewById(R.id.lblResultNumber);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);

        btnStartGeneration.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDoFermatTest.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            return;
        }

        restoreTextFieldString(savedInstanceState, ARG_BITS, txtBitsForNumbers);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveTextFieldString(outState, ARG_BITS, txtBitsForNumbers);
    }

    @Override
    public void onPause() {
        super.onPause();

        cancelGeneration();
    }

    private void cancelGeneration() {
        if (carmichaelTask != null && !carmichaelTask.isCancelled()) {
            carmichaelTask.cancel(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (btnStartGeneration.equals(view)) {
            startGeneration();
        } else if (btnCancel.equals(view)) {
            cancelGeneration();
        } else if (btnDoFermatTest.equals(view)) {

        }
    }

    private void startGeneration() {
        int bits = 0;

        if (NumberHelper.isValidBitNumberInTextView(txtBitsForNumbers)) {
            bits = Integer.valueOf(txtBitsForNumbers.getText().toString());
        } else {
            Toast.makeText(getActivity(),
                    "You have to input a valid bit number!", Toast.LENGTH_LONG).show(); // TODO use StringBuilder
            return;
        }

        closeSoftKeyboard();
        doPostCalculationStartSetup();

        carmichaelTask = new CarmichaelFinderTask(this);
        carmichaelTask.execute(bits);
    }

    private void doPostCalculationStartSetup() {
        progressBar.setVisibility(View.VISIBLE);
        lblResultNumber.setVisibility(View.VISIBLE);
        lblResultNumber.setText("");
        lblTimeMeasurement.setVisibility(View.VISIBLE);
        lblTimeMeasurement.setText("");

        btnStartGeneration.setEnabled(false);
        btnCancel.setVisibility(View.VISIBLE);
        btnDoFermatTest.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task, CarmichaelResult carmichaelResult) {
        progressBar.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.VISIBLE);

        btnStartGeneration.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);

        if (carmichaelResult == null) { // Cancelled?
            lblResultNumber.setText("Generated number: Generation cancelled");
            lblTimeMeasurement.setText("");

            return;
        }

        lblResultNumber.setText("Generated number: " + carmichaelResult.getCarmichaelNumber()); // TODO use StringBuilder
        lblTimeMeasurement.setText("Taken time: " + carmichaelResult.getMilliseconds()); // TODO use StringBuilder

        btnDoFermatTest.setVisibility(View.VISIBLE);
        generatedCarmichaelNumber = carmichaelResult.getCarmichaelNumber();
    }
}
