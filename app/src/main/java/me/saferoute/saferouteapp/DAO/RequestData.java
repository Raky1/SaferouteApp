package me.saferoute.saferouteapp.DAO;

import android.os.AsyncTask;

import me.saferoute.saferouteapp.DAO.DataBase;

public class RequestData extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... urls) {
        return DataBase.postDados(urls[0], urls[1]);
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
