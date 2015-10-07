/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atomictransactions;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Brandon
 */
public abstract class Transaction extends Thread {
    
    /**
     * Every account modification that a transaction performs is recorded. This 
     * method will undo in reverse order each of the modifications that have 
     * been made up to the point where it is called.
     */
    protected void undoLog(){
        for (int i = (log.size()-1); i >= 0; i--){
            ActionLog toUndo = log.get(i);
            AtomicTransactions.setBalance(toUndo.getAccountNumber(), toUndo.getPriorBalance());
        }
    }
    
    /**
     *This method encapsulates the sleep method of Thread to avoid using a try-
     * catch block every time
     * @param milliseconds
     */
    protected void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            //ignore this. Not gonna happen
        }
    }
    
    /**
     * This method attempts to acquire a read lock for a desired account. If it
     * fails, it sleeps for 10 milliseconds and then tries again.
     * @param i index of the account for which the lock is requested
     */
    protected void acquireReadLock(int i){
        while (!AtomicTransactions.acquireReadLock(i)){
            this.sleep(10);
        }
        System.out.println(Thread.currentThread() + " acquired read lock for account " + i);
    }
    
    /**
     *This method attempts to acquire a write lock for a desired account. If it
     * fails, it sleeps for 10 milliseconds and then tries again.
     * @param i index of the account for which the lock is requested
     */
    protected void acquireWriteLock(int i){
        while (!AtomicTransactions.acquireWriteLock(i)){
            this.sleep(10);
        }
        System.out.println(Thread.currentThread() + " acquired write lock for account " + i);
    }
    
    /**
     * This method releases a read lock for a specified account
     * @param i index of the account to be released
     */
    protected void releaseReadLock(int i){
        AtomicTransactions.releaseReadLock(i);
        System.out.println(Thread.currentThread() + " released read lock for account " + i);
    }
    
    /**
     * This method releases a write lock for a specified account
     * @param i index of the account to be released
     */
    protected void releaseWriteLock(int i){
        AtomicTransactions.releaseWriteLock(i);
        System.out.println(Thread.currentThread() + " released write lock for account " + i);
    }
    
    /**
     * This method is called in the run method of each different transaction to
     * give a randomized delay before execution begins. This leads to a more interleaved
     * execution and proves the atomicity of the program
     */
    protected void delay(){
        this.sleep(r.nextInt(100));
    }
    
    protected final ArrayList<ActionLog> log = new ArrayList();
    private final Random r = new Random();
}
