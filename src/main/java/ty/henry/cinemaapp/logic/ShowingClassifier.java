package ty.henry.cinemaapp.logic;

import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.model.Showing;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class ShowingClassifier<K> {

    private Map<K, List<Showing>> keyToShowings;

    public static Function<Showing, LocalDate> BY_DATE_CLASSIFICATION_FUNCTION =
            showing -> showing.getShowingDate().toLocalDate();
    public static Function<Showing, Movie> BY_MOVIE_CLASSIFICATION_FUNCTION =
            Showing::getMovie;

    public ShowingClassifier(Collection<Showing> showings, Function<Showing, K> classificationFunction) {
        keyToShowings = showings.stream().collect(groupingBy(classificationFunction,
                TreeMap::new, toList()));
    }

    public ShowingClassifier(Collection<Showing> showings, Function<Showing, K> classificationFunction,
                                  Comparator<K> keyComparator) {
        keyToShowings = showings.stream().collect(groupingBy(classificationFunction,
                () -> new TreeMap<>(keyComparator), toList()));
    }

    public Collection<K> getKeys() {
        return keyToShowings.keySet();
    }

    public Collection<K> getFirstNKeys(int n) {
        List<K> keyList = new ArrayList<>();
        for(K key : keyToShowings.keySet()) {
            if(keyList.size() == n) {
                break;
            }
            keyList.add(key);
        }
        return keyList;
    }

    public List<Showing> getShowingsByKey(K key) {
        return keyToShowings.get(key);
    }
}
