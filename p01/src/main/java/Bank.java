public class Bank{
    private double balance;

    public Bank(double startAmount) {
        this.balance = startAmount;
    }

    public double getBalance() {
        return balance;
    }

    //deposit funds into an account (use doubles, not ints)
    public void deposit(double balance) {
        this.balance += balance;
    }

    //withdraw funds from an account (no overdrafting!)
    public void withdraw(double balance) {
        if(balance <= this.balance){
            this.balance -= balance;
        }
        else{
            System.out.println("Insufficient Funds! ");
        }
    }
}
