    package ilia.liveplaces.db;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    import java.util.ArrayList;
    import java.util.List;

    import ilia.liveplaces.Place;

    public class DBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "DBHELPER";


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "livePlaceDB";

    // Table Names
    private static final String TABLE_PLACES = "places";

    // Common column names
    private static final String KEY_ID = "id";

    // PLACES Table - column names
    private static final String KEY_INST_ID = "inst_id";
    private static final String KEY_NAME = "name";

    // Table Create Statements
    private static final String CREATE_TABLE_PLACES = "CREATE TABLE " +
            TABLE_PLACES +
            "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_INST_ID + " INTEGER," +
            KEY_NAME + " TEXT" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_PLACES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /*
    * Creating a Place
    */
    public long createPlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INST_ID, place.getInstId());
        values.put(KEY_NAME, place.getName());

        // insert row
        return db.insert(TABLE_PLACES, null, values);
    }

    /*
    * get single Place by dbId
    */
    public Place getPlace(long place_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PLACES + " WHERE "
                + KEY_ID + " = " + place_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Place place = new Place();
        place.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        place.setInstId((c.getInt(c.getColumnIndex(KEY_INST_ID))));
        place.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        return place;
    }

    /*
    * getting all Places
    */
    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Place p = new Place();
                p.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                p.setInstId((c.getInt(c.getColumnIndex(KEY_INST_ID))));
                p.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                Log.d(DEBUG_TAG, "" + p.getId() + p.getName());
                places.add(p);
            } while (c.moveToNext());
        }

        return places;
    }

    /*
    * Deleting a Place
    */
    public void deletePlace(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
}