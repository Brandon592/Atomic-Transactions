/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atomictransactions;

/**
 *
 * @author Brandon
 */
public class ActionLog {

    public ActionLog(int accountNumber, int priorBalance, int postBalance) {
        this.accountNumber = accountNumber;
        this.priorBalance = priorBalance;
        this.postBalance = postBalance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getPriorBalance() {
        return priorBalance;
    }

    public int getPostBalance() {
        return postBalance;
    }
    
    private final int accountNumber;
    private final int priorBalance;
    private final int postBalance;
}
