package at.fhj.gaar.asecrypto.mobile.ui.apptasks;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.SectionAttachable;
import at.fhj.gaar.asecrypto.mobile.util.NumberHelper;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // sets the title of the fragment on the activity
        ((SectionAttachable) activity).onSectionAttached(getArguments()
                .getString(ARG_SECTION_TITLE));
    }

    /**
     * @link http://stackoverflow.com/a/7696791
     */
    protected void closeSoftKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        //check if no view has focus:
        View v = getActivity().getCurrentFocus();
        if(v == null) {
            return;
        }

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected AseInteger retrieveAndDisplayNumber(EditText bitField, EditText numberField,
                                                String whichNumberField) {
        String concreteNumber = numberField.getText().toString();

        AseInteger targetNumber;
        if (NumberHelper.isValidBitNumberInTextView(bitField)) {
            int bits = Integer.valueOf(bitField.getText().toString());

            targetNumber = new AseInteger(bits, new Random());
            targetNumber = targetNumber.setBit(bits - 1);

            // Display number for the user in the edit element
            numberField.setText(targetNumber.toString());
        } else if (concreteNumber.length() > 0) {
            targetNumber = new AseInteger(concreteNumber);
        } else {

            Toast.makeText(getActivity(), whichNumberField
                    + ": You have to input either bits or a target number!", Toast.LENGTH_LONG)
                    .show(); // TODO use StringBuilder
            return null;
        }

        return targetNumber;
    }
}
