package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public class ComparatorSortedSet<T> implements Comparator<SortedSet<T>> {

    public int compare(SortedSet<T> ts1, SortedSet<T> ts2) {
        if (ts1 == null && ts2 == null){
            return 0;
        }
        if (ts1 != null && ts2 == null){
            return -1;
        }
        if (ts1 == null && ts2 != null){
            return 1;
        }
        
        Integer size1 = new Integer(ts1.size());
        Integer size2 = new Integer(ts2.size());
        int score = 0;
        score = size1.compareTo(size2);
        if (score != 0){
            return score;
        }
        
        Iterator<T> i1 = ts1.iterator();
        Iterator<T> i2 = ts2.iterator();

        Comparator<? super T> c = ts1.comparator();
        
        while(i1.hasNext()) {
            T n1 = i1.next();
            T n2 = i2.next(); 
            if (c != null){
                score = c.compare(n1, n2);
            } else {
                try {
                    Comparable<T> c1 = (Comparable<T>) n1;
                    score = c1.compareTo(n2);
                } catch (ClassCastException e){
                    return -1;
                }
            }
            
            if (score != 0) {
                return score;
            }
        }
        
        return 0;
    }

    //TODO public boolean equals(Object other)
    
    //TODO serializable interface
}
