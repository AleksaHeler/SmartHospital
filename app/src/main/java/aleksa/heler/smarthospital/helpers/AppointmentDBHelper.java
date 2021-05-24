package aleksa.heler.smarthospital.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import aleksa.heler.smarthospital.classes.Appointment;
import aleksa.heler.smarthospital.classes.User;

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

    // Returns all appointments from database
    public Appointment[] readAppointments() {
        SQLiteDatabase db = getReadableDatabase();

        // Get data from database
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        if (cursor.getCount() <= 0) // If there is nothing found
            return null;


        // Go trough data and create class objects
        Appointment[] appointments = new Appointment[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            appointments[i++] = createAppointment(cursor);
        }

        close();
        return appointments;
    }

    // Returns all appointments with given ID from database
    public Appointment[] readAppointments(String id) {
        SQLiteDatabase db = getReadableDatabase();

        // Get data from database
        //Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + "=?", new String[]{id}, null, null, null);
        if (cursor.getCount() <= 0) // If there is nothing found
            return null;

        // Go trough data and create class objects
        Appointment[] appointments = new Appointment[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            appointments[i++] = createAppointment(cursor);
        }

        close();
        return appointments;
    }

    // Returns appointment from database by ID
    public Appointment readAppointment(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + "=?", new String[]{id}, null, null, null);
        if (cursor.getCount() <= 0) // If there is nothing found
            return null;
        cursor.moveToFirst(); // In case there are more users with same ID
        Appointment appointment = createAppointment(cursor);

        close();
        return appointment;
    }

    // Creates an appointment class object from given cursor
    private Appointment createAppointment(Cursor cursor){
        String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
        String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        String text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT));
        return new Appointment(id, date, text);
    }

    // Deletes appointments with given ID and returns how many rows were deleted
    public int deleteAppointment(String id){
        SQLiteDatabase db = getReadableDatabase();
        int delete_count = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        close();
        return  delete_count;
    }
}
