package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class bankaccountTest {

    @Test
    void testNegativeBalanceinitialandrandomseed(){
        bankaccount account = new bankaccount(-30,-30);
        Assertions.assertEquals(0,account.getCurrentbalance());
        System.out.println("This ID SHUD NOT BE -30 but random" + "\n" + account.getId() + "\n");
        Assertions.assertEquals(0,account.getNumberOfWithdraws());
        Assertions.assertEquals(0,account.getAmountWithdrawn());

    }

    @Test
    void testDeposit() {
        //initial balance = 0 check if deposit works check if deposit negative numbers works
        //check if starting bank account with negative balance works
        bankaccount account = new bankaccount(0,0);
        Assertions.assertEquals(account.getCurrentbalance(),0);
        boolean ret = account.deposit(100);
        Assertions.assertEquals(true, ret);
        Assertions.assertEquals(100,account.getCurrentbalance());
        Assertions.assertEquals(0,account.getNumberOfWithdraws());
        Assertions.assertEquals(0,account.getNumberOfWithdraws());
    }

    @Test
    void testWithdraw() {
        bankaccount account = new bankaccount(440,0);
        boolean result = account.withdraw(-100);
        Assertions.assertEquals(false, result);
        Assertions.assertEquals(0, account.getAmountWithdrawn());
        Assertions.assertEquals(0, account.getNumberOfWithdraws());
        Assertions.assertEquals(440, account.getCurrentbalance());
    }

    @Test
    void testWithdrawoverlimit(){
        bankaccount account = new bankaccount(0,0);
        account.deposit(300);
        boolean result = account.withdraw(400);
        Assertions.assertEquals(false,result);
        Assertions.assertEquals(300,account.getCurrentbalance());
        Assertions.assertEquals(0, account.getAmountWithdrawn());
        Assertions.assertEquals(0, account.getNumberOfWithdraws());

    }

    @Test
    void testWithdrawoverdailylimit(){
        bankaccount account = new bankaccount(6969,0);
        boolean result =    account.withdraw(801);
        Assertions.assertEquals(false,result);
        Assertions.assertEquals(0, account.getAmountWithdrawn());
        Assertions.assertEquals(0, account.getNumberOfWithdraws());
        Assertions.assertEquals(6969, account.getCurrentbalance());

    }
    @Test
    void testWithdrawoverdailylimit2(){
        bankaccount account = new bankaccount(6969,0);
        boolean result = account.withdraw(799);
        Assertions.assertEquals(true,result);
        Assertions.assertEquals(799, account.getAmountWithdrawn());
        Assertions.assertEquals(1, account.getNumberOfWithdraws());
        Assertions.assertEquals(6969-799, account.getCurrentbalance());
        result = account.withdraw(799);
        Assertions.assertEquals(false,result);
        Assertions.assertEquals(799, account.getAmountWithdrawn());
        Assertions.assertEquals(1, account.getNumberOfWithdraws());
        Assertions.assertEquals(6969-799, account.getCurrentbalance());

    }
    @Test
    void testRegularWithdraw(){
        bankaccount accounts = new bankaccount(6969,0);
        boolean result = accounts.withdraw(99);
        Assertions.assertEquals(true,result);
        Assertions.assertEquals(99, accounts.getAmountWithdrawn());
        Assertions.assertEquals(1, accounts.getNumberOfWithdraws());
        Assertions.assertEquals(6969-99, accounts.getCurrentbalance());
        result = accounts.withdraw(100);
        Assertions.assertEquals(true,result);
        Assertions.assertEquals(199, accounts.getAmountWithdrawn());
        Assertions.assertEquals(2, accounts.getNumberOfWithdraws());
        Assertions.assertEquals(6969-199, accounts.getCurrentbalance());
    }

    @Test
    void TestInvariant(){
        /**
         * TESTING NOW IF U CAN START ACCOUNT AND GET BALANCE < 0 NOPE
         */
        bankaccount accounts = new bankaccount(-30, 40);
        Assertions.assertEquals(0,accounts.getCurrentbalance());
        Assertions.assertEquals(40,accounts.getId());
        Assertions.assertEquals(0, accounts.getAmountWithdrawn());
        Assertions.assertEquals(0, accounts.getNumberOfWithdraws());
        boolean result = accounts.withdraw(-100);
        Assertions.assertEquals(false, result);
        Assertions.assertEquals(0, accounts.getAmountWithdrawn());
        Assertions.assertEquals(0, accounts.getNumberOfWithdraws());
        Assertions.assertEquals(0, accounts.getCurrentbalance());

    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme