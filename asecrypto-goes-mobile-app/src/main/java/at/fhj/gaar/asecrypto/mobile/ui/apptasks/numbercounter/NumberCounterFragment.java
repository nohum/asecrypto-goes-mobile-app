package at.fhj.gaar.asecrypto.mobile.ui.apptasks.numbercounter;

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

import java.math.BigInteger;

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;
import at.fhj.gaar.asecrypto.mobile.util.NumberHelper;

/**
 * Implements a number counter (Lab1_Task1)
 */
public class NumberCounterFragment extends BaseFragment
        implements View.OnClickListener, TaskFinishedCallable<Long> {

    private static final String ARG_BIT_NUMBER = "bit_number";

    private static final String ARG_CONCRETE_NUMBER = "concrete_number";

    private EditText txtBitNumber;

    private EditText txtConcreteNumber;

    private Button btnCount;

    private ProgressBar progressBar;

    private TextView lblResultNumber;

    private TextView lblTimeMeasurement;

    private AsyncTask<BigInteger, Void, Long> numberCounterTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_number_counter, container, false);

        txtBitNumber = (EditText) viewRoot.findViewById(R.id.txtBitNumber);
        txtConcreteNumber = (EditText) viewRoot.findViewById(R.id.txtConcreteNumber);
        btnCount = (Button) viewRoot.findViewById(R.id.btnCount);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblResultNumber = (TextView) viewRoot.findViewById(R.id.lblResultNumber);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);

        btnCount.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            return;
        }

        restoreTextFieldString(savedInstanceState, ARG_BIT_NUMBER, txtBitNumber);
        restoreTextFieldInteger(savedInstanceState, ARG_CONCRETE_NUMBER, txtConcreteNumber);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveTextFieldInteger(outState, ARG_BIT_NUMBER, txtBitNumber);
        saveTextFieldString(outState, ARG_CONCRETE_NUMBER, txtConcreteNumber);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (numberCounterTask != null && numberCounterTask.isCancelled()) {
            numberCounterTask.cancel(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (btnCount.equals(view)) {
            startCounting();
        }
    }

    private void startCounting() {
        String concreteNumber = txtConcreteNumber.getText().toString();

        BigInteger targetNumber;
        if (NumberHelper.isValidBitNumberInTextView(txtBitNumber)) {
            int bits = Integer.valueOf(txtBitNumber.getText().toString());
            targetNumber = new BigInteger("2");
            targetNumber = targetNumber.pow(bits).subtract(BigInteger.ONE);
        } else if (concreteNumber.length() > 0) {
            targetNumber = new BigInteger(concreteNumber);
        } else {
            Toast.makeText(getActivity(), "You have to input either bits or a target number!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        closeSoftKeyboard();

        numberCounterTask = new NumberCounterTask(this);
        numberCounterTask.execute(targetNumber);

        progressBar.setVisibility(View.VISIBLE);
        lblResultNumber.setVisibility(View.VISIBLE);
        lblTimeMeasurement.setVisibility(View.INVISIBLE);
        btnCount.setEnabled(false);

        lblResultNumber.setText("Target number: " + targetNumber); // TODO use StringBuilder
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task, Long elapsedTime) {
        Toast.makeText(getActivity(), "Counting has been finished", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.VISIBLE);
        btnCount.setEnabled(true);

        lblTimeMeasurement.setText("Time taken: " + elapsedTime + "ms"); // TODO use StringBuilder
    }
}
