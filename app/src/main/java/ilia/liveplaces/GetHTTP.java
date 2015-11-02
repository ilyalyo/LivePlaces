package ilia.liveplaces;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

public class GetHTTP extends AsyncTask<String, String, String> {
    OkHttpClient client = new OkHttpClient();

    @Override
    protected String doInBackground(String... params) {
        Request request = new Request.Builder()
                .url(params[0])
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert response != null;
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}