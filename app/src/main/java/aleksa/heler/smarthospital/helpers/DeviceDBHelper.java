package aleksa.heler.smarthospital.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import aleksa.heler.smarthospital.classes.SmartDevice;
import aleksa.heler.smarthospital.classes.User;

public class DeviceDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "devices.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "device";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_TYPE = "Type";
    private static final String COLUMN_ACTIVE = "Active";
    private static final String COLUMN_IMAGE = "Image";
    //(String id, String name, String type, String active, String image)

    public DeviceDBHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE user (ID TEXT, Name TEXT ...)
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_ACTIVE + " TEXT, " +
                COLUMN_IMAGE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(SmartDevice device){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, device.getId());
        values.put(COLUMN_NAME, device.getName());
        values.put(COLUMN_TYPE, device.getType());
        values.put(COLUMN_ACTIVE, device.getActive());
        values.put(COLUMN_IMAGE, device.getImage());

        db.insert(TABLE_NAME, null, values);
        close();
    }

    // Returns all devices from database
    public SmartDevice[] readDevices() {
        SQLiteDatabase db = getReadableDatabase();

        // Get data from database
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        if (cursor.getCount() <= 0) // If there is nothing found
            return null;


        // Go trough data and create class objects
        SmartDevice[] devices = new SmartDevice[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            devices[i++] = createDevice(cursor);
        }

        close();
        return devices;
    }

    // Returns devices from database by ID
    public SmartDevice readDevice(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + "=?", new String[]{id}, null, null, null);
        if (cursor.getCount() <= 0) // If there is nothing found
            return null;
        cursor.moveToFirst(); // In case there are more users with same ID
        SmartDevice device = createDevice(cursor);

        close();
        return device;
    }

    // Creates a user class object from given cursor
    private SmartDevice createDevice(Cursor cursor){
        String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
        String active = cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVE));
        String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
        return new SmartDevice(id, name, type, active, image);
    }

    // Deletes user with given ID and returns how many rows were deleted
    public int deleteDevice(String id){
        SQLiteDatabase db = getReadableDatabase();
        int delete_count = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        close();
        return  delete_count;
    }
}
