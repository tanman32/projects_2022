public class UserCredentials {
    //Basic Pojo

    private String name;
    private String password;
    private Bank account;

    public UserCredentials(String name, String password, Bank account) {
        this.account = account;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Bank getAccount() {
        return account;
    }
}
