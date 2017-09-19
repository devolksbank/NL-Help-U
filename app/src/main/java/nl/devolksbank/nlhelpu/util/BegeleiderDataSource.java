package nl.devolksbank.nlhelpu.util;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BegeleiderDataSource {

    // Database fields
    private SQLiteDatabase database;
    private final MySQLiteHelper dbHelper;
    private final String[] allColumns = {
            MySQLiteHelper.BEGELEIDERTABLE_COLUMN_NAME,
            MySQLiteHelper.BEGELEIDERTABLE_COLUMN_EMAIL,
            MySQLiteHelper.BEGELEIDERTABLE_COLUMN_PHONE,
            MySQLiteHelper.BEGELEIDERTABLE_COLUMN_NOTES
    };

    private boolean isOpen = false;

    public BegeleiderDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        isOpen = true;
    }

    public void close() {
        dbHelper.close();
        isOpen = false;
    }

    private void ensureConnectionIsOpen() throws SQLException {
        if (!isOpen) {
            open();
        }
    }

    private void insertEmptyRecord() {
        ensureConnectionIsOpen();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.BEGELEIDERTABLE_COLUMN_NAME, "");
        values.put(MySQLiteHelper.BEGELEIDERTABLE_COLUMN_EMAIL, "");
        values.put(MySQLiteHelper.BEGELEIDERTABLE_COLUMN_PHONE, "");
        values.put(MySQLiteHelper.BEGELEIDERTABLE_COLUMN_NOTES, "");
        database.insert(MySQLiteHelper.TABLE_BEGELEIDER, null, values);
    }

    public String getBegeleiderName() {
        ensureConnectionIsOpen();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BEGELEIDER, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        if (0 == cursor.getCount()) {
            insertEmptyRecord();
            return "";
        }

        String output = cursor.getString(0);
        cursor.close();
        return output;
    }

    public String getBegeleiderEmail() {
        ensureConnectionIsOpen();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BEGELEIDER, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        if (0 == cursor.getCount()) {
            insertEmptyRecord();
            return "";
        }

        String output = cursor.getString(1);
        cursor.close();
        return output;
    }

    public String getBegeleiderPhone() {
        ensureConnectionIsOpen();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BEGELEIDER, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        if (0 == cursor.getCount()) {
            insertEmptyRecord();
            return "";
        }

        String output = cursor.getString(2);
        cursor.close();
        return output;
    }

    public String getBegeleiderNotes() {
        ensureConnectionIsOpen();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BEGELEIDER, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        if (0 == cursor.getCount()) {
            insertEmptyRecord();
            return "";
        }

        String output = cursor.getString(3);
        cursor.close();
        return output;
    }

    public void updateBegeleider(final String name, final String email, final String phone, final String notes) {
        Log.i("BegeleiderDataSource", "Updating begeleider info");
        ensureConnectionIsOpen();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.BEGELEIDERTABLE_COLUMN_NAME, name);
        values.put(MySQLiteHelper.BEGELEIDERTABLE_COLUMN_EMAIL, email);
        values.put(MySQLiteHelper.BEGELEIDERTABLE_COLUMN_PHONE, phone);
        values.put(MySQLiteHelper.BEGELEIDERTABLE_COLUMN_NOTES, notes);
        int numRowsAffected = database.update(MySQLiteHelper.TABLE_BEGELEIDER, values, null, null);
        Log.i("BegeleiderDataSource", "Number of updated records: " + numRowsAffected);
    }
}
