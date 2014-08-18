package at.fhj.gaar.asecrypto.mobile.ui.apptasks;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;
import at.fhj.gaar.asecrypto.mobile.ui.SectionAttachable;
import at.fhj.gaar.asecrypto.mobile.util.NumberChoiceSelector;
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

    protected AseInteger retrieveNumber(RadioButton bitChoiceButton, EditText bitField,
                                        RadioButton numberChoiceButton, EditText numberField,
                                        String whichNumberField) {

        NumberChoiceSelector selector = new NumberChoiceSelector(bitChoiceButton, bitField,
                numberChoiceButton, numberField);

        String userMessage;

        try {
            return selector.retrieveNumber();
        } catch (NumberChoiceSelector.InvalidNumberException e) {
            userMessage = "You have to input a valid target number!";
        } catch (NumberChoiceSelector.InvalidBitsException e) {
            userMessage = "You have to input a valid number of bits!";
        } catch (NumberChoiceSelector.InvalidChoiceException e) {
            userMessage = "Please select a mode of operation using the radio boxes!";
        }

        Toast.makeText(getActivity(), String.format("%s: %s", whichNumberField, userMessage),
                Toast.LENGTH_LONG).show();
        return null;
    }
}
