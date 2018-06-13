package Observer;

public interface Subject {
    void attatch(Observer o);
    void detatched(Observer o);
    void notifyAllUsers();
}
