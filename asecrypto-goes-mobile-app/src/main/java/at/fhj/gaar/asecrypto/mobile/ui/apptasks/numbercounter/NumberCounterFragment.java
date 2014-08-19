package at.fhj.gaar.asecrypto.mobile.ui.apptasks.numbercounter;

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

    private RadioButton rdbBits;

    private EditText txtBitNumber;

    private RadioButton rdbOwnNumber;

    private EditText txtConcreteNumber;

    private Button btnCount;

    private Button btnCancel;

    private ProgressBar progressBar;

    private TextView lblResultNumber;

    private TextView lblTimeMeasurement;

    private AsyncTask<BigInteger, Void, Long> numberCounterTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_number_counter, container, false);

        rdbBits = (RadioButton) viewRoot.findViewById(R.id.rdbBits);
        txtBitNumber = (EditText) viewRoot.findViewById(R.id.txtBitNumber);
        rdbOwnNumber = (RadioButton) viewRoot.findViewById(R.id.rdbOwnNumber);
        txtConcreteNumber = (EditText) viewRoot.findViewById(R.id.txtConcreteNumber);
        btnCount = (Button) viewRoot.findViewById(R.id.btnCount);
        btnCancel = (Button) viewRoot.findViewById(R.id.btnCancel);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblResultNumber = (TextView) viewRoot.findViewById(R.id.lblResultNumber);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);

        btnCount.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelTask();
    }

    private void cancelTask() {
        if (numberCounterTask != null && !numberCounterTask.isCancelled()) {
            numberCounterTask.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTask();
    }

    @Override
    public void onClick(View view) {
        if (btnCount.equals(view)) {
            startCounting();
        } else if (btnCancel.equals(view)) {
            resetUI();
            cancelTask();
        }
    }

    private void startCounting() {
        BigInteger targetNumber = retrieveTargetNumber();
        if (targetNumber == null) {
            return;
        }

        closeSoftKeyboard();

        numberCounterTask = new NumberCounterTask(this);
        numberCounterTask.execute(targetNumber);

        progressBar.setVisibility(View.VISIBLE);
        lblResultNumber.setVisibility(View.VISIBLE);
        lblTimeMeasurement.setVisibility(View.INVISIBLE);

        btnCount.setEnabled(false);
        btnCancel.setVisibility(View.VISIBLE);

        lblResultNumber.setText("Target number: " + targetNumber); // TODO use StringBuilder
    }

    private BigInteger retrieveTargetNumber() {
        if (rdbOwnNumber.isChecked()) {
            try {
                // if the user submits an empty number, this leads to an exception
                String concreteNumber = txtConcreteNumber.getText().toString();
                return new BigInteger(concreteNumber);
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "You have to input a valid target number!",
                        Toast.LENGTH_LONG).show();
                return null;
            }
        } else if (rdbBits.isChecked()) {
            if (!NumberHelper.isValidBitNumberInTextView(txtBitNumber)) {
                Toast.makeText(getActivity(), "You have to input a valid number of bits!",
                        Toast.LENGTH_LONG).show();
                return null;
            }

            int bits = Integer.valueOf(txtBitNumber.getText().toString());
            BigInteger result = new BigInteger("2");
            return result.pow(bits).subtract(BigInteger.ONE);
        }

        Toast.makeText(getActivity(),
                "Please select a mode of operation using the radio boxes!",
                Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task, Long elapsedTime) {
        Toast.makeText(getActivity(), "Counting has been finished", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.VISIBLE);

        btnCount.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);

        lblTimeMeasurement.setText("Time taken: " + elapsedTime + " milliseconds"); // TODO use StringBuilder
    }

    private void resetUI() {
        progressBar.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.INVISIBLE);
        lblResultNumber.setVisibility(View.INVISIBLE);

        btnCount.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);
    }
}
