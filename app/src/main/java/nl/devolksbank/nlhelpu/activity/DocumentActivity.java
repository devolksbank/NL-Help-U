package nl.devolksbank.nlhelpu.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nl.devolksbank.nlhelpu.R;
import nl.devolksbank.nlhelpu.model.DocumentPart;
import nl.devolksbank.nlhelpu.model.FileType;
import nl.devolksbank.nlhelpu.util.DocumentPartsDataSource;
import nl.devolksbank.nlhelpu.viewadapter.DocumentPartCollectionViewAdapter;

public class DocumentActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private String mCurrentPhotoPath;
    private String mCurrentPhotoFilename;

    private DocumentPartsDataSource documentPartsDataSource;

    private int selectedSectionId = -1;
    private int selectedFileTypeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DocumentActivity", "on-create");
        setContentView(R.layout.activity_document);

        Intent intent = getIntent();
        selectedSectionId = intent.getIntExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
        selectedFileTypeId = intent.getIntExtra(DocumentCollectionActivity.SELECTED_FILE_TYPE_ID, selectedFileTypeId);

        Log.i("DocumentActivity", "oncreate (section=" + selectedSectionId + ", fileType=" + selectedFileTypeId + ")");

        final FileType selectedFileType = FileType.fromInt(selectedFileTypeId);

        setLocalizedTitle(selectedFileType);

        // TODO: set the text of the header to a specific text for the selected filetype

        // Fetch the document parts which are available for this file type
        documentPartsDataSource = new DocumentPartsDataSource(this);
        refreshDocumentPartList();

        final Button button = (Button) findViewById(R.id.buttonNewDocument);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePictureForNewDocument();
            }
        });
    }

    public void refreshDocumentPartList() {
        documentPartsDataSource.open();

        List<DocumentPart> documentParts = documentPartsDataSource.getDocumentPartsForFileType(FileType.fromInt(selectedFileTypeId));
        Log.d("DocumentActivity", "Number of document parts found: " + documentParts.size());

        ListView listView = (ListView) findViewById(R.id.documentPartlist);
        DocumentPartCollectionViewAdapter listViewAdapter = new DocumentPartCollectionViewAdapter(this, R.layout.fragment_document_part, documentParts, this, selectedSectionId, selectedFileTypeId);
        listView.setAdapter(listViewAdapter);
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
    protected void onResume() {
        Log.i("DocumentActivity", "onresume");
        documentPartsDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("DocumentActivity", "onpause");
        documentPartsDataSource.close();
        super.onPause();
    }

    private void takePictureForNewDocument() {
        Log.i("DocumentActivity", "Taking new picture for document");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.i("DocumentActivity", "No permission present yet");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Log.i("DocumentActivity", "Presenting message regarding missing permissions");
                Toast.makeText(getApplicationContext(), getString(R.string.camera_permissions_error), Toast.LENGTH_LONG).show();
            } else {
                Log.i("DocumentActivity", "Requesting permissions");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        } else {
            Log.i("DocumentActivity", "Permissions granted, taking picture");
            takePictureIntent();
        }
    }

    private void takePictureIntent() {
        Log.i("DocumentActivity", "Taking picture");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (null != takePictureIntent.resolveActivity(getPackageManager())) {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("DocumentActivity", "On request permissions result");
        switch (requestCode) {
            case 101:
                Log.i("DocumentActivity", "Processing result of requested permissions");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    Log.i("DocumentActivity", "Permissions asked and granted");
                    takePictureIntent();
                } else {
                    //not granted
                    Log.i("DocumentActivity", "Permissions asked but NOT granted");
                    Toast.makeText(getApplicationContext(), getString(R.string.camera_permissions_error), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Log.i("DocumentActivity", "Processing default case");
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    static private final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Log.i("DocumentActivity", "Dispatching take picture intent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        PackageManager pm = getApplicationContext().getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Log.i("DocumentActivity", "Creating the file where the picture should go");

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("DocumentActivity", "Unable to create file location to be stored:" + ex.getLocalizedMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.i("DocumentActivity", "File created succesfully");

                if (doUseImageDataFromIntent()) {
                    Log.i("DocumentActivity", "Using Uri.fromFile()");
                } else {
                    Log.i("DocumentActivity", "Using FileProvider");
                    Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".util.GenericFileProvider", photoFile);
                    Log.i("DocumentActivity", "File stored");
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
                Log.i("DocumentActivity", "Starting take picture intent");
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                Log.i("DocumentActivity", "Done with take picture intent");
            }
        } else {
            Log.i("DocumentActivity", "No camera app available?!");
        }
        Log.i("DocumentActivity", "Done dispatching taking picture intent");
    }

    @Override
    public void onBackPressed() {
        Log.d("DocumentActivity", "on-back-pressed");
        Intent intent = new Intent();
        intent.putExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
        intent.putExtra(DocumentCollectionActivity.SELECTED_FILE_TYPE_ID, selectedFileTypeId);
        setResult(1, intent);
        finish();
    }

    private void resizePictureToThumbnail(final String originalFileLocation) {
        Log.i("DocumentActivity", "Creating thumbnail");
        // Resize the picture for display in list
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(originalFileLocation, options);

        double MAX_WIDTH = 500;
        double currentWidth = bitmap.getWidth();
        double scale = currentWidth / MAX_WIDTH;
        double newWidth = MAX_WIDTH;
        double currentHeight = bitmap.getHeight();
        double newHeight = currentHeight / scale;
        Log.d("DocumentActivity", "Scaling [" + currentWidth + "/" + currentHeight + "] to [" + newWidth + "/" + newHeight + "]");
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) newWidth, (int) newHeight, true);

        File thumbnail = getThumbnailPathForPicture(getFilesDir(), originalFileLocation);

        try {
            FileOutputStream out = new FileOutputStream(thumbnail);
            resized.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("DocumentActivity", "Unable to save thumbnail: " + e.getLocalizedMessage());
        }
        Log.i("DocumentActivity", "Done creating thumbnail");
    }

    private boolean doUseImageDataFromIntent() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Log.i("DocumentActivity", "Do not use image data directly from the intent");
            return false;
        } else {
            Log.i("DocumentActivity", "Use image data directly from the intent");
            // In Android4.4 and lower, the usage of a file path for taking pictures does not work properly
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("DocumentActivity", "On activity result");
        Log.i("DocumentActivity", "RequestCode[" + requestCode + "], resultCode[" + resultCode + "], hasIntentData[" + ((null != data) ? "yes" : "no") + "]");
        if (REQUEST_IMAGE_CAPTURE == requestCode && RESULT_OK == resultCode) {
            Log.i("DocumentActivity", "Result OK from image capture");

            if (doUseImageDataFromIntent()) {
                Log.i("DocumentActivity", "Fetching the data from the intent");
                // Fetch the data from the intent and write it to the image location
                Bundle extras = data.getExtras();

                Log.i("DocumentActivity", "Fetch the data");
                // Get the returned image from extra
                Bitmap bmp = (Bitmap) extras.get("data");

                try {
                    Log.i("DocumentActivity", "Writing to output stream");
                    FileOutputStream out = new FileOutputStream(mCurrentPhotoPath);

                    Log.i("DocumentActivity", "Compressing output");
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Log.i("DocumentActivity", "Done writing to stream");
                } catch (Exception e) {
                    Log.e("DocumentActivity", "Unable to save image: " + e.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(), getString(R.string.camera_general_error), Toast.LENGTH_LONG).show();
                    return;
                }
            }

            resizePictureToThumbnail(mCurrentPhotoPath);

            Log.i("DocumentActivity", "Saving to disk");
            documentPartsDataSource.ensureConnectionIsOpen();
            documentPartsDataSource.createDocument(FileType.fromInt(selectedFileTypeId), mCurrentPhotoFilename);
            Log.i("DocumentActivity", "Done saving picture to datasource");
            refreshDocumentPartList();
        } else if (RESULT_OK == resultCode && UCrop.REQUEST_CROP == requestCode) {
            Log.i("DocumentActivity", "Result OK from cropping");
            final Uri resultUri = UCrop.getOutput(data);

            resizePictureToThumbnail(resultUri.getPath());

            Log.i("DocumentActivity", "Output: " + resultUri);
            refreshDocumentPartList();
        } else if (UCrop.RESULT_ERROR == resultCode) {
            Log.i("DocumentActivity", "Result cropping NOK");
            final Throwable cropError = UCrop.getError(data);
            // TODO: log/present-error
            Log.i("DocumentActivity", "Error: " + cropError.getLocalizedMessage());
        } else {
            Log.i("DocumentActivity", "Result NOK: (resultCode=" + resultCode + ", requestCode=" + requestCode + ")");
        }
        Log.i("DocumentActivity", "Done with on activity result");
    }

    /**
     * Method for creating a new image location
     * @return A File pointing to the new image location
     * @throws IOException in case the file could not be created
     */
    private File createImageFile() throws IOException {
        Log.i("DocumentActivity", "Creating image file");

        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "NL_HELP_U_JPEG_" + timeStamp + ".jpg";
        File imagePath = new File(getFilesDir(), "");
        File image = new File(imagePath, imageFileName);

        mCurrentPhotoPath = image.getAbsolutePath();
        mCurrentPhotoFilename = imageFileName;
        return image;
    }

    public static File getThumbnailPathForPicture(final File filesDirectory, final String originalFilename) {
        String[] pathParts = originalFilename.split("/");
        String originalFilenameWithoutExtension = pathParts[pathParts.length - 1].split(".jpg")[0];
        String thumbnailFilename = originalFilenameWithoutExtension + "_thumb.jpg";
        return new File(filesDirectory, thumbnailFilename);
    }

}
