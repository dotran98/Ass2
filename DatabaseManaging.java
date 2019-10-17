import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;

public class DatabaseManaging {
    private static Connection conn;
    Statement stm;

    private static void connect(String filename) {
        try {
            // db parameters
            String url = String.format("jdbc:sqlite:%s", filename);
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            String mess = String.format("Connection to %s has been established.", filename);
            System.out.println(mess);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTableInStudentDB() {
        List<String> queries = new ArrayList<>();
        connect("Student.db");
        try {
            Statement stm = conn.createStatement();

            queries.add("CREATE TABLE Student(ID VARCHAR(250) PRIMARY KEY," +
                    "firstName VARCHAR(250)," +
                    "lastName VARCHAR(250)," +
                    "DOB Date," +
                    "class VARCHAR(250)," +
                    "badgeRank VARCHAR(250) DEFAULT 'N/A')");

            queries.add("CREATE TABLE WorkDone(workID VARCHAR(250) PRIMARY KEY," +
                    "studentID VARCHAR(250)," +
                    "date DATE," +
                    "FOREIGN KEY (studentID) REFERENCES Student(ID))");

            for (String s : queries){
                stm.execute(s);
            }
        } catch (SQLException sql) {sql.printStackTrace();}
        finally {
            try{
                conn.close();
            } catch(SQLException sql){
                sql.printStackTrace();
            }
        }
        System.out.println("Finished!");
    }

    public void createTableInCurriculumDB(){
        List<String> queries = new ArrayList<>();
        connect("Curriculum.db");
        try {
            Statement stm = conn.createStatement();

            queries.add("CREATE TABLE Test(testID VARCHAR(250) PRIMARY KEY," +
                    "topicID VARCHAR(250))");
            queries.add("CREATE TABLE Topic(topicID VARCHAR(250)," +
                    "partID VARCHAR(250) PRIMARY KEY," +
                    "FOREIGN KEY (topicID) REFERENCES Test(topicID))");
            queries.add("CREATE TABLE BadgeList(badgeID VARCHAR(250) PRIMARY KEY," +
                    "badgeType VARCHAR(250))");
            queries.add("CREATE TABLE BadgeComponent(badgeID VARCHAR(250)," +
                    "testID VARCHAR(250)," +
                    "FOREIGN KEY (testID) REFERENCES Test(testID))");
            queries.add("CREATE TABLE Session(sID VARCHAR(250) PRIMARY KEY," +
                    "sWeekNo INTEGER," +
                    "sType VARCHAR(250))");
            queries.add("CREATE TABLE Timetable(partID VARCHAR(250) PRIMARY KEY," +
                    "sID VARCHAR(250)," +
                    "FOREIGN KEY (sID) REFERENCES Session(sID)" +
                    "FOREIGN KEY (partID) REFERENCES Topic(partID))");

            for (String s : queries){
                stm.execute(s);
            }
        } catch (SQLException sql) {sql.printStackTrace();}
        finally {
            try{
                conn.close();
            } catch(SQLException sql){
                sql.printStackTrace();
            }
        }
        System.out.println("Finished!");
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DatabaseManaging db = new DatabaseManaging();
        db.createTableInStudentDB();
        db.createTableInCurriculumDB();
    }
}
