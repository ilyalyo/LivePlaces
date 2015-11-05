    package ilia.liveplaces;

    import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ilia.liveplaces.db.DBHelper;


public class PlacesAddAdapter extends ArrayAdapter<Place> implements Filterable {

    Context context;
    int layoutResourceId;
    List<Place>  pOriginalValues = null;
    List<Place>  places = null;
    ArrayList items = null;

    public PlacesAddAdapter(Context context, int layoutResourceId, List<Place> places) {
        super(context, layoutResourceId, places);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.places = places;
        items = new ArrayList();
    }
    private static final String DEBUG_TAG = "PlacesAddAdapter";

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlaceHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlaceHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.name);

            row.setTag(holder);
        }
        else
        {
            holder = (PlaceHolder)row.getTag();
        }

        Button btn = (Button) row.findViewById(R.id.add_places_btn);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(getContext());
                db.createPlace(places.get(position));
                db.closeDB();

                v.setBackgroundColor(Color.GREEN);
                items.add(position);
                Log.d(DEBUG_TAG, items.toString());
            }
        });

        if(items.contains(Integer.valueOf(position)))
            btn.setBackgroundColor(Color.GREEN);
        else
            btn.setBackgroundColor(Color.GRAY);

        Place p = places.get(position);
        holder.txtTitle.setText(p.getName());


        return row;
    }

    static class PlaceHolder
    {
        TextView txtTitle;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                places = (List<Place>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d(DEBUG_TAG, "start filt");

                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<Place> FilteredArrList = new ArrayList<>();

                if (pOriginalValues == null) {
                    pOriginalValues = new ArrayList<>(places); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = pOriginalValues.size();
                    results.values = pOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < pOriginalValues.size(); i++) {
                        Place data = pOriginalValues.get(i);
                        if (data.getName().toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                Log.d(DEBUG_TAG, results.values.toString());

                return results;
            }
        };
        return filter;
    }

}