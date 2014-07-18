package at.fhj.gaar.asecrypto.mobile.ui.apptasks;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.ui.SectionAttachable;

/**
 * Base fragment.
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

        // sets the title of the fragment on the activity
        ((SectionAttachable) activity).onSectionAttached(getArguments()
                .getString(ARG_SECTION_TITLE));
    }

    /**
     * Attention: The bundle is not checked for being null.
     *
     * @param bundle The Bundle
     * @param identifier The identifier for saving the data into the Bundle
     * @param textView the TextView which should be used as data source
     */
    protected void saveTextFieldString(Bundle bundle, String identifier, TextView textView) {
        bundle.putString(identifier, textView.getText().toString());
    }

    /**
     * Attention: The bundle is not checked for being null.
     *
     * @param bundle The Bundle
     * @param identifier The identifier for saving the data into the Bundle
     * @param textView the TextView which should be used as data source
     */
    protected void saveTextFieldInteger(Bundle bundle, String identifier, TextView textView) {
        if (textView.getText().toString().length() > 0) {
            bundle.putInt(identifier, Integer.valueOf(textView.getText().toString()));
        }
    }

    /**
     * Attention: The bundle is not checked for being null.
     *
     * @param bundle The Bundle
     * @param identifier The identifier to look for in the Bundle
     * @param textView the TextView on which the data should be applied to
     */
    protected void restoreTextFieldString(Bundle bundle, String identifier, TextView textView) {
        String data = bundle.getString(identifier);
        if (data == null) {
            return;
        }

        textView.setText(data);
    }

    /**
     * Attention: Zero is not treated as number but rather as "no number available".
     * Also the bundle is not checked for being null!
     *
     * @param bundle The Bundle
     * @param identifier The identifier to look for in the Bundle
     * @param textView the TextView on which the data should be applied to
     */
    protected void restoreTextFieldInteger(Bundle bundle, String identifier, TextView textView) {
        int number = bundle.getInt(identifier);
        if (number != 0) {
            textView.setText(number);
        }
    }
}
