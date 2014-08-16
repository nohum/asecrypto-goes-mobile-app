package at.fhj.gaar.asecrypto.mobile.ui.apptasks.primitiveroots;

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
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;
import at.fhj.gaar.asecrypto.mobile.util.NumberHelper;

/**
 * Exercise 18: Primitive root finder
 */
public class PrimitiveRootFinderFragment extends BaseFragment implements View.OnClickListener,
        TaskFinishedCallable<PrimitiveRootResult> {

    private EditText txtBits;

    private EditText txtNumberOfRuns;

    private Button btnFindPrimitiveRoot;

    private Button btnCancel;

    private ProgressBar progressBar;

    private TextView lblPrime;

    private TextView lblModulusNumber;

    private TextView lblTimeMeasurement;

    private AsyncTask<FinderArguments, Void, PrimitiveRootResult> finderTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_primitive_roots, container, false);

        txtBits = (EditText) viewRoot.findViewById(R.id.txtBits);
        txtNumberOfRuns = (EditText) viewRoot.findViewById(R.id.txtNumberOfRuns);
        btnFindPrimitiveRoot = (Button) viewRoot.findViewById(R.id.btnFindPrimitiveRoot);
        btnCancel = (Button) viewRoot.findViewById(R.id.btnCancel);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblPrime = (TextView) viewRoot.findViewById(R.id.lblPrime);
        lblModulusNumber = (TextView) viewRoot.findViewById(R.id.lblModulusNumber);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);

        btnFindPrimitiveRoot.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelPrimitiveRootFinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelPrimitiveRootFinder();
    }

    private void cancelPrimitiveRootFinder() {
        if (finderTask != null && !finderTask.isCancelled()) {
            finderTask.cancel(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (btnFindPrimitiveRoot.equals(view)) {
            startPrimitiveRootFinder();
        } else if (btnCancel.equals(view)) {
            cancelPrimitiveRootFinder();

            progressBar.setVisibility(View.INVISIBLE);
            lblTimeMeasurement.setVisibility(View.INVISIBLE);
            lblPrime.setVisibility(View.INVISIBLE);
            lblModulusNumber.setVisibility(View.INVISIBLE);

            btnFindPrimitiveRoot.setEnabled(true);
            btnCancel.setVisibility(View.INVISIBLE);
        }
    }

    private void startPrimitiveRootFinder() {
        if (!NumberHelper.isValidBitNumberInTextView(txtBits)) {
            Toast.makeText(getActivity(), "Bit count"
                    + ": You have to input a valid number larger than zero!", Toast.LENGTH_LONG)
                    .show(); // TODO use StringBuilder
            return;
        }

        int bits = Integer.valueOf(txtBits.getText().toString());

        int numberOfRuns;
        try {
            numberOfRuns = Integer.parseInt(txtNumberOfRuns.getText().toString());
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
        doPostCalculationStartSetup();

        finderTask = new PrimitiveRootLookupTask(this);
        finderTask.execute(new FinderArguments(bits, numberOfRuns));
    }

    private void doPostCalculationStartSetup() {
        progressBar.setVisibility(View.VISIBLE);

        lblPrime.setVisibility(View.VISIBLE);
        lblPrime.setText("");
        lblTimeMeasurement.setVisibility(View.VISIBLE);
        lblTimeMeasurement.setText("");
        lblModulusNumber.setVisibility(View.VISIBLE);
        lblModulusNumber.setText("");

        btnFindPrimitiveRoot.setEnabled(false);
        btnCancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task, PrimitiveRootResult primitiveRootResult) {
        progressBar.setVisibility(View.INVISIBLE);
        lblTimeMeasurement.setVisibility(View.VISIBLE);

        btnFindPrimitiveRoot.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);

        if (primitiveRootResult == null) { // if task has been cancelled
            return;
        }

        lblPrime.setText("Found prime: " + primitiveRootResult.getPrime()); // TODO use StringBuilder
        lblModulusNumber.setText("Residue class: " + primitiveRootResult.getModulus()); // TODO use StringBuilder
        lblTimeMeasurement.setText("Total milliseconds: " + primitiveRootResult.getMilliseconds()); // TODO use StringBuilder
    }
}
