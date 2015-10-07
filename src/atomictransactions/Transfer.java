/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atomictransactions;

import java.util.Random;

/**
 *
 * @author Brandon
 */
public class Transfer extends Transaction {

    /**
     * A thread to transfer funds from one account to another
     *
     * @param accountFrom index of the account from which funds will be
     * transferred
     * @param accountTo index of the account to which funds will be transferred
     * @param amount value to be transferred
     */
    public Transfer(int accountFrom, int accountTo, int amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    @Override
    public void run() {
        this.delay();
        this.acquireWriteLock(Math.min(accountFrom, accountTo));
        this.acquireWriteLock(Math.max(accountFrom, accountTo));
        int currentBalanceFrom = AtomicTransactions.getBalance(accountFrom);
        int newBalanceFrom = currentBalanceFrom - amount;
        this.log.add(new ActionLog(accountFrom, currentBalanceFrom, newBalanceFrom));
        AtomicTransactions.setBalance(accountFrom, newBalanceFrom);
        int chance = r.nextInt(100);
        if (chance == 0) {
            this.undoLog();
            System.out.println(Thread.currentThread() + " Transaction cancelled");
        } else {
            int currentBalanceTo = AtomicTransactions.getBalance(accountTo);
            int newBalanceTo = currentBalanceTo + amount;
            this.log.add(new ActionLog(accountTo, currentBalanceTo, newBalanceTo));
            AtomicTransactions.setBalance(accountTo, newBalanceTo);
            System.out.println(Thread.currentThread() + " transfered " + amount + " from account "
                    + accountFrom + " to account " + accountTo);
        }
        this.releaseWriteLock(Math.min(accountFrom, accountTo));
        this.releaseWriteLock(Math.max(accountFrom, accountTo));
    }

    private final int accountFrom;
    private final int accountTo;
    private final int amount;
    private final Random r = new Random();
}
