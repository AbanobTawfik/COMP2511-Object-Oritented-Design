package com.company;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.Date;

public class tests {

    public static void runtests(){
        case1();
        case2();
        case3();
        case4();
        case5();
    }

    /**
     * this is a function designed to test the scanerio when an employee is equal to clone
     * checks the clone and equals function made
     * i expected for them to both be equal in each scanerio
     */
    public static void case1() {
        System.out.println("Testing whether employee is = clone of employee");

        employee e = new employee(3, 4, "juan");
        employee clone = e.cloneemployee(e);
        String eDetails = e.employeedetails();
        String cloneDetails = clone.employeedetails();

        if (eDetails.equals(cloneDetails))
            System.out.println("test passed equal employees");

        Boolean flag = e.equalemployees(e, clone);
        if (flag == true)
            System.out.println("Clone feature works as intended grats m9-1");

        System.out.println(" ");
    }

    /**
     * this case is to test whether a manager with the same details as the employee superclass is the same
     * the function made to test this worked out correctly
     */
    public static void case2(){

        employee e = new employee(69,69,"69lord");

        Date hiringDate = new Date(1945,4,21);
        manager m = new manager(69,69,"69lord",hiringDate);
        System.out.println(m.managerdetails());
        System.out.println(e.employeedetails());
        Boolean flag = m.checkmanagerisemployee(m,e);
        if(flag == true)
            System.out.println("your check manager is employee function is correct");
        System.out.println(" ");
    }

    /**
     * this test case tests whether a cloned employee has the same name as the original employee
     * the test case passed
     */
    public static void case3(){
        employee e1 = new employee(69,69,"69lord");
        employee e2 = e1.cloneemployee(e1);
        System.out.println(e1.employeedetails());
        System.out.println(e2.employeedetails());
        Boolean flag = e1.getName().equals(e2.getName());
        if(flag == true)
            System.out.println("the names of the cloned employee equals the name of the original employee\ntest case passed");
        System.out.println(" ");
    }

    /**
     * this test case tests what happens when you change the hiredate of a cloned manager
     * it wants to check whether it will also change the original
     * my guess is that it does not as the clone is created rather than having the same pointer in memory
     */

    public static void case4(){
        Date d = new Date(1546,10,1);
        manager m = new manager(12,13,"juansmanager",d);
        manager cloned = m.clonemanager(m);
        Date newDate = new Date(1547,10,1);
        cloned.setHiredate(newDate);

        System.out.println(m.managerdetails());
        System.out.println(cloned.managerdetails());

        Boolean flag = m.getHiredate().equals(cloned.getHiredate());

        if(flag == false)
            System.out.println("Your prediction was true and date for original was not changed");
        System.out.println(" ");

    }

    /**
     * this method tests what methods can be called for the manager when
     * the manager is declared as an employee object
     * i predict that only employee methods can be called
     * infact it turned out the only things you can do to the object is effect age/salary/name
     * however NO MANAGER FUNCTIONS/PROPERTIES CAN BE USED ON AN EMPLOYEE OBJECT
     */
    public static void case5(){
        Date d = new Date(1546,10,1);
        employee m = new manager(4,5,"juan",d);

        System.out.println(m.employeedetails());
        System.out.println("As was expected, only employee methods can be called as it is still an employee object NOT A MANAGER");
    }
}
