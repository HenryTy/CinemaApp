package ty.henry.cinemaapp.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TicketPriceCalculator {

    public static final float TICKET_PRICE = 25.0f;

    public static float getTicketPriceWithDiscount(int userPoints) {
        float priceWithDiscount = (100 - userPoints) * TICKET_PRICE / 100;
        return new BigDecimal(Float.toString(priceWithDiscount)).setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
