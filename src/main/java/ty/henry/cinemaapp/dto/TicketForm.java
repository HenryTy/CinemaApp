package ty.henry.cinemaapp.dto;

import java.util.ArrayList;
import java.util.List;

public class TicketForm {

    private int userId;
    private long showingId;
    private SeatState[][] seatStates;

    public TicketForm(int rowCount, int seatsInRow) {
        seatStates = new SeatState[rowCount][seatsInRow];
        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < seatsInRow; j++) {
                seatStates[i][j] = SeatState.FREE;
            }
        }
    }

    public int getRowCount() {
        return seatStates.length;
    }

    public int getSeatsInRow() {
        return seatStates[0].length;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getShowingId() {
        return showingId;
    }

    public void setShowingId(long showingId) {
        this.showingId = showingId;
    }

    public SeatState getSeatState(int row, int seat) {
        return seatStates[row-1][seat-1];
    }

    public void clickOnSeat(int row, int seat) {
        if(seatStates[row-1][seat-1] == SeatState.FREE) {
            seatStates[row-1][seat-1] = SeatState.CLICKED;
        }
        else if(seatStates[row-1][seat-1] == SeatState.CLICKED) {
            seatStates[row-1][seat-1] = SeatState.FREE;
        }
    }

    public void setReserved(int row, int seat) {
        seatStates[row-1][seat-1] = SeatState.RESERVED;
    }

    public List<int[]> getClickedSeats() {
        List<int[]> clickedSeats = new ArrayList<>();
        for(int i = 0; i < getRowCount(); i++) {
            for(int j = 0; j < getSeatsInRow(); j++) {
                if(seatStates[i][j] == SeatState.CLICKED) {
                    clickedSeats.add(new int[]{i+1, j+1});
                }
            }
        }
        return clickedSeats;
    }

    public enum SeatState {
        FREE,
        CLICKED,
        RESERVED
    }
}
