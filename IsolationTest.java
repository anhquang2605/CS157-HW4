import java.sql.*;
import java.util.*;
public class IsolationTest{
    public static void main(String[] args){
        final String sqliteConnection = "jdbc:sqlite:";
        final String mysqlConnection = "jdbc:mysql://localhost";
        final String mysqlUsername = "root";
        final String mysqlPassword = "CS157bmysql";

        Connection con1 = null;
        Connection con2 = null;
        try{
            con1 = DriverManager.getConnection(mysqlConnection, mysqlUsername, mysqlPassword);
            con2 = DriverManager.getConnection(mysqlConnection, mysqlUsername, mysqlPassword);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}