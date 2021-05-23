package aleksa.heler.smarthospital.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import aleksa.heler.smarthospital.Appointment;
import aleksa.heler.smarthospital.User;

public class AppointmentDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appointments.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "appointment";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TEXT = "Text";

    public AppointmentDBHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE user (ID TEXT, Name TEXT ...)
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TEXT + " TEXT);");

        /* TODO:
        *  Za sada je ovu tabelu baze potrebno popuniti ručno, kako bi se pri pokretanju aplikacije u listi nalazilo minimum 5 unosa
        *    insert(addElement(new Appointment("12.05.2021", "pregled 1"))
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(Appointment appointment){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, appointment.getId());
        values.put(COLUMN_DATE, appointment.getDate());
        values.put(COLUMN_TEXT, appointment.getText());

        db.insert(TABLE_NAME, null, values);
        close();
    }
}
