package ty.henry.cinemaapp.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class HallForm {

    @Size(min = 1, max = 50, message = "Nazwa musi zawierać od 1 do 50 znaków")
    private String name;

    @Min(value = 1, message = "Sala musi mieć co najmniej 1 rząd")
    @Max(value = 20, message = "Sala może mieć maksymalnie 20 rzędów")
    @NotNull(message = "Podaj liczbę rzędów")
    private Integer rowCount;

    @Min(value = 1, message = "Rząd musi mieć co najmniej 1 miejsce")
    @Max(value = 30, message = "Rząd może mieć maksymalnie 30 miejsc")
    @NotNull(message = "Podaj liczbę miejsc w rzędzie")
    private Integer seatsInRow;

    public String getName() {
        return name;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public Integer getSeatsInRow() {
        return seatsInRow;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public void setSeatsInRow(Integer seatsInRow) {
        this.seatsInRow = seatsInRow;
    }
}
