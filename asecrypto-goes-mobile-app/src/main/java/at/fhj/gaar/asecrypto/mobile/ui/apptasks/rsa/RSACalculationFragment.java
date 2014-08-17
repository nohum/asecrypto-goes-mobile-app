package at.fhj.gaar.asecrypto.mobile.ui.apptasks.rsa;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import at.fhj.gaar.asecrypto.mobile.ui.TaskFinishedCallable;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;

/**
 * Exercise 15: RSA speed up with chinse remainder
 */
public class RSACalculationFragment extends BaseFragment implements View.OnClickListener,
        TaskFinishedCallable<ParameterCalculationHelperTask.CalculationResult>, TextWatcher {

    private static final String ARG_PRIME_N = "n";

    private static final String ARG_PRIME_PHI_OF_N = "phi_of_n";

    // General
    private ScrollView scrollView;

    private EditText txtPrimeP;

    private EditText txtPrimeQ;

    private TextView lblCalculatedN;

    private TextView lblCalculatedPhiOfN;

    private EditText txtNumberE;

    private TextView lblCalculatedNumberD;

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


    private AseInteger n;

    private AseInteger phiOfN;

    private AseInteger d;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_rsa, container, false);

        scrollView = (ScrollView) viewRoot.findViewById(R.id.scrollView);
        txtPrimeP = (EditText) viewRoot.findViewById(R.id.txtPrimeP);
        txtPrimeQ = (EditText) viewRoot.findViewById(R.id.txtPrimeQ);
        lblCalculatedN = (TextView) viewRoot.findViewById(R.id.lblCalculatedN);
        lblCalculatedPhiOfN = (TextView) viewRoot.findViewById(R.id.lblCalculatedPhiOfN);
        txtNumberE = (EditText) viewRoot.findViewById(R.id.txtNumberE);
        lblCalculatedNumberD = (TextView) viewRoot.findViewById(R.id.lblCalculatedNumberD);

        txtNumberToDecrypt = (EditText) viewRoot.findViewById(R.id.txtNumberToDecrypt);
        btnDecryptNormal = (Button) viewRoot.findViewById(R.id.btnDecryptNormal);
        btnDecryptCRT = (Button) viewRoot.findViewById(R.id.btnDecryptCRT);

        txtNumberToEncrypt = (EditText) viewRoot.findViewById(R.id.txtNumberToEncrypt);
        btnEncrypt = (Button) viewRoot.findViewById(R.id.btnEncrypt);

        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);
        lblResult = (TextView) viewRoot.findViewById(R.id.lblResult);
        lblTimeMeasurement = (TextView) viewRoot.findViewById(R.id.lblTimeMeasurement);

        txtPrimeP.addTextChangedListener(this);
        txtPrimeQ.addTextChangedListener(this);
        txtNumberE.addTextChangedListener(this);

        btnDecryptNormal.setOnClickListener(this);
        btnDecryptCRT.setOnClickListener(this);
        btnEncrypt.setOnClickListener(this);

        return viewRoot;
    }


    @Override
    public void onStart() {
        super.onStart();

        // as the fragment becomes visible, calculate n and phi(n)
        calculateAdditionalParameters();
    }

    private void calculateAdditionalParameters() {
        lblCalculatedN.setText("n: will be calculated");
        lblCalculatedPhiOfN.setText("phi(n): will be calculated");
        lblCalculatedNumberD.setText("phi(n): will be calculated");

        // always to this in a seperate task as with very high numbers the UI thread is disturbed

        if (hasValidCryptoPrimes()) {
            AseInteger p = new AseInteger(txtPrimeP.getText().toString());
            AseInteger q = new AseInteger(txtPrimeQ.getText().toString());
            AseInteger e = null;

            if (txtNumberE.getText().length() > 0) {
                e = new AseInteger(txtNumberE.getText().toString());
            }

            // result is obtained in onAsyncTaskFinished(AsyncTask task, CalculationResult calculationResult)
            new ParameterCalculationHelperTask(this).execute(p, q, e);
        }
    }

    private boolean hasValidCryptoPrimes() {
        // just simple checks if there is something inputted into the fields
        // as the fields by themselves are constrained to "only-numbers", a non-zero length
        // indicates a valid number
        return txtPrimeP.getText().length() > 0 && txtPrimeQ.getText().length() > 0;
    }

    @Override
    public void onClick(View view) {
        // Always get rid of the soft keyboard
        closeSoftKeyboard();

        if (btnDecryptNormal.equals(view)) {
            doDecryption(false);
        } else if (btnDecryptCRT.equals(view)) {
            doDecryption(true);
        } else if (btnEncrypt.equals(view)) {
            doEncryption();
        }
    }

    private void doEncryption() {
        if (!validateGeneralParameters()) {
            return;
        }

        AseInteger message = checkAndGetNumber(txtNumberToEncrypt, "Number to encrypt");
        if (message == null) {
            return;
        }

        prepareViewForResults();

        RSAEncryptionTask encryptionTask = new RSAEncryptionTask(new TaskFinishedCallable<RSAResult>() {
            @Override
            public void onAsyncTaskFinished(AsyncTask task, RSAResult result) {
                handleResultForUser("Encryption", result);
            }
        });

        AseInteger e = new AseInteger(txtNumberE.getText().toString());

        //noinspection unchecked
        encryptionTask.execute(new RSAEncryptionParameters(n, e, message));
    }

    private void handleResultForUser(String type, RSAResult result) {
        progressBar.setVisibility(View.INVISIBLE);
        lblResult.setText(type + " result: " + result.getResult().toString() +
                " mod " + n.toString()); // TODO use StringBuilder

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

    private AseInteger checkAndGetNumber(EditText field, String fieldName) {
        try {
            return new AseInteger(field.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), fieldName + ": invalid number", Toast.LENGTH_LONG)
                    .show(); // TODO: use StringBuilder
            return null;
        }
    }

    private boolean validateGeneralParameters() {
        if (txtPrimeP.getText().length() == 0 || txtPrimeQ.getText().length() == 0) {
            Toast.makeText(getActivity(), "Please fill in all RSA general parameters",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (txtPrimeP.getText().toString().equals(txtPrimeQ.getText().toString())) {
            Toast.makeText(getActivity(), "Nice try, but p and q should be different",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        // these parameters are set in onAsyncTaskFinished(AsyncTask task, CalculationResult calculationResult)
        if (n == null || phiOfN == null || d == null) {
            Toast.makeText(getActivity(), "Please check all the RSA general parameters",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void doDecryption(boolean useChineseRemainderTheorem) {
        if (!validateGeneralParameters()) {
            return;
        }

        AseInteger message = checkAndGetNumber(txtNumberToDecrypt, "Number to decrypt");
        if (message == null) {
            return;
        }

        prepareViewForResults();

        RSADecryptionTask decryptionTask = new RSADecryptionTask(
                new TaskFinishedCallable<RSAResult>() {
            @Override
            public void onAsyncTaskFinished(AsyncTask task, RSAResult result) {
                handleResultForUser("Decryption", result);
            }
        });

        AseInteger p = new AseInteger(txtPrimeP.getText().toString());
        AseInteger q = new AseInteger(txtPrimeQ.getText().toString());

        //noinspection unchecked
        decryptionTask.execute(new RSADecryptionParameters(p, q, d, message,
                useChineseRemainderTheorem));
    }

    private void prepareViewForResults() {
        progressBar.setVisibility(View.VISIBLE);

        lblResult.setVisibility(View.VISIBLE);
        lblResult.setText("");

        lblTimeMeasurement.setVisibility(View.VISIBLE);
        lblTimeMeasurement.setText("");
    }

    @Override
    public void onAsyncTaskFinished(AsyncTask task,
                                    ParameterCalculationHelperTask.CalculationResult calculationResult) {
        // if p or q is not prime, the results are totally invalid
        // therefore display that to the user
        if (!calculationResult.isPPrime() || !calculationResult.isQPrime()) {
            String errorData = getPrimeErrorString(calculationResult.isPPrime(),
                    calculationResult.isQPrime());

            lblCalculatedPhiOfN.setText("phi(n): " + errorData);// TODO use StringBuilder
            lblCalculatedN.setText("n: " + errorData);// TODO use StringBuilder
            return;
        }

        lblCalculatedN.setText("n: " + calculationResult.getN());// TODO use StringBuilder
        lblCalculatedPhiOfN.setText("phi(n): " + calculationResult.getPhiOfN());// TODO use StringBuilder

        n = calculationResult.getN();
        phiOfN = calculationResult.getPhiOfN();

        if (calculationResult.isETooBig()) {
            lblCalculatedNumberD.setText("d: e must be smaller than phi(n)");
        } else if (!calculationResult.isGcdOfEAndPhiOfNEqualsOne()) {
            lblCalculatedNumberD.setText("d: gcd(e, phi(n)) is not 1");
        } else if (calculationResult.getDecryptionNumber() != null) {
            lblCalculatedNumberD.setText("d: " + calculationResult.getDecryptionNumber() +
                    " mod " + n); // TODO use StringBuilder

            d = calculationResult.getDecryptionNumber();
        }
    }

    private String getPrimeErrorString(boolean pPrime, boolean qPrime) {
        if (!pPrime && !qPrime) {
            return "p and q are composite numbers";
        }

        if (!pPrime) {
            return "p is a composite number";
        }

        return "q is a composite number";
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        n = null;
        phiOfN = null;
        d = null;

        if (txtPrimeP.getText().length() == 0 || txtPrimeQ.getText().length() == 0) {
            return;
        }

        // recalculate n, phi(n) and d
        calculateAdditionalParameters();
    }
}
