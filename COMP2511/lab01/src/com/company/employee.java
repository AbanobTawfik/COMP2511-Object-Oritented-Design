package com.company;
import java.util.Date;

public class employee{
        private int age;
        private int salary;
        private String name;

        public employee(int age, int salary, String name) {
            this.age = age;
            this.salary = salary;
            this.name = name;
        }

    /**
     * constructor for the employee sets all the data in the appropriate fields
     * @return
     */

    public int getAge() {
            return age;
        }

    /**
     * getter method for age
     * @param age
     */
        public void setAge(int age) {
            this.age = age;
        }

    /**
     * setter method for age
     */

    public int getSalary() {
            return salary;
        }

    /**
     * getter method for salary
     * @param salary
     */

    public void setSalary(int salary) {
            this.salary = salary;
        }

    /**
     * setter method for salary
     */

    public String getName() {
            return name;
        }

    /**
     * getter method for name
     * @param name
     */

    public void setName(String name) {
            this.name = name;
        }

    /**
     * setter method for name
     */

    public String employeedetails() {
            return "employee{" +
                    "age=" + age +
                    ", salary=" + salary +
                    ", name='" + name + '\'' +
                    '}';
        }

    /**
     * to string method for employee
     * prints out name/age/salary
     */

    public employee cloneemployee(employee e){
        employee e1 = new employee(e.getAge(),e.getSalary(),e.getName());
        return e1;
    }

    public boolean equalemployees(employee e1, employee e2){
            return e1.getAge() == e2.getAge() && e1.getSalary() == e2.getSalary() && e1.getName() == e2.getName();
    }

    /**
     * method to check whether the employees are the same
     * compares every field for both employees to check equal
     */

}

    class manager extends employee{
        private Date hiredate;

        public manager(int age, int salary, String name, Date hiredate) {
            super(age, salary, name);
            this.hiredate = hiredate;
        }

        /**
         * manager constructor
         * uses super keyword to build the employee superclass for this subclass
         */

        public manager clonemanager(manager m){
            manager m1 = new manager(m.getAge(),m.getSalary(),m.getName(),m.getHiredate());
            return m1;
        }

        /**
         * clones the manager initalises a new manager with the attributes of the manager input
         */
        public Date getHiredate() {
            return hiredate;
        }

        /**
         *getter method for hire date
         */

        public void setHiredate(Date hiredate) {
            this.hiredate = hiredate;
        }

        /**
         *setter method for  hiredate
         */

        public String managerdetails() {
            return "manager{" +
                    "age=" + getAge() +
                    ", salary=" + getSalary() +
                    ", name='" + getName() + '\'' +
                    ", hiredate=" + hiredate +
                    '}';
        }

        /**
         * prints out manager with all the details
         */

        public boolean checkmanagerisemployee(manager m, employee e){
            return m.getAge() == e.getAge() && m.getName() == e.getName() && m.getSalary() == e.getSalary();
        }

        /**
         * comparison statement, compares all fields of employee to all equal fields of manager
         * if they are all equal then that means the manager is the employee referenced
         */

        /**
         * when getClass.getName is called in the tostring method of the employee with a manager object
         * i expected the output to be
         * getClass returns the class of the object so - manager
         * this means that we arent referring to employee.getName or manager.getName
         * we are returning the name of the class
         * so i expected return or output to be Manager
         */
    }
