package ilia.liveplaces;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ilia.liveplaces.db.DBHelper;


public class TapeFragment extends Fragment {
    private static final String DEBUG_TAG = "TAPE";

    private ArrayList<String> urls;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tape,
                container, false);
        DBHelper db = new DBHelper(getContext());

        List<Place> allPlaces = db.getAllPlaces();
        Log.d(DEBUG_TAG, "saved place_id" + allPlaces.get(0).getName());


        urls = new ArrayList<>();
        if(allPlaces.size() == 0)
            return view;

        String [] allPlacesUrls = new String[allPlaces.size()];
        for ( int i = 0; i < allPlaces.size(); i++){
            String url ="https://api.instagram.com/v1/locations/"+ allPlaces.get(i).getInstId() +"/media/recent?client_id=2e51a52b26134652bc595449800d8387";
            allPlacesUrls[i] = url;
        }

        GetHTTP ge = new GetHTTP();
        //TODO ищет тока первый
        ge.execute(allPlacesUrls[0]);
        try {
            String response =ge.get();
            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray jsonMainArr = jsonObj.getJSONArray("data");

            if(jsonMainArr.length() == 0)
                Log.d(DEBUG_TAG, "empty");

            for (int i = 0; i < jsonMainArr.length(); i++) {
                JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
                JSONObject img = childJSONObject.getJSONObject("images");

                String img_url = img.getJSONObject("standard_resolution").getString("url");
                Log.d(DEBUG_TAG, img_url);
                urls.add(img_url);
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout);
        for (int i = 0; i < urls.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            Ion.with(this)
                    .load(urls.get(i))
                    .withBitmap()
                    .intoImageView(imageView);
            linearLayout.addView(imageView);
        }
    }
}

