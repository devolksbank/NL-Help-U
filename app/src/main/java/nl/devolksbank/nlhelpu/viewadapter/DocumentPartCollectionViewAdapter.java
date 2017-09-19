package nl.devolksbank.nlhelpu.viewadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import nl.devolksbank.nlhelpu.R;
import nl.devolksbank.nlhelpu.activity.DocumentActivity;
import nl.devolksbank.nlhelpu.activity.DocumentCollectionActivity;
import nl.devolksbank.nlhelpu.activity.DocumentPartViewActivity;
import nl.devolksbank.nlhelpu.activity.MainActivity;
import nl.devolksbank.nlhelpu.model.DocumentPart;
import nl.devolksbank.nlhelpu.util.DocumentPartsDataSource;

public class DocumentPartCollectionViewAdapter extends ArrayAdapter<DocumentPart> {

    private final int layoutResource;
    private final DocumentActivity documentActivity;
    private final int selectedSectionId;
    private final int selectedFileTypeId;

    public DocumentPartCollectionViewAdapter(Context context, int layoutResource, List<DocumentPart> documentParts, DocumentActivity documentActivity, int selectedSectionId, int selectedFileTypeId) {
        super(context, layoutResource, documentParts);
        this.layoutResource = layoutResource;
        this.documentActivity = documentActivity;
        this.selectedSectionId = selectedSectionId;
        this.selectedFileTypeId = selectedFileTypeId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("DocPartColViewAdapter", "Fetching view from document-part-collection-view-adapter (position=" + position + ")");

        View view = convertView;

        if (null == view) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        final View thisView = view;

        final DocumentPart documentPart = getItem(position);

        if (null != documentPart) {
            Log.d("DocPartColViewAdapter", "Document-part: " + documentPart);
            Button deleteDocumentPartButton = (Button) view.findViewById(R.id.buttonDeleteDocumentPart);

            if (null != deleteDocumentPartButton) {
                deleteDocumentPartButton.setText(view.getResources().getString(R.string.button_delete_document_page));
                deleteDocumentPartButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.d("DocPartColViewAdapter", "Deleting document part: " + documentPart);

                        Snackbar snackbar = Snackbar
                                .make(thisView, R.string.undo_document_part_delete_button_details, Snackbar.LENGTH_LONG)
                                .setAction(R.string.undo_document_part_delete_button, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("DocPartColViewAdapter", "Restoring deleted document part: " + documentPart);
                                        DocumentPartsDataSource dataSource = new DocumentPartsDataSource(thisView.getContext());
                                        dataSource.createDocument(documentPart.getType(), documentPart.getFileLocation());
                                        documentActivity.refreshDocumentPartList();
                                        Log.d("DocPartColViewAdapter", "Document part restored: " + documentPart);
                                    }
                                });

                        snackbar.show();

                        // Delete the document part
                        DocumentPartsDataSource dataSource = new DocumentPartsDataSource(thisView.getContext());
                        dataSource.deleteDocumentPart(documentPart);
                        documentActivity.refreshDocumentPartList();
                    }
                });
            }

            Button cropDocumentPartButton = (Button) view.findViewById(R.id.buttonCropDocumentPart);

            if (null != cropDocumentPartButton) {
                cropDocumentPartButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.d("DocPartColViewAdapter", "Cropping document part: " + documentPart);

                        UCrop.Options opts = new UCrop.Options();
                        opts.setToolbarTitle(getContext().getString(R.string.button_crop_document_page));
                        opts.setCompressionQuality(100);
                        opts.setFreeStyleCropEnabled(true);

                        opts.setActiveWidgetColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                        opts.setToolbarColor(getContext().getResources().getColor(R.color.colorPrimary));
                        opts.setToolbarWidgetColor(getContext().getResources().getColor(R.color.colorPrimaryLight));
                        opts.setStatusBarColor(getContext().getResources().getColor(R.color.colorPrimary));

                        Uri source = Uri.fromFile(new File(getContext().getFilesDir(), documentPart.getFileLocation()));
                        Uri destination = Uri.fromFile(new File(getContext().getFilesDir(), documentPart.getFileLocation()));
                        UCrop.of(source, destination)
                                .withAspectRatio(16, 9)
                                .withOptions(opts)
                                .start(documentActivity);
                    }
                });
            }

            ImageView image = (ImageView) view.findViewById(R.id.imageViewDocumentPart);
            File imgFile = DocumentActivity.getThumbnailPathForPicture(getContext().getFilesDir(), documentPart.getFileLocation());

            if (!imgFile.exists()) {
                Log.e("DocPartColViewAdapter", "Unable to show file from location: " + documentPart.getFileLocation());

                // Checking original location
                imgFile = new File(documentPart.getFileLocation());
                if (!imgFile.exists()) {
                    Log.e("DocPartColViewAdapter", "Unable to show file from location: " + documentPart.getFileLocation());
                    return view;
                } else {
                    Log.d("DocPartColViewAdapter", "Using original file: " + documentPart.getFileLocation());
                }
            } else {
                Log.d("DocPartColViewAdapter", "Using thumbnail");
            }

            Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            image.setImageBitmap(imageBitmap);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(documentActivity, DocumentPartViewActivity.class);

                    Log.e("DocPartColViewAdapter", "Opening documentPart: " + documentPart);
                    intent.putExtra(DocumentCollectionActivity.SELECTED_DOCUMENT_PART_ID, (int) documentPart.getId());
                    intent.putExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
                    intent.putExtra(DocumentCollectionActivity.SELECTED_FILE_TYPE_ID, selectedFileTypeId);

                    documentActivity.startActivity(intent);
                }
            });

        }
        return view;
    }
}