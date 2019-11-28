package ty.henry.cinemaapp.logic;

import ty.henry.cinemaapp.model.Showing;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;

public class ShowingDateClassifier {

    private Map<LocalDate, List<Showing>> dateToShowings;

    public ShowingDateClassifier(Collection<Showing> showings) {
        dateToShowings = showings.stream().collect(groupingBy(showing -> showing.getShowingDate().toLocalDate(),
                TreeMap::new, toList()));
    }

    public Collection<LocalDate> getDates() {
        return dateToShowings.keySet();
    }

    public Collection<Showing> getShowingsByDate(LocalDate date) {
        return dateToShowings.get(date);
    }
}
