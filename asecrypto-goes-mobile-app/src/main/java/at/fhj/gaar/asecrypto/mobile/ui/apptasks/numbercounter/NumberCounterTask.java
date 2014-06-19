package at.fhj.gaar.asecrypto.mobile.ui.apptasks.numbercounter;

import android.os.AsyncTask;

public class NumberCounterTask extends AsyncTask<NumberCounterTask.Parameters, Void, Integer> {

    @Override
    protected Integer doInBackground(Parameters... parameters) {
        return null;
    }

    public static class Parameters {

        public Parameters(int bits) {

        }

        public Parameters(String targetNumber) {

        }

    }
}
