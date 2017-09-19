package nl.devolksbank.nlhelpu.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nl.devolksbank.nlhelpu.model.DocumentPart;
import nl.devolksbank.nlhelpu.model.FileType;
import nl.devolksbank.nlhelpu.model.SectionModel;

public class DocumentPartsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private final MySQLiteHelper dbHelper;
    private final String[] allColumns = { MySQLiteHelper.DOCTABLE_COLUMN_ID, MySQLiteHelper.DOCTABLE_COLUMN_FILE_LOCATION, MySQLiteHelper.DOCTABLE_COLUMN_FILE_TYPE};

    private boolean isOpen = false;

    public DocumentPartsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        isOpen = true;
    }

    public void close() {
        dbHelper.close();
        isOpen = false;
    }

    public void ensureConnectionIsOpen() throws SQLException {
        if (!isOpen) {
            open();
        }
    }

    public void createDocument(FileType type, String fileLocation) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.DOCTABLE_COLUMN_FILE_LOCATION, fileLocation);
        values.put(MySQLiteHelper.DOCTABLE_COLUMN_FILE_TYPE, Integer.toString(type.getId()));
        ensureConnectionIsOpen();
        long insertId = database.insert(MySQLiteHelper.TABLE_DOCUMENTS, null, values);
        ensureConnectionIsOpen();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOCUMENTS, allColumns, MySQLiteHelper.DOCTABLE_COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public void deleteDocumentPart(DocumentPart documentPart) {
        long id = documentPart.getId();
        Log.i("DocPartsDataSource", "Documentpart deleted with id: " + id);
        ensureConnectionIsOpen();
        database.delete(MySQLiteHelper.TABLE_DOCUMENTS, MySQLiteHelper.DOCTABLE_COLUMN_ID + " = " + id, null);
    }

    private List<DocumentPart> getAllDocumentParts() {
        Log.i("DocPartsDataSource", "Get all document parts");
        List<DocumentPart> documentParts = new ArrayList<>();

        ensureConnectionIsOpen();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOCUMENTS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DocumentPart documentPart = cursorToDocument(cursor);
            documentParts.add(documentPart);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return documentParts;
    }

    public List<DocumentPart> getDocumentPartsForSection(final SectionModel sectionModel) {
        Log.i("DocPartsDataSource", "Get document parts for section: " + sectionModel);
        List<DocumentPart> output = new ArrayList<>();

        for (DocumentPart part : getAllDocumentParts()) {
            for (FileType sectionFileType : SectionsProvider.getFileTypesForSectionModel(sectionModel)) {
                if (sectionFileType.equals(part.getType())) {
                    output.add(part);
                }
            }
        }

        return output;
    }

    public List<DocumentPart> getDocumentPartsForFileType(final FileType fileType) {
        Log.i("DocPartsDataSource", "Get document parts for file type: " + fileType);
        List<DocumentPart> output = new ArrayList<>();

        for (DocumentPart part : getAllDocumentParts()) {
            if (fileType == part.getType()) {
                output.add(part);
            }
        }

        return output;
    }

    private DocumentPart cursorToDocument(Cursor cursor) {
        return new DocumentPart(cursor.getLong(0), cursor.getString(1), FileType.fromInt(cursor.getInt(2)));
    }
}