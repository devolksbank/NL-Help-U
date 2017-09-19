package nl.devolksbank.nlhelpu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import nl.devolksbank.nlhelpu.R;
import nl.devolksbank.nlhelpu.model.DocumentPart;
import nl.devolksbank.nlhelpu.model.FileType;
import nl.devolksbank.nlhelpu.util.DocumentPartsDataSource;

public class DocumentPartViewActivity extends AppCompatActivity {

    private int selectedSectionId = -1;
    private int selectedDocumentPartId = -1;
    private int selectedFileTypeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_part_view);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        selectedSectionId = intent.getIntExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
        selectedDocumentPartId = intent.getIntExtra(DocumentCollectionActivity.SELECTED_DOCUMENT_PART_ID, selectedDocumentPartId);
        selectedFileTypeId = intent.getIntExtra(DocumentCollectionActivity.SELECTED_FILE_TYPE_ID, selectedFileTypeId);
        Log.d("DocPartViewActivity", "selected sectionId: " + selectedSectionId);
        Log.d("DocPartViewActivity", "selected documentPartId: " + selectedDocumentPartId);
        Log.d("DocPartViewActivity", "selected fileTypeId: " + selectedFileTypeId);
        FileType fileType = FileType.fromInt(selectedFileTypeId);
        setLocalizedTitle(fileType);

        DocumentPartsDataSource documentPartsDataSource = new DocumentPartsDataSource(getApplicationContext());
        List<DocumentPart> documentParts = documentPartsDataSource.getDocumentPartsForFileType(fileType);

        DocumentPart documentPart = null;

        for (DocumentPart part : documentParts) {
            if (part.getId() == selectedDocumentPartId) {
                documentPart = part;
            }
        }

        if (null == documentPart) {
            Log.e("DocPartViewActivity", "Unable to find documentPart for ID: " + selectedDocumentPartId);
            return;
        }

        ImageView image = (ImageView) findViewById(R.id.imageViewDocumentPartDetails);
        File imgFile = new File(getFilesDir(), documentPart.getFileLocation());

        if (!imgFile.exists()) {
            Log.e("DocPartViewActivity", "Unable to show image");
            return;
        }

        Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        image.setImageBitmap(imageBitmap);
    }

    private void setLocalizedTitle(final FileType fileType) {
        if (-1 == fileType.getNameResourceId()) {
            setTitle(fileType.getDisplayName());
        } else {
            setTitle(getString(fileType.getNameResourceId()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Explicitly call the back pressed method to avoid a new create of the activity without the intent extra data
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d("DocPartViewActivity", "on-back-pressed");
        Intent intent = new Intent();
        intent.putExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
        intent.putExtra(DocumentCollectionActivity.SELECTED_FILE_TYPE_ID, selectedFileTypeId);
        setResult(1, intent);
        finish();
    }
}
