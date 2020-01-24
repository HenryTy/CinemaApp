package ty.henry.cinemaapp.logic;

public enum MovieSortType {
    BY_SHOWING_TIME("Godzina seansu"),
    BY_TITLE("Tytuł filmu");

    private String displayName;

    MovieSortType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
