package nl.devolksbank.nlhelpu.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class MySQLiteHelper extends SQLiteOpenHelper {

    // Document(parts) table
    public static final String TABLE_DOCUMENTS = "documents";
    public static final String DOCTABLE_COLUMN_ID = "id";
    public static final String DOCTABLE_COLUMN_FILE_LOCATION = "file_location";
    public static final String DOCTABLE_COLUMN_FILE_TYPE = "type";

    // Begeleider table (1 record actually)
    public static final String TABLE_BEGELEIDER = "begeleider";
    public static final String BEGELEIDERTABLE_COLUMN_NAME = "name";
    public static final String BEGELEIDERTABLE_COLUMN_EMAIL = "email";
    public static final String BEGELEIDERTABLE_COLUMN_PHONE = "phone";
    public static final String BEGELEIDERTABLE_COLUMN_NOTES = "notes";

    private static final String DATABASE_NAME = "documents-v4.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DOCDATABASE_CREATE = "" +
            "create table " + TABLE_DOCUMENTS + "( "
            + DOCTABLE_COLUMN_ID + " integer primary key autoincrement, "
            + DOCTABLE_COLUMN_FILE_LOCATION + " text not null, "
            + DOCTABLE_COLUMN_FILE_TYPE + " integer not null "
            + ")";

    private static final String BEGELEIDERDATABASE_CREATE = "" +
            "create table " + TABLE_BEGELEIDER + "( "
            + BEGELEIDERTABLE_COLUMN_NAME + " text null, "
            + BEGELEIDERTABLE_COLUMN_EMAIL + " text null, "
            + BEGELEIDERTABLE_COLUMN_PHONE + " text null, "
            + BEGELEIDERTABLE_COLUMN_NOTES + " text null "
            + ")";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.w("MySQLiteHelper", "Creating databases");
        database.execSQL(DOCDATABASE_CREATE);
        database.execSQL(BEGELEIDERDATABASE_CREATE);
        Log.w("MySQLiteHelper", "Databases created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("MySQLiteHelper", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("drop table if exists " + TABLE_DOCUMENTS);
        db.execSQL("drop table if exists " + TABLE_BEGELEIDER);
        this.onCreate(db);
    }

}
