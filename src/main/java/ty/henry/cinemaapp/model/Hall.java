package ty.henry.cinemaapp.model;

import javax.persistence.*;

@Entity
public class Hall {

    @SequenceGenerator(name = "SEQ_HALL", sequenceName = "SEQ_HALL")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HALL")
    private Integer id;

    private String name;
    private Integer rowCount;
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
