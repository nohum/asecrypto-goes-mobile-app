package at.fhj.gaar.asecrypto.mobile.util;

import android.widget.TextView;

/**
 * Various helper methods for numbers.
 */
public class NumberHelper {

    public static boolean isValidBitNumberInTextView(TextView textView) {
        try {
            return textView.getText().toString().length() > 0
                    && Integer.valueOf(textView.getText().toString()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
