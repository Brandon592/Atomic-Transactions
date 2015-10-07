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
public class Balance extends Transaction {
    
    /**
     * A thread for retrieving the balance of the account specified
     * @param accountNumber index of the account
     */
    public Balance(int accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    @Override
    public void run(){
        this.delay();
        this.acquireReadLock(accountNumber);
        System.out.println(Thread.currentThread() + " Account " + accountNumber 
                + " balance: " + AtomicTransactions.getBalance(accountNumber));
        this.releaseReadLock(accountNumber);
    }
    
    private final int accountNumber;
    
}
