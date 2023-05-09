import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ex2 {
    public static void main(String[] args) {
        Account account = new Account(10000);
        System.out.println("Account using ReadWriterLock");
        for(int i = 1; i <= 2; i++) {
            DepositClient c2 = new DepositClient("Deposit " + i, account);
            CheckClient c1 = new CheckClient("Check " + i, account);
        }
//        WithdrawClient c3 = new WithdrawClient("Withdraw ", account);
    }
}
class Account {
    private int balance;
    private ReadWriteLock lock;
    private Lock readLock, writeLock;
    public Account(int balance) {
        this.balance = balance;
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }
    public void deposit(int money) {
        writeLock.lock();
        try {
            this.balance += money;
        } catch(Exception e) { }
        finally {
            writeLock.unlock();
        }
    }
    public int check() {
        readLock.lock();
        try {
            return this.balance;
        } catch(Exception e) { }
        finally {
            readLock.unlock();
        }
        return 0;
    }
}
class CheckClient extends Thread{
    private Account account;
    public CheckClient(String name, Account account) {
        super(name);
        this.account = account;
        start();
    }

    @Override
    public void run() {
        try {
            while(true) {
                Thread.sleep((int) (Math.random() * 5000));
                System.out.println(this.getName() + " tries to check account");
                int balance = this.account.check();
                System.out.println(this.getName() + ": account balance = " + balance);
            }
        } catch (Exception e) { }
    }
}
class DepositClient extends Thread{
    private Account account;
    public DepositClient(String name, Account account) {
        super(name);
        this.account = account;
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep((int) (Math.random() * 5000));
                System.out.println(this.getName() + " tries to deposit account");
                int money = (int) (Math.random() * 10000);
                this.account.deposit(money);
                System.out.println(this.getName() + ": deposit " + money);
            }
        } catch (Exception e) { }
    }
}