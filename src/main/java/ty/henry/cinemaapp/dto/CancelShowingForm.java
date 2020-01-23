package ty.henry.cinemaapp.dto;

import javax.validation.constraints.Size;

public class CancelShowingForm {

    private long showingId;

    @Size(min = 1, max = 255, message = "Wiadomość musi zawierać od 1 do 255 znaków")
    private String message;

    public long getShowingId() {
        return showingId;
    }

    public void setShowingId(long showingId) {
        this.showingId = showingId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
