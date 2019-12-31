package ty.henry.cinemaapp.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReservationId implements Serializable {

    private int rowNumber;
    private int seatInRow;
    private String ticketNumber;

    public int getRowNumber() {
        return rowNumber;
    }

    public int getSeatInRow() {
        return seatInRow;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    @Override
    public boolean equals(Object otherObj) {
        if(this == otherObj) return true;
        if(getClass() != otherObj.getClass()) return false;
        ReservationId otherId = (ReservationId) otherObj;
        return rowNumber == otherId.getRowNumber() && seatInRow == otherId.getSeatInRow()
                && ticketNumber.equals(otherId.getTicketNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber, seatInRow, ticketNumber);
    }
}
