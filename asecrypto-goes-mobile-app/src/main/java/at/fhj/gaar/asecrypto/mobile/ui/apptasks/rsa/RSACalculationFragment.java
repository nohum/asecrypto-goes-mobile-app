package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

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

/**
 * Exercise 15: RSA speed up with chinse remainder
 */
public class RSACalculationFragment extends BaseFragment implements View.OnClickListener, TaskFinishedCallable<ParameterCalculationHelperTask.CalculationResult> {

    private static final String ARG_PRIME_P = "p";

    private static final String ARG_PRIME_Q = "q";

    private static final String ARG_DECRYPT_NUMBER = "decrypt_number";

    private static final String ARG_ENCRYPT_NUMBER = "encrypt_number";

    // General
    private EditText txtPrimeP;

    private EditText txtPrimeQ;

    private TextView lblCalculatedN;

    private TextView lblCalculatedPhiOfN;

    // Decryption
    private EditText txtNumberToDecrypt;

    private Button btnDecryptNormal;

    private Button btnDecryptCRT;


    // Encryption
    private EditText txtNumberToEncrypt;

    private Button btnEncrypt;


    // Again, general
    private ProgressBar progressBar;

    private TextView lblResult;

    private TextView lblTimeMeasurement;


    private AsyncTask<RSAAlgorithmArguments, Void, Void> rsaTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_rsa, container, false);

        txtPrimeP = (EditText) viewRoot.findViewById(R.id.txtPrimeP);
        txtPrimeQ = (EditText) viewRoot.findViewById(R.id.txtPrimeQ);
        lblCalculatedN = (TextView) viewRoot.findViewById(R.id.lblCalculatedN);
        lblCalculatedPhiOfN = (TextView) viewRoot.findViewById(R.id.lblCalculatedPhiOfN);

        txtNumberToDecrypt = (EditText) viewRoot.findViewById(R.id.txtNumberToDecrypt);
        btnDecryptNormal = (Button) viewRoot.findViewById(R.id.btnDecryptNormal);
        btnDecryptCRT = (Button) viewRoot.findViewById(R.id.btnDecryptCRT);

        txtNumberToEncrypt = (EditText) viewRoot.findViewById(R.id.txtNumberToEncrypt);
        btnEncrypt = (Button) viewRoot.findViewById(R.id.btnEncrypt);

        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblResult = (TextView) viewRoot.findViewById(R.id.lblResult);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);

        btnDecryptNormal.setOnClickListener(this);
        btnDecryptCRT.setOnClickListener(this);
        btnEncrypt.setOnClickListener(this);

        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            return;
        }

        restoreTextFieldString(savedInstanceState, ARG_PRIME_P, txtPrimeP);
        restoreTextFieldString(savedInstanceState, ARG_PRIME_Q, txtPrimeQ);
        restoreTextFieldInteger(savedInstanceState, ARG_DECRYPT_NUMBER, txtNumberToDecrypt);
        restoreTextFieldInteger(savedInstanceState, ARG_ENCRYPT_NUMBER, txtNumberToEncrypt);
    }

    @Override
    public void onStart() {
        super.onStart();

        // as the fragment becomes visible, calculate n and phi(n)
        // always to this in a seperate task as with very high numbers the UI thread is disturbed

        if (hasValidCryptoPrimes()) {
            AseInteger p = new AseInteger(txtPrimeP.getText().toString());
            AseInteger q = new AseInteger(txtPrimeQ.getText().toString());

            // result is obtained in onAsyncTaskFinished(AsyncTask task, CalculationResult calculationResult)
            new ParameterCalculationHelperTask(this).execute(p, q);
        }
    }

    private boolean hasValidCryptoPrimes() {
        // just simple checks if there is something inputted into the fields
        // as the fields by themselves are constrained to "only-numbers", a non-zero length
        // indicates a valid number
        return txtPrimeP.getText().length() > 0 && txtPrimeQ.getText().length() > 0;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveTextFieldString(outState, ARG_PRIME_P, txtPrimeP);
        saveTextFieldString(outState, ARG_PRIME_Q, txtPrimeQ);
        saveTextFieldInteger(outState, ARG_DECRYPT_NUMBER, txtNumberToDecrypt);
        saveTextFieldInteger(outState, ARG_ENCRYPT_NUMBER, txtNumberToEncrypt);
    }


    @Override
    public void onClick(View view) {
        // Always get rid of the soft keyboard
        closeSoftKeyboard();

        if (btnDecryptNormal.equals(view)) {

        } else if (btnDecryptCRT.equals(view)) {

        } else if (btnEncrypt.equals(view)) {

        }
    }

    private void prepareViewForResults() {
        progressBar.setVisibility(View.VISIBLE);

        lblResult.setVisibility(View.VISIBLE);
        lblResult.setText("");

        lblTimeMeasurement.setVisibility(View.VISIBLE);
        lblTimeMeasurement.setText("");
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task, ParameterCalculationHelperTask.CalculationResult calculationResult) {
        lblCalculatedN.setText("n: " + calculationResult.getN());// TODO use StringBuilder
        lblCalculatedPhiOfN.setText("phi(n): " + calculationResult.getPhiOfN());// TODO use StringBuilder
    }
}
