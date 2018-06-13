package Observer;

public class Main {
    public static void main(String args[]) {
        Main m = new Main();
        m.run();
    }

    public void run() {
        IKEA ikea = new IKEA();
        EBGames ebgames = new EBGames();

        Student wanton = new Student("chinli");
        Student eric = new Student("Eric");
        CarpanterStudent hans = new CarpanterStudent("Hans");
        CarpanterStudent rick = new CarpanterStudent("Rick");

        ebgames.attatch(wanton);
        ebgames.attatch(eric);
        ebgames.attatch(hans);
        ebgames.attatch(rick);

        ikea.attatch(wanton);
        ikea.attatch(eric);
        ikea.attatch(hans);
        ikea.attatch(rick);

        ebgames.notifyAllUsers();
        ikea.notifyAllUsers();

        ikea.notifyAllUsers();
        ebgames.notifyAllUsers();
    }
}