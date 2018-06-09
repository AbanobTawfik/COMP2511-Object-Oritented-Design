package Lab4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ArraySet<E> implements Set<E> {
    private ArrayList<E> set = new ArrayList<E>();

    @Override
    public boolean add(E item) {
        if (exists(item))
            return false;
        set.add(item);
        return true;
    }

    @Override
    public boolean remove(E item) {
        if (!exists(item))
            return false;
        set.remove(item);
        return true;
    }

    @Override
    public boolean remove(int index) {
        if (index >= set.size())
            return false;
        set.remove(index);
        return true;
    }

    @Override
    public boolean exists(E item) {
        return set.contains(item);
    }

    @Override
    public Set<E> Union(Set<E> union) {
        ArraySet<E> newSet = new ArraySet<E>();
        for (E item : union.getAll())
            newSet.add(item);
        for (E item : set)
            newSet.add(item);
        return newSet;
    }

    @Override
    public Set<E> Intersect(Set<E> intersect) {
        ArraySet<E> newSet = new ArraySet<E>();
        for(E item : set)
            if(intersect.exists(item))
                newSet.add(item);
        return newSet;
    }

    @Override
    public ArraySet<E> subset(Comparator c) {
        ArraySet<E> newSet = new ArraySet<E>();
        for(E item : set)
            if(c.compare(item,item) == 0)
                newSet.add(item);
        return newSet;
    }

    @Override
    public Iterator<E> iterator() {
        return set.listIterator();
    }

    @Override
    public List<E> getAll() {
        return this.set;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof ArraySet))
            return false;
        ArraySet<E> traverse = (ArraySet<E>) o;
        return traverse.getAll().containsAll(set) && set.containsAll(traverse.getAll());
    }
}
