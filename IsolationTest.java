import java.sql.*;
import java.util.*;
import java.io.*;
public class IsolationTest{
    static public void read_stm (Statement read, String stm) throws SQLException{
        ResultSet rs = read.executeQuery(stm);
        System.out.println("Reading from the database with stament: " + stm );
        System.out.print("Result read: ");
        while(rs.next()){
            System.out.println(rs.getInt(1));
        }
    }
    static public void write_stm (Statement write, int val) throws SQLException{
        String stm = "UPDATE UNREPEATABLE SET  data = " + val;
        write.executeUpdate(stm);
        System.out.println("Writing to the database with stament: " + stm );
    }
    public static void main(String[] args){
        //final String sqliteConnection = "jdbc:sqlite:";
        final String mysqlConnection = "jdbc:mysql://localhost";
        final String mysqlUsername = "root";
        final String mysqlPassword = "CS157bmysql";

        Connection readCon = null;
        Connection writeCon = null;
        //READ COMMITED ISOLATION WITH executeUpdate
        String read_commited_stmt = "SET TRANSACTION ISOLATION LEVEL READ COMMITTED";
        //SERIALIZABLE ISOLATION WITH executeUpdate
        String serializable_stmt = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE";
        String[] stmts = {read_commited_stmt, serializable_stmt};
        String connectToDB = "USE ISOLATION_TEST";
        String insert = "INSERT INTO UNREPEATABLE VALUES (15)";
        for(int i = 0; i < 2; i++){
            try{
                readCon = DriverManager.getConnection(mysqlConnection, mysqlUsername, mysqlPassword);
                writeCon = DriverManager.getConnection(mysqlConnection, mysqlUsername, mysqlPassword);
                
                FileInputStream dbFile = new FileInputStream("MakeDB.txt");
                Scanner sc = new Scanner(dbFile);
                String createDBStmt = "";
                String createTableStmt = "";
                while(sc.hasNextLine()){
                    createDBStmt = sc.nextLine();
                    createTableStmt = sc.nextLine();
                }
                sc.close();
                Statement createDB = readCon.createStatement();
                createDB.execute(createDBStmt);
                createDB.execute(connectToDB);
                createDB.execute(createTableStmt);
                createDB.executeUpdate(insert);
                //Isolation level is set here, 0 is read commited, 1 is serializable
                createDB.executeUpdate(stmts[i]);
                //Interleaving the two transactions
                Statement read = readCon.createStatement();
                Statement write = writeCon.createStatement();
                String stm = "SELECT * FROM UNREPEATABLE";
                read_stm(read, stm);
                write_stm(write, 19);
                read_stm(read, stm);
                write_stm(write,157);
                read_stm(read, stm);
                
                //Droping the database for the next experiment
                String dbName = createDBStmt.split(" ")[2];
                String dropDBStm = "DROP DATABASE " + dbName;
                Statement dropDB = writeCon.createStatement();
                dropDB.execute(dropDBStm);
                //Next have your program close and reestablish the two connections
                readCon.close();
                writeCon.close();
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }
}