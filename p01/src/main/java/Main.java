import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    //Used for keeping track of where you are in arraylist
    static int index;
    private static Connection conn = null;

    //To establish a new connection if one doesn't exist
    public static Connection getConnection() {

        if (conn == null) {

            Properties props = new Properties();
            try {
                //???Important
                props.load(Main.class.getClassLoader().getResourceAsStream("connection.properties"));

                //Information is stored in a file, instead of hard coded in
                String endpoint = props.getProperty("endpoint");
                String url = "jdbc:postgresql://" + endpoint + "/postgres";
                String username = props.getProperty("username");
                String password = props.getProperty("password");

                //Where the connection is established
                conn = DriverManager.getConnection(url, username, password);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

        return conn;
    }

    public static void main(String[] args) {
        startUp();
    }

    private static void startUp() {
        //Sets up connection
        Connection connection = getConnection();

        System.out.println("Welcome to Corporate Bank!\n");

        //Creates a custom ArrayList
        MyArrayList user = new MyArrayList(); //modified version

        //The following code will go and fetch the rows in the bank table
        String sql = "SELECT * FROM bank";

        try {
            //Set up
            Statement statement = connection.createStatement();

            //The results of your queries
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                //Finds the appropriate columns info
                int id = result.getInt("bank_id");
                String userName = result.getString("user_name");
                String password = result.getString("user_password");
                double balance = result.getInt("balance");

                //Creates a new user that is stored in the MyArrayList
                user.add(new UserCredentials(userName, password, new Bank(balance)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Declares new Scanner
        Scanner scan = new Scanner(System.in);

        //Loops through until exit. Allows you to choose from various options.
        while(true){
            System.out.println("Choose between the following options: \n" +
                    "l - Login" + "\n" +
                    "c - Create New User" + "\n"  +
                    "e - Exit");
            //Gets first response
            String response = scan.nextLine();
            switch(response) {
                case "l":
                    //Checks to see if password and username match up
                    if(authenticate(getInfo(), user)){
                        accessAccount(user.get(index).getAccount());
                    }
                    else{
                        System.out.println("Password or username doesn't match");
                    }
                    continue;
                case "c":
                    //Gets and stores object
                    String[] info = getInfo();

                    //Adds users and creates new banks account
                    user.add(new UserCredentials(info[0], info[1], new Bank(50.00)));

                    //The following code adds a new row to the database
                    sql = "INSERT INTO bank VALUES (default,?,?,?) RETURNING *";

                    try {
                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setString(1, info[0]);
                        ps.setString(2, info[1]);
                        ps.setDouble(3, 50);
                        ps.executeQuery();
                    } catch (SQLException e) {
                        System.out.println("User-name already exists.");
                    }
                    continue;

                case "e":
                    //Allows you to escape this loop.
                    break;
                default:
                    continue;
            }

            break;
        }
        scan.close();

        //Make sure you're updating the right table!!!!!!
        sql = "UPDATE bank set user_name=?, user_password=?, balance=? WHERE bank_id = ? RETURNING *";

        //Updates Database
        try {

            for(int i = 0; i < user.size(); i++){
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, user.get(i).getName());
                ps.setString(2, user.get(i).getPassword());
                ps.setDouble(3, user.get(i).getAccount().getBalance());
                ps.setInt(4, i + 1);
                ps.executeQuery();;
            }

        } catch (SQLException e) {
            System.out.println("Failed to connect.");
        }



        //Say Good bye
        System.out.println("\nThank you, for using Corporate Bank");

        //Ends connection
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Includes you basic banking function
    private static void accessAccount(Bank access) {
        Scanner scan = new Scanner(System.in);
        while(true){
            //Prompt
            System.out.println("Choose between the following options: \n" +
                    "v - View balance" + "\n" +
                    "d - Deposit" + "\n"  +
                    "w - Withdraw" + "\n"  +
                    "a - Add other account" + "\n"  +
                    "e - Exit");

            //Gets first response
            String response = scan.nextLine();
            switch(response) {
                case "v":
                    //Work on formatting this correctly
                    System.out.println("Your balance is: " + access.getBalance());
                    continue;
                case "d":
                    deposit(access);
                    continue;
                case "w":
                    withdraw(access);
                    continue;
                case "a":
                    System.out.println("This area is currently, under construction :)");
                    continue;
                case "e":
                    break;
                default:
                    continue;
            }

            break;
        }
    }

    //Takes money out of your account
    private static void withdraw(Bank access) {

        Scanner scan = new Scanner(System.in);
        while(true){
            System.out.println("How much would you like to withdraw?");
            //look into try catch
            double result = 0;
            try{
                result = Double.parseDouble(scan.nextLine());

                //Checks if you have money in your account
                if(result >= 0){
                    access.withdraw(result);
                }
                else{
                    System.out.println("Can not input negative numbers");
                }
            }
            catch(Exception e){
                System.out.println("Invalid input");
            }
            break;
        }

    }

    //Puts money into your account
    private static void deposit(Bank access) {
        Scanner scan = new Scanner(System.in);
        while(true){
            System.out.println("How much would you like to deposit?");
            //look into try catch
            double result = 0;
            try{
                result = Double.parseDouble(scan.nextLine());

                if(result >= 0){
                    access.deposit(result);
                }
                else{
                    System.out.println("Can not input negative numbers");
                }
            }
            catch(Exception e){
                System.out.println("Invalid input");
            }
            break;
        }

    }

    //Tells you if the username and password match with what's in the system
    private static boolean authenticate(String[] response, MyArrayList user) {
        for(int i = 0; i < user.size(); i++){
            if(user.get(i).getName().equals(response[0]) && user.get(i).getPassword().equals(response[1])){
                // index = user.indexOf(u);
                index = i;
                return true;
            }
        }
        return false;
    }

    //Has user enter in name and password
    private static String[] getInfo() {
        //Declare String array for username and password
        String[] info = new String[2];

        //Declare scanner
        Scanner scan = new Scanner(System.in);
        //Get username
        System.out.print("Enter an user-name? ");
        String username = scan.nextLine();
        info[0] = username;

        //Get password
        System.out.print("Enter in a password? ");
        String password = scan.nextLine();
        info[1] = password;

        //Return String array
        //scan.close(); //This bit of code was causing there to be a : NoSuchElementException Error
        return info;
    }
}

