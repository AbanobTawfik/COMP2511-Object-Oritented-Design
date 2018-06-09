package Lab4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public interface Set<E> extends Iterable<E> {
    boolean add(E item);
    boolean remove(E item);
    boolean remove(int index);
    boolean exists(E item);
    Set<E> Union(Set<E> union);
    Set<E> Intersect(Set<E> intersect);
    Set<E> subset(Comparator c);
    boolean equals(Object o);
    Iterator<E> iterator();
    List<E> getAll();
}
