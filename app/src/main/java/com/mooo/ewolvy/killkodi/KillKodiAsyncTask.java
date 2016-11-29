package com.mooo.ewolvy.killkodi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class KillKodiAsyncTask extends AsyncTask <KKState, Void, KKState>{
    private Context context;
    private ProgressDialog pDialog;

    public KillKodiAsyncTask (Context cont){
        context = cont;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog (context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage(context.getString(R.string.pdialog_message));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(KKState k) {
        super.onPostExecute(k);
        pDialog.dismiss();
    }

    @Override
    protected KKState doInBackground(KKState... kkStates) {
        return null;
    }
}
