package ty.henry.cinemaapp.dto;

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
        return seatStates[row][seat];
    }

    public void clickOnSeat(int row, int seat) {
        if(seatStates[row][seat] == SeatState.FREE) {
            seatStates[row][seat] = SeatState.CLICKED;
        }
        else if(seatStates[row][seat] == SeatState.CLICKED) {
            seatStates[row][seat] = SeatState.FREE;
        }
    }

    public void setReserved(int row, int seat) {
        seatStates[row][seat] = SeatState.RESERVED;
    }

    public enum SeatState {
        FREE,
        CLICKED,
        RESERVED
    }
}
