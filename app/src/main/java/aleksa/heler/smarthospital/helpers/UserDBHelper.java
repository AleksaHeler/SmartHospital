package aleksa.heler.smarthospital.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    // Insert given user to database
    public void insert(User user) {
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

    // Returns all users from database
    public User[] readUsers() {
        SQLiteDatabase db = getReadableDatabase();

        // Get data from database
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        if (cursor.getCount() <= 0) // If there is nothing found
            return null;


        // Go trough data and create class objects
        User[] users = new User[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            users[i++] = createUser(cursor);
        }

        close();
        return users;
    }

    // Returns user from database by ID
    public User readUser(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + "=?", new String[]{id}, null, null, null);
        if (cursor.getCount() <= 0) // If there is nothing found
            return null;
        cursor.moveToFirst(); // In case there are more users with same ID
        User user = createUser(cursor);

        close();
        return user;
    }

    // Creates a user class object from given cursor
    private User createUser(Cursor cursor){
        String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String surname = cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME));
        String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
        String dateOfBirth = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_OF_BIRTH));
        String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        return new User(id, name, surname, gender, dateOfBirth, password);
    }

    // Deletes user with given ID and returns how many rows were deleted
    public int deleteUser(String id){
        SQLiteDatabase db = getReadableDatabase();
        int delete_count = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        close();
        return  delete_count;
    }
}
