package at.fhj.gaar.asecrypto.mobile.ui.apptasks.numbercounter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;

/**
 * Implements a number counter (Lab1_Task1)
 */
public class NumberCounterFragment extends BaseFragment {

    private static final String ARG_BIT_NUMBER = "bit_number";

    private static final String ARG_CONCRETE_NUMBER = "concrete_number";

    private EditText txtBitNumber;

    private EditText txtConcreteNumber;

    private Button btnCount;

    private ProgressBar progressBar;

    private TextView lblResultNumber;

    private TextView lblTimeMeasurement;

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

        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            int bits = savedInstanceState.getInt(ARG_BIT_NUMBER);
            if (bits > 0) {
                txtBitNumber.setText(bits);
            }

            txtConcreteNumber.setText(savedInstanceState.getString(ARG_CONCRETE_NUMBER));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_BIT_NUMBER, Integer.valueOf(txtBitNumber.getText().toString()));
        outState.putString(ARG_CONCRETE_NUMBER, txtConcreteNumber.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
