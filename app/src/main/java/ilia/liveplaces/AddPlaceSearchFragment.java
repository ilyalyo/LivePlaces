package ilia.liveplaces;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ilia.liveplaces.db.DBHelper;

public class AddPlaceSearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String DEBUG_TAG = "PLACE_STEP2";

    String tmp_url ="https://api.instagram.com/v1/locations/search?lat=48.858844&lng=2.294351&client_id=2e51a52b26134652bc595449800d8387";
    ListView lv;
    SearchView search_view;

    ArrayAdapter<String> adapter;
    private ArrayList<String> places;
    private ArrayList<Place> tmp_places;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_place_search,
                container, false);

        places = new ArrayList<>();
        tmp_places = new ArrayList<>();

        lv = (ListView) view.findViewById(R.id.list_view);
        search_view = (SearchView) view.findViewById(R.id.searchView);

        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.list_items, R.id.name, places);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Place pl = tmp_places.get(position);
                DBHelper mydb = new DBHelper(getContext());

                mydb.createPlace(pl);
            }
        });

        search_view.setOnQueryTextListener(this);

        GetHTTP ge = new GetHTTP();
        ge.execute(tmp_url);

        try {
            String response =ge.get();
            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

            JSONArray jsonMainArr = jsonObj.getJSONArray("data");
            Log.d(DEBUG_TAG, "REEEES");

            for (int i = 0; i < jsonMainArr.length(); i++) {
                JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
                String name = childJSONObject.getString("name");
                int id = childJSONObject.getInt("id");
                Log.d(DEBUG_TAG, name);

                places.add(name);
                tmp_places.add(new Place(id, name));
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);

        return false;
    }
}

