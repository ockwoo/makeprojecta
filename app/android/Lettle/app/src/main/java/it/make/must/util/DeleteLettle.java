package it.make.must.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import it.make.must.lettle.MainActivity;
import it.make.must.model.Lettle;

/**
 * Created by EVOL on 2016-06-24.
 */
public class DeleteLettle extends AsyncTask<String, Void, Integer> {

    private Context mContext;
    private final String  TAG = "[Lettle]";

    public DeleteLettle(Context context){
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) {
        URL url;
        int responseConde = 0;

        try {
            url = new URL(params[0]);
            Log.d(TAG, "[DeleteLettle] Request URL : " + params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("DELETE");
            conn.setDoInput(true);
            conn.connect();

            responseConde = conn.getResponseCode();
            Log.d(TAG, "[DeleteLettle] Response Code : " + responseConde);
        }
        catch (Exception e) {
            Log.d(TAG , "[DeleteLettle] IOException - " + e.toString());
        }

        return responseConde;
    }

    @Override
    protected void onPostExecute(Integer result) {
        try {
            MainActivity mainActivity = (MainActivity) mContext;
            if(result != null && result == HttpURLConnection.HTTP_OK) {
                Toast.makeText(mContext, "삭제 하였습니다.", Toast.LENGTH_SHORT).show();
                mainActivity.getLettleList();
            }
        } catch (Exception e) {
            Log.d(TAG, "[GetLettle] Exception - " + e.toString());
            Toast.makeText(mContext, "서버와의 통신에서 알수 없는 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
        }

    }
}

