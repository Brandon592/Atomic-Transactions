/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atomictransactions;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class controls the interactions of multiple threads in a simulated bank-
 * account-style simulation. It implements two phase locking to preserve
 * atomicity
 *
 * @author Brandon
 */
public class AtomicTransactions {

    /**
     * The main method creates a series of transactions (which are extensions of
     * the Thread class) and runs them concurrently. In order to help them run
     * concurrently instead of sequentially, the first instruction in the run 
     * method of each transaction is a sleep for 0 to 100 milliseconds
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
            int x = r.nextInt(4);
            Transaction t;
            switch (x) {
                case 0:
                    t = new Balance(r.nextInt(10));
                    break;
                case 1:
                    t = new Withdrawal(r.nextInt(10), r.nextInt(100));
                    break;
                case 2:
                    t = new Deposit(r.nextInt(10), r.nextInt(100));
                    break;
                default:
                    int a1,
                     a2;
                    a1 = r.nextInt(10);
                    do {
                        a2 = r.nextInt(10);
                    } while (a2 == a1);
                    t = new Transfer(a1, a2, r.nextInt(100));
            }
            t.start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AtomicTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < accounts.length; i++) {
            System.out.println("Account " + i + " final balance: " + accounts[i]);
        }
    }

    /**
     * A function for reading the balance of an account with index i
     * @param i the index of the desired account
     * @return the balance of the requested account
     */
    public static int getBalance(int i) {
        return accounts[i];
    }

    /**
     * Modifies the balance of an account with index i
     * @param i the index of the account to be modified
     * @param newBalance the new value of the account's balance
     */
    public static void setBalance(int i, int newBalance) {
        accounts[i] = newBalance;
    }

    /**
     * Acquires a read lock for a particular account if that account does not have 
     * a current write lock. The read lock will allow other read locks, but not
     * write locks, to be established
     * @param i index of the account for which a lock is requested
     * @return true if the lock is successfully acquired, false otherwise
     */
    public static boolean acquireReadLock(int i) {
        if (!writeLock[i] && !writeLockWaiting[i]) {
            readLocks[i]++;
            return true;
        }
        return false;
    }

    /**
     * Acquires a write lock for a particular account if no other locks (read or
     * write) are currently in place. No other locks can be established after a 
     * write lock is in place
     * @param i index of the account for which a lock is requested
     * @return true if the lock is successfully acquired, false otherwise
     */
    public static boolean acquireWriteLock(int i) {
        if (readLocks[i] == 0 && !writeLock[i]) {
            writeLock[i] = true;
            writeLockWaiting[i] = false;
            return true;
        }
        writeLockWaiting[i] = true;
        return false;
    }

    /**
     * Releases one read lock from the specified account
     * @param i index of the account to be released
     */
    public static void releaseReadLock(int i) {
        readLocks[i]--;
    }

    /**
     * Removes the write lock from the specified account
     * @param i index of the account to be released
     */
    public static void releaseWriteLock(int i) {
        writeLock[i] = false;
    }

    private static final int[] accounts = {500, 500, 500, 500, 500, 500, 500, 500, 500, 500};
    private static final boolean[] writeLock = new boolean[10];
    private static final int[] readLocks = new int[10];
    private static final boolean[] writeLockWaiting = new boolean[10];
}
