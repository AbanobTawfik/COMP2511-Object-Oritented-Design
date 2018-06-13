package Iterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class IteratorImplement<E> implements Iterator{
    int index = 0;
    private E[] elements;

    public IteratorImplement(E[] elements) {
        this.elements = elements;
    }

    public IteratorImplement(ArrayList<E> s) {
        this.elements = (E[]) s.toArray();
    }

    public IteratorImplement(HashSet<E> s) {
        this.elements = (E[]) s.toArray();
    }

    public IteratorImplement(LinkedList<E> s) {
        this.elements = (E[]) s.toArray();
    }
    @Override
    public boolean hasNext() {
        return index < elements.length;
    }

    @Override
    public E next() {
        if(hasNext())
            return elements[index++];
        return null;
    }
}
