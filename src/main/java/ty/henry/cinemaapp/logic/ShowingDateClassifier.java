package ty.henry.cinemaapp.logic;

import ty.henry.cinemaapp.model.Showing;

import java.time.LocalDate;
import java.util.*;

public class ShowingDateClassifier {

    private ShowingClassifier<LocalDate> classifier;

    public ShowingDateClassifier(Collection<Showing> showings) {
        classifier = new ShowingClassifier<>(showings, ShowingClassifier.BY_DATE_CLASSIFICATION_FUNCTION);
    }

    public Collection<LocalDate> getDates() {
        return classifier.getKeys();
    }

    public List<Showing> getShowingsByDate(LocalDate date) {
        return classifier.getShowingsByKey(date);
    }
}
