package nl.devolksbank.nlhelpu.viewadapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import nl.devolksbank.nlhelpu.model.SectionModel;

public class SectionViewAdapter extends ArrayAdapter<SectionModel> {

    public SectionViewAdapter(Context context, int layoutResource, List<SectionModel> sectionModels) {
        super(context, layoutResource, sectionModels);
    }

    // TODO: localize the data

}
