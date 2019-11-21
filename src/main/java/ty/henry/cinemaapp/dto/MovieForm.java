package ty.henry.cinemaapp.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MovieForm {

    @Size(min = 1, max = 50, message = "Tytuł musi zawierać od 1 do 50 znaków")
    private String title;

    @Min(value = 1900, message = "Podaj rok z przedziału od 1900 do 2500")
    @Max(value = 2500, message = "Podaj rok z przedziału od 1900 do 2500")
    @NotNull(message = "Podaj rok produkcji")
    private Integer productionYear;

    @Min(value = 1, message = "Długość filmu musi wynosić co najmniej 1 minutę")
    @Max(value = 999, message = "Długość filmu musi wynosić mniej niż 1000 minut")
    @NotNull(message = "Podaj długość filmu")
    private Integer lengthMinutes;

    @Size(min = 1, max = 50, message = "Gatunek musi zawierać od 1 do 50 znaków")
    private String genre;

    @Size(max = 50, message = "Imię i nazwisko reżysera może zawierać maksymalnie 50 znaków")
    private String director;

    @Min(value = 1, message = "Podaj liczbę z przedziału od 1 do 99")
    @Max(value = 99, message = "Podaj liczbę z przedziału od 1 do 99")
    private Integer allowedFromAge;

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
