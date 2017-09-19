package nl.devolksbank.nlhelpu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import nl.devolksbank.nlhelpu.R;
import nl.devolksbank.nlhelpu.model.DocumentCollectionItem;
import nl.devolksbank.nlhelpu.model.DocumentPart;
import nl.devolksbank.nlhelpu.model.FileType;
import nl.devolksbank.nlhelpu.model.SectionModel;
import nl.devolksbank.nlhelpu.util.BegeleiderDataSource;
import nl.devolksbank.nlhelpu.util.DocumentPartsDataSource;
import nl.devolksbank.nlhelpu.util.SectionsProvider;
import nl.devolksbank.nlhelpu.viewadapter.DocumentCollectionViewAdapter;

public class DocumentCollectionActivity extends AppCompatActivity {

    public static final String SELECTED_FILE_TYPE_ID = "nl.devolksbank.nlhelpu.SELECTED_FILE_TYPE_ID";
    public static final String SELECTED_DOCUMENT_PART_ID = "nl.devolksbank.nlhelpu.SELECTED_DOCUMENT_PART_ID";

    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 234;

    private ListView listView;

    private DocumentPartsDataSource documentPartsDataSource;

    private int selectedSectionId = -1;

    private SectionModel sectionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DocCollectionActivity", "on-create");
        setContentView(R.layout.activity_document_collection);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        selectedSectionId = intent.getIntExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
        Log.d("DocCollectionActivity", "selected sectionId: " + selectedSectionId);

        SectionsProvider sectionsProvider = new SectionsProvider();
        sectionModel = sectionsProvider.getSectionById(getApplicationContext(), selectedSectionId);

        setLocalizedTitle(sectionModel);
        setLocalizedHeader(sectionModel);

        documentPartsDataSource = new DocumentPartsDataSource(this);
        reloadItemsList();

        final DocumentCollectionActivity documentCollectionActivity = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DocCollectionActivity", "document clicked");

                // ListView Clicked item value
                DocumentCollectionItem itemValue = (DocumentCollectionItem) listView.getItemAtPosition(position);

                // Show Alert
                Log.i("DocCollectionActivity", "Document position : " + position + ", ListItem : " + itemValue);

                Intent intent = new Intent(documentCollectionActivity, DocumentActivity.class);

