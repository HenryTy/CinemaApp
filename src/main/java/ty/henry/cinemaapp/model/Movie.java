package ty.henry.cinemaapp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Movie implements Comparable<Movie> {

    @SequenceGenerator(name = "SEQ_MOVIE", sequenceName = "SEQ_MOVIE", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOVIE")
    @Column(name = "movie_id")
    private Integer id;

    private String title;
    private Integer productionYear;
    private Integer lengthMinutes;

    @Enumerated(EnumType.STRING)
    private MovieGenre genre;

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

    public MovieGenre getGenre() {
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

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setAllowedFromAge(Integer allowedFromAge) {
        this.allowedFromAge = allowedFromAge;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(other == null || other.getClass() != getClass()) {
            return false;
        }
        Movie otherMovie = (Movie) other;
        return Objects.equals(title, otherMovie.title)
                && Objects.equals(productionYear, otherMovie.productionYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, productionYear);
    }

    @Override
    public int compareTo(Movie other) {
        int compareTitle = title.compareTo(other.title);
        return compareTitle == 0 ? productionYear.compareTo(other.productionYear) : compareTitle;
    }
}
