package Observer;

import java.util.ArrayList;

public class EBGames implements Subject {
    private ArrayList<Observer> observers = new ArrayList<Observer>();

    @Override
    public void attatch(Observer o) {
        observers.add(o);
    }

    @Override
    public void detatched(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyAllUsers() {
        for (Observer o : observers)
            o.update(this);
    }
}
