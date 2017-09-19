package nl.devolksbank.nlhelpu.viewadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import nl.devolksbank.nlhelpu.R;
import nl.devolksbank.nlhelpu.model.DocumentCollectionItem;

public class DocumentCollectionViewAdapter extends ArrayAdapter<DocumentCollectionItem> {

    private final int layoutResource;

    public DocumentCollectionViewAdapter(Context context, int layoutResource, List<DocumentCollectionItem> documentCollectionItems) {
        super(context, layoutResource, documentCollectionItems);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        DocumentCollectionItem documentCollectionItem = getItem(position);

        if (documentCollectionItem != null) {
            TextView leftTextView = (TextView) view.findViewById(R.id.textViewLeft);
            TextView rightTextView = (TextView) view.findViewById(R.id.textViewRight);

            if (leftTextView != null) {
                leftTextView.setText(documentCollectionItem.getName());
            }

            if (rightTextView != null) {
                rightTextView.setText(documentCollectionItem.getStatus());
            }

            CheckBox box = (CheckBox) view.findViewById(R.id.checkBox);
            if (documentCollectionItem.isItemPresent()) {
                box.setChecked(true);
            } else {
                box.setChecked(false);
            }
        }

        return view;
    }


}