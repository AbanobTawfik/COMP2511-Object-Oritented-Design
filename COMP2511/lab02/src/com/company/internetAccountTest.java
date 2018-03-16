package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class internetAccountTest {

    @Test
    void testNegativeBalanceinitialandrandomseed(){
        internetAccount IA = new internetAccount(-30,-30);
        Assertions.assertEquals(0,IA.getCurrentbalance());
        System.out.println("This ID SHUD NOT BE -30 but random" + "\n" + IA.getId() + "\n");
        Assertions.assertEquals(0,IA.getNumberOfWithdraws());
        Assertions.assertEquals(0,IA.getAmountWithdrawn());
        Assertions.assertEquals(0 , IA.getNumberOfInternetPayments());
    }

    @Test
    void testValidInternetPayment() {
        internetAccount IA = new internetAccount(0, 40);
        IA.deposit(300);
        Assertions.assertEquals(300,IA.getCurrentbalance());
        Assertions.assertEquals(40,IA.getId());
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0 , IA.getNumberOfInternetPayments());

        boolean result = IA.internetPayment(200);
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(300-200,IA.getCurrentbalance());
        Assertions.assertEquals(1, IA.getNumberOfWithdraws());
        Assertions.assertEquals(200, IA.getAmountWithdrawn());
        Assertions.assertEquals(1, IA.getNumberOfInternetPayments());
    }

    /**
     * testing OVER the deposit
     */
    @Test
    void testinvalidpayment1(){
        internetAccount IA = new internetAccount(0, 40);
        IA.deposit(300);
        Assertions.assertEquals(300,IA.getCurrentbalance());
        Assertions.assertEquals(40,IA.getId());
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0 , IA.getNumberOfInternetPayments());

        boolean result = IA.internetPayment(500);
        Assertions.assertEquals(false, result);
        Assertions.assertEquals(300,IA.getCurrentbalance());
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0 , IA.getNumberOfInternetPayments());
    }

    @Test
    void testInvalidPayment2(){
        internetAccount IA = new internetAccount(60000, 40);
        Assertions.assertEquals(60000,IA.getCurrentbalance());
        Assertions.assertEquals(40,IA.getId());
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0 , IA.getNumberOfInternetPayments());

        boolean result = IA.internetPayment(799);
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(60000-799,IA.getCurrentbalance());
        Assertions.assertEquals(799, IA.getAmountWithdrawn());
        Assertions.assertEquals(1, IA.getNumberOfWithdraws());
        Assertions.assertEquals(1 , IA.getNumberOfInternetPayments());

        result = IA.internetPayment(799);
        Assertions.assertEquals(false, result);
        Assertions.assertEquals(60000-799,IA.getCurrentbalance());
        Assertions.assertEquals(799, IA.getAmountWithdrawn());
        Assertions.assertEquals(1, IA.getNumberOfWithdraws());
        Assertions.assertEquals(1 , IA.getNumberOfInternetPayments());
    }
    @Test
    void test10paymentlimit(){
        internetAccount IA = new internetAccount(60000, 40);
        Assertions.assertEquals(60000,IA.getCurrentbalance());
        Assertions.assertEquals(40,IA.getId());
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0 , IA.getNumberOfInternetPayments());

        for(int x = 0; x<=9 ; x++){
            boolean result = IA.internetPayment(10);
            Assertions.assertEquals(true,result);
            Assertions.assertEquals(60000 - 10*(x+1), IA.getCurrentbalance());
            Assertions.assertEquals(10*(x+1), IA.getAmountWithdrawn());
            Assertions.assertEquals(x+1, IA.getNumberOfWithdraws());
            Assertions.assertEquals(x+1, IA.getNumberOfInternetPayments());
        }

        boolean result = IA.internetPayment(10);
        Assertions.assertEquals(false,result);
        Assertions.assertEquals(60000 - 10*(9+1), IA.getCurrentbalance());
        Assertions.assertEquals(10*(9+1), IA.getAmountWithdrawn());
        Assertions.assertEquals(9+1, IA.getNumberOfWithdraws());
        Assertions.assertEquals(9+1, IA.getNumberOfInternetPayments());
    }

    @Test
    void testDeposit() {
        internetAccount IA = new internetAccount(60000, 40);
        IA.deposit(300);
        Assertions.assertEquals(60000+300,IA.getCurrentbalance());
        Assertions.assertEquals(40,IA.getId());
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0 , IA.getNumberOfInternetPayments());
    }

    @Test
    void testvalidmultiplewithdraws(){
        internetAccount IA = new internetAccount(0, 40);
        IA.deposit(300);
        Assertions.assertEquals(300,IA.getCurrentbalance());
        Assertions.assertEquals(40,IA.getId());
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0 , IA.getNumberOfInternetPayments());

        boolean result = IA.internetPayment(200);
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(300-200,IA.getCurrentbalance());
        Assertions.assertEquals(1, IA.getNumberOfWithdraws());
        Assertions.assertEquals(200, IA.getAmountWithdrawn());
        Assertions.assertEquals(1, IA.getNumberOfInternetPayments());

        result = IA.internetPayment(1.48);
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(300-200-1.48,IA.getCurrentbalance());
        Assertions.assertEquals(2, IA.getNumberOfWithdraws());
        Assertions.assertEquals(200+1.48, IA.getAmountWithdrawn());
        Assertions.assertEquals(2, IA.getNumberOfInternetPayments());
    }
    @Test
    void testWithdraw() {
        internetAccount IA = new internetAccount(0, 40);
        IA.deposit(300);
        Assertions.assertEquals(300,IA.getCurrentbalance());
        Assertions.assertEquals(40,IA.getId());
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0 , IA.getNumberOfInternetPayments());

        boolean result = IA.withdraw(200);
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(300-200,IA.getCurrentbalance());
        Assertions.assertEquals(1, IA.getNumberOfWithdraws());
        Assertions.assertEquals(200, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfInternetPayments());
    }

    @Test
    void testinvariance(){
        /**
         * TESTING NOW IF U CAN START ACCOUNT AND GET BALANCE < 0 NOPE
         */
        internetAccount IA = new internetAccount(-30, 40);
        Assertions.assertEquals(0,IA.getCurrentbalance());
        Assertions.assertEquals(40,IA.getId());
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0,IA.getNumberOfInternetPayments());
        boolean result = IA.withdraw(-100);
        Assertions.assertEquals(false, result);
        Assertions.assertEquals(0, IA.getAmountWithdrawn());
        Assertions.assertEquals(0, IA.getNumberOfWithdraws());
        Assertions.assertEquals(0, IA.getCurrentbalance());
        Assertions.assertEquals(0,IA.getNumberOfInternetPayments());
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme