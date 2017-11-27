package nl.devolksbank.nlhelpu.model;

import nl.devolksbank.nlhelpu.R;

public enum FileType {
    ID(1, "filetype-1"),
    ALIEN_DOCUMENT(2, "filetype-2"),
    RESIDENCE_PERMIT(3, "filetype-3"),
    ACTIVATION_LETTER_DIGID(4, "filetype-4"),
    HOUSE_RENTAL_CONTRACT(5, "filetype-5"),
    BANK_NOTE(6, "filetype-6"),
    PASSPORT_PHOTO(7, "filetype-7"),
    RZA_HEALTH_INSURANCE_CARD(8,"filetype-8");

    private final int id;
    private final String displayName;

    FileType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static FileType fromInt(final int id) {
        for (FileType value : values()) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }

    public int getNameResourceId() {
        switch (id) {
            case 1:
                return R.string.filetype_1;
            case 2:
                return R.string.filetype_2;
            case 3:
                return R.string.filetype_3;
            case 4:
                return R.string.filetype_4;
            case 5:
                return R.string.filetype_5;
            case 6:
                return R.string.filetype_6;
            case 7:
                return R.string.filetype_7;
            case 8:
                return R.string.filetype_8;
            default:
                break;
        }
        return -1;
    }
}
