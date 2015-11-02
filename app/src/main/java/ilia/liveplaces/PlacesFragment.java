    package ilia.liveplaces;

    import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ilia.liveplaces.db.DBHelper;

public class PlacesFragment extends Fragment {
    private static final String DEBUG_TAG = "PLACE_STEP0";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places,
                container, false);

        DBHelper db = new DBHelper(getContext());

        List<Place> places = db.getAllPlaces();

        PlacesAdapter adapter = new PlacesAdapter(getActivity().getApplicationContext(),
                R.layout.list_items, places);

        ListView lv = (ListView) view.findViewById(R.id.list_view);
        lv.setAdapter(adapter);

        return view;
    }
}

