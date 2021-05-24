package aleksa.heler.smarthospital.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import aleksa.heler.smarthospital.classes.SmartDevice;

public class DeviceDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "devices.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "device";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_IMAGE = "Image";
    private static final String COLUMN_ACTIVE = "Active";

    public DeviceDBHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE user (ID TEXT, Name TEXT ...)
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_ACTIVE + " INTEGER);");

        /* TODO:
        * Za sada je ovu tabelu baze potrebno popuniti ručno, kako bi se pri pokretanju aplikacije u listi  nalazilo  minimum 10 unosa
        *   insert(new SmartDevice(getResources().getDrawable(R.drawable.blood_test), "description 1"))
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(SmartDevice device){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, device.getId());
        values.put(COLUMN_NAME, device.getName());
        values.put(COLUMN_IMAGE, device.getImage().toString());
        values.put(COLUMN_ACTIVE, device.isActive());

        db.insert(TABLE_NAME, null, values);
        close();
    }
}
