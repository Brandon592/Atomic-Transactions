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
public class Deposit extends Transaction{
        
    /**
     * A thread to deposit money into an account
     * @param accountNumber index of the account
     * @param amount value to be deposited in the account
     */
    public Deposit(int accountNumber, int amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }
    
    @Override
    public void run(){
        this.delay();
        this.acquireWriteLock(accountNumber);
        int currentBalance = AtomicTransactions.getBalance(accountNumber);
        int newBalance = currentBalance + amount;
        log.add(new ActionLog(accountNumber, currentBalance, newBalance));
        AtomicTransactions.setBalance(accountNumber, newBalance);
        System.out.println(Thread.currentThread() + " deposited " + amount + " in account "
        + accountNumber + ". New Balance: " + newBalance);
        this.releaseWriteLock(accountNumber);
    }
    
    private final int accountNumber;
    private final int amount;

}
