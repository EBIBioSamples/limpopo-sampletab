package uk.ac.ebi.arrayexpress2.sampletab.comparator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ComparatorList<T extends Comparable<T>> implements Comparator<List<T>> {

    public int compare(List<T> o1, List<T> o2) {

        Integer size1 = new Integer(o1.size());
        Integer size2 = new Integer(o2.size());
        int score = 0;
        score = size1.compareTo(size2);
        if (score != 0){
            return score;
        }
        

        Iterator<T> i1 = o1.iterator();
        Iterator<T> i2 = o2.iterator();

        while(i1.hasNext()) {
            T n1 = i1.next();
            T n2 = i2.next();
            
            score = n1.compareTo(n2);
            
            if (score != 0) {
                return score;
            }
        }   
        return 0;
    }

    //TODO public boolean equals(Object other)
    
    //TODO serializable interface
    
}
