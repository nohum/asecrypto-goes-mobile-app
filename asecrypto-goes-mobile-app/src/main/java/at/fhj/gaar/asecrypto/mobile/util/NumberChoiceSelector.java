package at.fhj.gaar.asecrypto.mobile.util;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.Random;

import at.fhj.gaar.asecrypto.mobile.crypto.AseInteger;

public class NumberChoiceSelector {

    private RadioButton rdbForBits;

    private EditText fieldForBits;

    private RadioButton rdbForOwnNumber;

    private EditText fieldForOwnNumber;

    public NumberChoiceSelector(RadioButton rdbForBits, EditText fieldForBits,
                                RadioButton rdbForOwnNumber, EditText fieldForOwnNumber) {
        this.rdbForBits = rdbForBits;
        this.fieldForBits = fieldForBits;
        this.rdbForOwnNumber = rdbForOwnNumber;
        this.fieldForOwnNumber = fieldForOwnNumber;
    }

    public AseInteger retrieveNumber() throws InvalidNumberException, InvalidBitsException,
            InvalidChoiceException {
        if (rdbForOwnNumber.isChecked()) {
            try {
                // if the user submits an empty number, this leads to an exception
                String concreteNumber = fieldForOwnNumber.getText().toString();
                return new AseInteger(concreteNumber);
            } catch (NumberFormatException e) {
                throw new InvalidNumberException(e);
            }
        } else if (rdbForBits.isChecked()) {
            if (!NumberHelper.isValidBitNumberInTextView(fieldForBits)) {
                throw new InvalidBitsException();
            }

            int bits = Integer.valueOf(fieldForBits.getText().toString());

            AseInteger result = new AseInteger(bits, new Random());
            return result.setBit(bits - 1);
        }

        throw new InvalidChoiceException();
    }

    public class InvalidNumberException extends Exception {
        public InvalidNumberException(Exception cause) {
            super(cause);
        }
    }

    public class InvalidBitsException extends Exception {
    }

    public class InvalidChoiceException extends Exception {
    }
}
