package nl.devolksbank.nlhelpu.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nl.devolksbank.nlhelpu.R;
import nl.devolksbank.nlhelpu.model.FileType;
import nl.devolksbank.nlhelpu.model.SectionModel;

public class SectionsProvider {

    public List<SectionModel> listSections(Context context) {
        Log.d(this.getClass().getName(), "Fetching sections");
        List<SectionModel> output = new ArrayList<>();

        output.add(new SectionModel(1, context.getString(getNameResourceId(1))));
        output.add(new SectionModel(2, context.getString(getNameResourceId(2))));
        output.add(new SectionModel(3, context.getString(getNameResourceId(3))));
        output.add(new SectionModel(4, context.getString(getNameResourceId(4))));
        output.add(new SectionModel(5, context.getString(getNameResourceId(5))));
        // Disabled for now as no real content is available for it
//        output.add(new SectionModel(6, context.getString(getNameResourceId(6))));
//        output.add(new SectionModel(7, context.getString(getNameResourceId(7))));
//        output.add(new SectionModel(8, context.getString(getNameResourceId(8))));
//        output.add(new SectionModel(9, context.getString(getNameResourceId(9))));

        return output;
    }

    public static int getNameResourceId(final int sectionModelId) {
        switch (sectionModelId) {
            case 1:
                return R.string.section_1;
            case 2:
                return R.string.section_2;
            case 3:
                return R.string.section_3;
            case 4:
                return R.string.section_4;
            case 5:
                return R.string.section_5;
            case 6:
                return R.string.section_6;
            case 7:
                return R.string.section_7;
            case 8:
                return R.string.section_8;
            case 9:
                return R.string.section_9;
            default:
                return -1;
        }
    }

    public static int getContentResourceId(final int sectionModelId) {
        switch (sectionModelId) {
            case 1:
                return R.string.section_header_1;
            case 2:
                return R.string.section_header_2;
            case 3:
                return R.string.section_header_3;
            case 4:
                return R.string.section_header_4;
            case 5:
                return R.string.section_header_5;
            case 6:
                return R.string.section_header_6;
            case 7:
                return R.string.section_header_7;
            case 8:
                return R.string.section_header_8;
            case 9:
                return R.string.section_header_9;
            default:
                return -1;
        }
    }

    public static int getDetailsResourceId(final int sectionModelId) {
        switch (sectionModelId) {
            case 1:
                return R.array.section_details_1;
            case 2:
                return R.array.section_details_2;
            case 3:
                return R.array.section_details_3;
            case 4:
                return R.array.section_details_4;
            case 5:
                return R.array.section_details_5;
            case 6:
                return R.array.section_details_6;
            case 7:
                return R.array.section_details_7;
            case 8:
                return R.array.section_details_8;
            case 9:
                return R.array.section_details_9;
            default:
                return -1;
        }
    }

    public SectionModel getSectionById(Context context, final int sectionId) {
        for (SectionModel sectionModel : listSections(context)) {
            if (sectionModel.getId() == sectionId) {
                return sectionModel;
            }
        }
        return null;
    }

    public static List<FileType> getFileTypesForSectionModel(final SectionModel sectionModel) {
        List<FileType> output = new ArrayList<>();
        switch (sectionModel.getId()) {
            case 1:
                output.add(FileType.ACTIVATION_LETTER_DIGID);
                break;
            case 2:
                output.add(FileType.ID);
                output.add(FileType.RESIDENCE_PERMIT);
                output.add(FileType.HOUSE_RENTAL_CONTRACT);
                output.add(FileType.BANK_NOTE);
                break;
            case 3:
                output.add(FileType.ID);
                output.add(FileType.ALIEN_DOCUMENT);
                break;
            case 4:
                output.add(FileType.ID);
                output.add(FileType.ALIEN_DOCUMENT);
                break;
            case 5:
                output.add(FileType.PASSPORT_PHOTO);
            case 6:
            case 7:
            case 8:
            case 9:
                // TODO
            default:
                break;
        }
        return output;
    }
}
