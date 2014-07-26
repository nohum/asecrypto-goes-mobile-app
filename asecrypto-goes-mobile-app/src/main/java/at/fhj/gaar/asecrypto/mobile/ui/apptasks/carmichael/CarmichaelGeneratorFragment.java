package at.fhj.gaar.asecrypto.mobile.ui.apptasks.carmichael;

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

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.MainActivity;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;
import at.fhj.gaar.asecrypto.mobile.util.NumberHelper;

/**
 * Lab3_Task1: Carmichaal number generation
 */
public class CarmichaelGeneratorFragment extends BaseFragment implements View.OnClickListener,
        TaskFinishedCallable<CarmichaelResult> {

    private static final String ARG_BITS = "bits";

    private EditText txtBitsForNumbers;

    private Button btnStartGeneration;

    private Button btnCancel;

    private Button btnDoFermatTest;

    private Button btnDoMillerRabinTest;

    private ProgressBar progressBar;

    private TextView lblResultNumber;

    private TextView lblFailNumbersCount;

    private TextView lblSuccessNumbersCount;

    private TextView lblFailChance;

    private TextView lblTimeMeasurement;

    private ScrollView scrollView;

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
        btnDoMillerRabinTest = (Button) viewRoot.findViewById(R.id.btnDoMillerRabinTest);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblResultNumber = (TextView) viewRoot.findViewById(R.id.lblResultNumber);
        lblFailNumbersCount = (TextView) viewRoot.findViewById(R.id.lblFailNumbersCount);
        lblSuccessNumbersCount = (TextView) viewRoot.findViewById(R.id.lblSuccessNumbersCount);
        lblFailChance = (TextView) viewRoot.findViewById(R.id.lblFailChance);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);
        scrollView = (ScrollView) viewRoot.findViewById(R.id.scrollView);

        btnStartGeneration.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDoFermatTest.setOnClickListener(this);
        btnDoMillerRabinTest.setOnClickListener(this);

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
            switchToFermatTest();
        } else if (btnDoMillerRabinTest.equals(view)) {
            switchToMillerRabinTest();
        }
    }

    private void switchToFermatTest() {
        Toast.makeText(getActivity(),
                "Switching now to Fermat example with your desired Carmichal number",
                Toast.LENGTH_SHORT).show();

        ((MainActivity) getActivity()).openFermatTest(generatedCarmichaelNumber.toString());
    }

    private void switchToMillerRabinTest() {
        Toast.makeText(getActivity(),
                "Switching now to Miller-Rabin example with your desired Carmichal number",
                Toast.LENGTH_SHORT).show();

        ((MainActivity) getActivity()).openMillerRabinTest(generatedCarmichaelNumber.toString());
    }

    private void startGeneration() {
        int bits;

        if (NumberHelper.isValidBitNumberInTextView(txtBitsForNumbers)) {
            bits = Integer.valueOf(txtBitsForNumbers.getText().toString());
        } else {
            Toast.makeText(getActivity(),
                    "You have to input a valid bit number!", Toast.LENGTH_LONG).show(); // TODO use StringBuilder
            return;
        }

        if (bits < 4) {
            Toast.makeText(getActivity(),
                    "You have to input a bit number greater than 4!", Toast.LENGTH_LONG).show(); // TODO use StringBuilder
            return;
        }

        closeSoftKeyboard();
        doPostCalculationStartSetup();

        carmichaelTask = new CarmichaelFinderTask(this);
        carmichaelTask.execute(bits);
    }

    private void doPostCalculationStartSetup() {
        progressBar.setVisibility(View.VISIBLE);

        lblResultNumber.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.INVISIBLE);
        lblFailNumbersCount.setVisibility(View.INVISIBLE);
        lblSuccessNumbersCount.setVisibility(View.INVISIBLE);
        lblFailChance.setVisibility(View.INVISIBLE);

        btnStartGeneration.setEnabled(false);
        btnCancel.setVisibility(View.VISIBLE);
        btnDoFermatTest.setVisibility(View.INVISIBLE);
        btnDoMillerRabinTest.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task, CarmichaelResult carmichaelResult) {
        progressBar.setVisibility(View.INVISIBLE);

        lblResultNumber.setVisibility(View.VISIBLE);
        lblTimeMeasurement.setVisibility(View.VISIBLE);
        lblFailNumbersCount.setVisibility(View.VISIBLE);
        lblSuccessNumbersCount.setVisibility(View.VISIBLE);
        lblFailChance.setVisibility(View.VISIBLE);

        btnStartGeneration.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);

        handleResultDisplay(carmichaelResult);
    }

    private void handleResultDisplay(CarmichaelResult carmichaelResult) {
        if (carmichaelResult == null) { // Cancelled?
            lblResultNumber.setText("Generated number: Generation cancelled");
            lblTimeMeasurement.setText("");
            lblFailNumbersCount.setText("");
            lblSuccessNumbersCount.setText("");
            lblFailChance.setText("");

            return;
        }

        lblResultNumber.setText("Generated number: " + carmichaelResult.getCarmichaelNumber()); // TODO use StringBuilder
        lblTimeMeasurement.setText("Taken milliseconds: " + carmichaelResult.getMilliseconds()); // TODO use StringBuilder
        lblFailNumbersCount.setText("Numbers for which Fermat will fail: "
                + carmichaelResult.getFailNumbersCount()); // TODO use StringBuilder
        lblSuccessNumbersCount.setText("Numbers for which Fermat will succeed: "
                + carmichaelResult.getSuccessNumbersCount()); // TODO use StringBuilder
        lblFailChance.setText("Fail chance: 1/" + carmichaelResult.getFailChance()); // TODO use StringBuilder

        btnDoFermatTest.setVisibility(View.VISIBLE);
        btnDoMillerRabinTest.setVisibility(View.VISIBLE);
        generatedCarmichaelNumber = carmichaelResult.getCarmichaelNumber();

        // scroll to bottom
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
