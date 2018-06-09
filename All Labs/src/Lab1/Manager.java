package Lab1;

import java.util.Date;
import java.util.Objects;

public class Manager extends Employee implements Cloneable {
    public Date hireDate;

    public Manager(String name, double salary, Date hireDate) {
        super(name, salary);
        this.hireDate = hireDate;
    }

    public Date getHireDate(){
        return this.hireDate;
    }

    public Manager m(){
        Manager m = new Manager("bob",3,new Date());
        System.out.println(m.toString());
        return m;
    }

    public Manager clone() throws CloneNotSupportedException{
            return (Manager)super.clone();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manager)) return false;
        Manager manager = (Manager) o;
        return Objects.equals(getHireDate(), manager.getHireDate()) && getName() == ((Manager) o).getName() && getSalary() == ((Manager) o).getSalary();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getHireDate());
    }
}
