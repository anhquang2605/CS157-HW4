import java.sql.*;
import java.util.*;
import java.io.*;
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

            ResultSet dbNames = con1.getMetaData().getCatalogs();
            boolean dbExists = false;
            while(dbNames.next()){
                String existingDB = dbNames.getString(1);
                if("ISOLATION_TEST".equals(existingDB)){
                    dbExists = true;
                }
            }
            dbNames.close();
            if(!dbExists){
                FileInputStream dbFile = new FileInputStream("MakeDB.txt");
                Scanner sc = new Scanner(dbFile);
                String createDBStmt = "";
                String createTableStmt = "";
                while(sc.hasNextLine()){
                    createDBStmt = sc.nextLine();
                    createTableStmt = sc.nextLine();
                }
                //Statement createDB = con1.createStatement();
                //createDB.execute(createDBStmt);
            }
            con1.close();
            con2.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}