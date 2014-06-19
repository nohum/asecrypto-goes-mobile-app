package at.fhj.gaar.asecrypto.mobile.ui.apptasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.fhj.gaar.asecrypto.mobile.R;

/**
 * Implements a number counter.
 */
public class NumberCounterFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_number_counter, container, false);
    }

}
