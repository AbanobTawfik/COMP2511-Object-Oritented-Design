package Observer;

public class Student implements Observer {
    private String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void update(Object o) {
        if (o instanceof EBGames)
            System.out.println("Hey! " + name + " New game in store come check it out");
        else if (o instanceof IKEA)
            System.out.println("Hey! " + name + " Fuck you more spam");
        else
            System.out.println("Hey! " + name + " irrelevant info");
    }
}
