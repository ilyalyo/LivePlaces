    package ilia.liveplaces;

    import android.content.Context;
    import android.graphics.Color;
    import android.graphics.drawable.ColorDrawable;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.TextView;

    import java.util.ArrayList;
    import java.util.List;

    import ilia.liveplaces.db.DBHelper;


public class PlacesAddAdapter extends ArrayAdapter<Place> {

    Context context;
    int layoutResourceId;
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

}