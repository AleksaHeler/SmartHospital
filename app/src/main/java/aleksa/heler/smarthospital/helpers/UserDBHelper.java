package aleksa.heler.smarthospital.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import aleksa.heler.smarthospital.classes.User;

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_SURNAME = "Surname";
    private static final String COLUMN_GENDER = "Gender";
    private static final String COLUMN_DATE_OF_BIRTH = "Date of birth";
    private static final String COLUMN_PASSWORD = "Password";

    public UserDBHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE user (ID TEXT, Name TEXT ...)
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SURNAME + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_DATE_OF_BIRTH + " TEXT, " +
                COLUMN_PASSWORD + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(User user){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_SURNAME, user.getSurname());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_DATE_OF_BIRTH, user.getDateOfBirth());
        values.put(COLUMN_PASSWORD, user.getPassword());

        db.insert(TABLE_NAME, null, values);
        close();
    }
}
