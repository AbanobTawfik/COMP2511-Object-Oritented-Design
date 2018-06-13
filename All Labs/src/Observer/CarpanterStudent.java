package Observer;

public class CarpanterStudent implements Observer {
    private String name;

    public CarpanterStudent(String name) {
        this.name = name;
    }

    @Override
    public void update(Object o) {
        if(o instanceof EBGames)
            System.out.println("Hey! " + name + " why did u sign up go study");
        else if(o instanceof IKEA)
            System.out.println("Hey! " + name + " go back to beating it to ikea");
        else
            System.out.println("Hey! " + name + " random spam");
    }
}
