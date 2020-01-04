package ty.henry.cinemaapp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Hall implements Comparable<Hall> {

    @SequenceGenerator(name = "SEQ_HALL", sequenceName = "SEQ_HALL", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HALL")
    @Column(name = "hall_id")
    private Integer id;

    private String name;
    private Integer rowCount;
    private Integer seatsInRow;

    public Integer getId() {
        return id;
    }

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

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(other == null || other.getClass() != getClass()) {
            return false;
        }
        Hall otherHall = (Hall) other;
        return Objects.equals(name, otherHall.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Hall other) {
        return name.compareTo(other.name);
    }
}
