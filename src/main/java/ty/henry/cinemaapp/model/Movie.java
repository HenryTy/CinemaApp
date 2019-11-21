package ty.henry.cinemaapp.model;

import javax.persistence.*;

@Entity
public class Movie {

    @SequenceGenerator(name = "SEQ_MOVIE", sequenceName = "SEQ_MOVIE")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOVIE")
    private Integer id;

    private String title;
    private Integer productionYear;
    private Integer lengthMinutes;
    private String genre;
    private String director;
    private Integer allowedFromAge;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public Integer getLengthMinutes() {
        return lengthMinutes;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public Integer getAllowedFromAge() {
        return allowedFromAge;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public void setLengthMinutes(Integer lengthMinutes) {
        this.lengthMinutes = lengthMinutes;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setAllowedFromAge(Integer allowedFromAge) {
        this.allowedFromAge = allowedFromAge;
    }
}
