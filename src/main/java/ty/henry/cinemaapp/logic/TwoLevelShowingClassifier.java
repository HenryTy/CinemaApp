package ty.henry.cinemaapp.logic;

import ty.henry.cinemaapp.model.Showing;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class TwoLevelShowingClassifier<K1, K2> {

    private Map<K1, ShowingClassifier<K2>> key1ToClassifierByKey2;

    public TwoLevelShowingClassifier(Collection<Showing> showings,
                                     Function<Showing, K1> firstLevelClassificationFunction,
                                     Function<Showing, K2> secondLevelClassificationFunction,
                                     Comparator<K1> firstLevelKeyComparator,
                                     Comparator<K2> secondLevelKeyComparator) {

        ShowingClassifier<K1> firstLevelClassifier = new ShowingClassifier<>(showings, firstLevelClassificationFunction);

        if(firstLevelKeyComparator == null) {
            key1ToClassifierByKey2 = new TreeMap<>();
        }
        else {
            key1ToClassifierByKey2 = new TreeMap<>(firstLevelKeyComparator);
        }

        for(K1 key1 : firstLevelClassifier.getKeys()) {
            ShowingClassifier<K2> secondLevelClassifier;
            Collection<Showing> toClassify = firstLevelClassifier.getShowingsByKey(key1);
            if(secondLevelKeyComparator == null) {
                secondLevelClassifier = new ShowingClassifier<>(toClassify, secondLevelClassificationFunction);
            }
            else {
                secondLevelClassifier = new ShowingClassifier<>(toClassify, secondLevelClassificationFunction,
                        secondLevelKeyComparator);
            }
            key1ToClassifierByKey2.put(key1, secondLevelClassifier);
        }
    }

    public Collection<K1> getFirstLevelKeys() {
        return key1ToClassifierByKey2.keySet();
    }

    public ShowingClassifier<K2> getSecondLevelClassifier(K1 firstLevelKey) {
        return key1ToClassifierByKey2.get(firstLevelKey);
    }
}
