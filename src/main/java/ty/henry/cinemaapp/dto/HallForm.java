package ty.henry.cinemaapp.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class HallForm {

    @Size(min = 1, max = 50, message = "Nazwa musi zawierać od 1 do 50 znaków")
    private String name;

    @Min(value = 1, message = "Sala musi mieć co najmniej 1 rząd")
    @Max(value = 99, message = "Sala może mieć maksymalnie 99 rzędów")
    private Integer rowCount;

    @Min(value = 1, message = "Rząd musi mieć co najmniej 1 miejsce")
    @Max(value = 99, message = "Rząd może mieć maksymalnie 99 miejsc")
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
