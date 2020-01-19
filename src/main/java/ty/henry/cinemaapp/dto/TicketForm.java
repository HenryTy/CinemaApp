package ty.henry.cinemaapp.dto;

import ty.henry.cinemaapp.logic.TicketPriceCalculator;
import ty.henry.cinemaapp.model.Showing;
import ty.henry.cinemaapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class TicketForm {

    private User user;
    private Showing showing;
    private SeatState[][] seatStates;
    private int clickedSeatsCount;

    private boolean successfulPurchase;
    private String generatedTicketNumber;
    private String errorMessage;

    public TicketForm(int rowCount, int seatsInRow) {
        seatStates = new SeatState[rowCount][seatsInRow];
        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < seatsInRow; j++) {
                seatStates[i][j] = SeatState.FREE;
            }
        }
        clickedSeatsCount = 0;
    }

    public int getRowCount() {
        return seatStates.length;
    }

    public int getSeatsInRow() {
        return seatStates[0].length;
    }

    public int getUserId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getShowingId() {
        return showing.getId();
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public SeatState getSeatState(int row, int seat) {
        return seatStates[row-1][seat-1];
    }

    public void clickOnSeat(int row, int seat) {
        if(seatStates[row-1][seat-1] == SeatState.FREE) {
            seatStates[row-1][seat-1] = SeatState.CLICKED;
            clickedSeatsCount++;
        }
        else if(seatStates[row-1][seat-1] == SeatState.CLICKED) {
            seatStates[row-1][seat-1] = SeatState.FREE;
            clickedSeatsCount--;
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

    public String getOneTicketPrice() {
        return String.format("%.2f PLN", TicketPriceCalculator.TICKET_PRICE);
    }

    public String getDiscount() {
        return user.getPoints() + "%";
    }

    public String getTicketPriceWithoutDiscount() {
        return String.format("%.2f PLN", clickedSeatsCount * TicketPriceCalculator.TICKET_PRICE);
    }

    public String getTicketPriceWithDiscount() {
        return String.format("%.2f PLN", clickedSeatsCount * TicketPriceCalculator.getTicketPriceWithDiscount(user.getPoints()));
    }

    public boolean isSuccessfulPurchase() {
        return successfulPurchase;
    }

    public void setSuccessfulPurchase(boolean successfulPurchase) {
        this.successfulPurchase = successfulPurchase;
    }

    public String getGeneratedTicketNumber() {
        return generatedTicketNumber;
    }

    public void setGeneratedTicketNumber(String generatedTicketNumber) {
        this.generatedTicketNumber = generatedTicketNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public enum SeatState {
        FREE,
        CLICKED,
        RESERVED
    }
}
