package ru.sbt.edu.hw2.ex95;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private int balance;
    private final Lock lock = new ReentrantLock();
    private final Condition income = lock.newCondition();
    private int preferred = 0;
    private final Lock preferredLock = new ReentrantLock();
    private final Condition preferredFinished = lock.newCondition();

    public void deposit(int amount){
        lock.lock();
        try {
            balance += amount;
            income.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void transfer(int amount, Account reserve){
        lock.lock();
        try {
            reserve.withdraw(amount);
            deposit(amount);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(int amount) {
        preferredLock.lock();
        try {
            while (preferred > 0) preferredFinished.await();
            doWithdraw(amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            preferredLock.unlock();
        }
    }

    public void preferredWithdrawal(int amount) {
        preferredLock.lock();
        try {
            preferred++;
            doWithdraw(amount);
            preferred--;
            if (preferred == 0) preferredFinished.signalAll();
        } finally {
            preferredLock.unlock();
        }
    }

    private void doWithdraw(int amount) {
        lock.lock();
        try {
            awaitIncome(amount);
            balance -= amount;
        } finally {
            lock.unlock();
        }
    }

    private void awaitIncome(int amount){
        while (balance < amount) {
            try {
                income.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
