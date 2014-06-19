package at.fhj.gaar.asecrypto.mobile.ui.apptasks;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.ui.SectionAttachable;

/**
 * Base fragment
 */
public class BaseFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * The fragment argument representing the section title for this fragment.
     */
    public static final String ARG_SECTION_TITLE = "section_title";

    public BaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((SectionAttachable) activity).onSectionAttached(getArguments().getString(ARG_SECTION_TITLE));
    }
}
