import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankTester {
    static Bank bank;

    @BeforeEach
    public void before() {
        bank = new Bank(50);
    }

    @Test
    public void depositGood(){
        bank.deposit(32);
        assertTrue(bank.getBalance() >= 50);
        assertEquals(82, bank.getBalance());
    }

    @Test
    public void depositNeg(){
        double depositValue = -32;
        assertFalse(depositValue > 0);
    }

    //Withdraws

    @Test
    public void withdrawGood(){
        bank.withdraw(32);
        assertTrue(bank.getBalance()  < 50);
        assertEquals(18, bank.getBalance());
    }

    @Test
    public void withdrawTooMuch(){
        bank.withdraw(100);
        assertFalse(bank.getBalance()  < 0);
    }

    @Test
    public void withdrawNeg(){
        double withdrawValue = -32;
        assertFalse(withdrawValue > 0);
    }


}