                intent.putExtra(SELECTED_FILE_TYPE_ID, itemValue.getType().getId());
                intent.putExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
                startActivity(intent);
            }
        });

        final Button button = (Button) findViewById(R.id.mailDocumentsButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendDocumentsViaMail();
            }
        });

        final ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DocCollectionActivity", "Starting section detail");
                Intent intent = new Intent(documentCollectionActivity, SectionInfo.class);
                intent.putExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
                startActivity(intent);
            }
        });
    }

    private void setLocalizedTitle(final SectionModel sectionModel) {
        if (-1 != sectionModel.getNameResourceId()) {
            setTitle(sectionModel.getNameResourceId());
        } else {
            setTitle(sectionModel.getName());
        }
    }

    private void setLocalizedHeader(final SectionModel sectionModel) {
        TextView headerText = (TextView) findViewById(R.id.txtDocumentCollection);
        if (-1 != sectionModel.getContentResourceId()) {
            headerText.setText(sectionModel.getContentResourceId());
        } else {
            headerText.setText(sectionModel.getName());
        }
    }

    private void reloadItemsList() {
        documentPartsDataSource.ensureConnectionIsOpen();

        List<DocumentCollectionItem> documentCollectionItems = new ArrayList<>();

        Log.d("DocCollectionActivity", "Checking filetypes to add");
        for (FileType fileType : SectionsProvider.getFileTypesForSectionModel(sectionModel)) {
            Log.d("DocCollectionActivity", "Checking filetype: " + fileType);
            // TODO: use some sort of map instead
            boolean isPresent = false;
            for (DocumentPart documentPart : documentPartsDataSource.getDocumentPartsForSection(sectionModel)) {
                if (documentPart.getType().equals(fileType)) {
                    DocumentCollectionItem docData = new DocumentCollectionItem(getString(fileType.getNameResourceId()), getString(R.string.document_collection_list_item_present_title), fileType, true);
                    documentCollectionItems.add(docData);
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent) {
                Log.d("DocCollectionActivity", "File type not found in documentparts, add as TO-DO");
                DocumentCollectionItem docData = new DocumentCollectionItem(getString(fileType.getNameResourceId()), getString(R.string.document_collection_list_item_not_present_title), fileType, false);
                documentCollectionItems.add(docData);
            }
        }

        listView = (ListView) findViewById(R.id.documentlist);
        DocumentCollectionViewAdapter listViewAdapter = new DocumentCollectionViewAdapter(this, R.layout.fragment_document, documentCollectionItems);
        listView.setAdapter(listViewAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DocCollectionActivity", "on-activity-result");
        if (requestCode == 1) {
            Log.d("DocCollectionActivity", "requestcode=1");
            if (resultCode == RESULT_OK) {
                Log.d("DocCollectionActivity", "result-ok");
            }
        }
    }

    @Override
    protected void onResume() {
        Log.d("DocCollectionActivity", "on-resume");
        reloadItemsList();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("DocCollectionActivity", "on-pause");
        documentPartsDataSource.close();
        super.onPause();
    }

    private void sendDocumentsViaMail() {
        Log.i("DocCollectionActivity", "Sending documents via mail");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("DocCollectionActivity", "There is no granted permission for external storage yet");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.i("DocCollectionActivity", "Show explanation");

                Toast.makeText(getApplicationContext(), getString(R.string.mail_permissions_error), Toast.LENGTH_LONG).show();

            } else {
                Log.i("DocCollectionActivity", "No need to show an explanation, just request the permission from the user now");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
            }
        } else {
            Log.i("DocCollectionActivity", "The permission for external storage has already been granted, send the mail");
            sendMail();
        }
    }

    // Improvement: move this method to a utilities class
    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try
        {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
        finally
        {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    private void sendMail() {
        Log.d("DocCollectionActivity", "Sending mail");
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/html");

        BegeleiderDataSource dataSource = new BegeleiderDataSource(getApplicationContext());
        StringBuilder toEmail = new StringBuilder();
        toEmail.append(dataSource.getBegeleiderName()).append("<").append(dataSource.getBegeleiderEmail()).append(">");
        if (!toEmail.toString().isEmpty()) {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{toEmail.toString()});
        }
        // TODO: use section name
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));

        ArrayList<Uri> uris = new ArrayList<>();

        Log.d("DocCollectionActivity", "Adding attachments");
        for (DocumentPart documentPart : documentPartsDataSource.getDocumentPartsForSection(sectionModel)) {
            try {
                // First copy the file to external storage as otherwise it is not accessible for other applications handling the intent
                File thisFile = new File(getFilesDir(), documentPart.getFileLocation());
                File thisFileAttachment = new File(Environment.getExternalStorageDirectory(), documentPart.getFileLocation());
                copyFile(thisFile, thisFileAttachment);
                Uri thisUri = Uri.fromFile(thisFileAttachment);
                uris.add(thisUri);
            } catch (IOException ex) {
                Log.e("DocCollectionActivity", "Unable to copy attachment to shared environment");
            }
        }

        if (!uris.isEmpty()) {
            Log.i("DocCollectionActivity", "Attachments found, adding them");
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }

        Log.d("DocCollectionActivity", "Starting email intent");
        startActivity(Intent.createChooser(emailIntent, getString(R.string.mail_chooser_hint)));
        Log.d("DocCollectionActivity", "Done starting intent");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        Log.i("DocCollectionActivity", "Processing result of permission request");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {
                Log.i("DocCollectionActivity", "Checking external storage request result");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("DocCollectionActivity", "The access was granted, send the mail");
                    sendMail();
                } else {
                    Log.i("DocCollectionActivity", "Access not granted");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), getString(R.string.mail_permissions_error), Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
            default:
                Log.w("DocCollectionActivity", "Unable to process permission request code: " + requestCode);
        }
    }
}
