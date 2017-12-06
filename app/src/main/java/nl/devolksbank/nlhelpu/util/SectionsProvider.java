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
        output.add(new SectionModel(6, context.getString(getNameResourceId(6))));

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
                output.add(FileType.BANK_NOTE);
                output.add(FileType.RZA_HEALTH_INSURANCE_CARD);
                break;
            case 5:
                output.add(FileType.PASSPORT_PHOTO);
                break;
            case 6:
                output.add(FileType.BANK_NOTE);
                break;
            default:
                break;
        }
        return output;
    }
}
