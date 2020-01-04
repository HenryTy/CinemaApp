package ty.henry.cinemaapp.model;

public enum MovieGenre {
    ACTION("Akcja"),
    ADVENTURE("Przygodowy"),
    ANIMATION("Animowany"),
    BIOGRAPHICAL("Biograficzny"),
    COMEDY("Komedia"),
    CRIME("Kryminalny"),
    DRAMA("Dramat"),
    FAMILY("Familijny"),
    FANTASY("Fantasy"),
    HORROR("Horror"),
    ROMANTIC_COMEDY("Komedia romantyczna"),
    SCI_FI("Sci-Fi"),
    SPORT("Sportowy"),
    THRILLER("Thriller");

    private String displayName;

    MovieGenre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
