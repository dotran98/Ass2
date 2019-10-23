import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Structures required databases
 * @author Do Tran, Nam Pham
 */
public class DatabaseManaging {
    /**
     * Connection to the database
     */
    private static Connection conn;

    /**
     * Creates connection to a database
     * @param filename path to the database
     */
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

    /**
     * Structures Student database
     */
    public void createTableInStudentDB() {
        List<String> queries = new ArrayList<>();
        connect("Student.db"); //connects to Student database
        try {
            Statement stm = conn.createStatement();

            //Creates a record of each student
            queries.add("CREATE TABLE Student(ID VARCHAR(250) PRIMARY KEY," +
                    "firstName VARCHAR(250)," +
                    "lastName VARCHAR(250)," +
                    "DOB Date," +
                    "class VARCHAR(250)," +
                    "badgeRank VARCHAR(250) DEFAULT 'Unranked')");

            // Creates a record of badges achieved
            queries.add("CREATE TABLE BadgeList(badgeID VARCHAR(250)," +
                    "studentID VARCHAR(250)," +
                    "FOREIGN KEY (studentID) REFERENCES Student(ID)," +
                    "CONSTRAINT BadgeList_Key PRIMARY KEY(badgeID, studentID))");
            // Creates a record of tests done
            queries.add("CREATE TABLE TestDone(testID VARCHAR(250)," +
                    "studentID VARCHAR(250)," +
                    "FOREIGN KEY (studentID) REFERENCES Student(ID)" +
                    "CONSTRAINT TestDone_Key PRIMARY KEY(testID, studentID))");
            // Creates a record of topics done
            queries.add("CREATE TABLE TopicDone(topicID VARCHAR(250)," +
                    "studentID VARCHAR(250)," +
                    "FOREIGN KEY (studentID) REFERENCES Student(ID)" +
                    "CONSTRAINT TopicDone_Key PRIMARY KEY(topicID, studentID))");
            // Creates a record of parts done
            queries.add("CREATE TABLE PartDone(partID VARCHAR(250)," +
                    "studentID VARCHAR(250)," +
                    "date DATE," +
                    "FOREIGN KEY (studentID) REFERENCES Student(ID)," +
                    "CONSTRAINT PartDone_Key PRIMARY KEY(partID, studentID))");

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
     * Structures Curriculum database
     */
    public void createTableInCurriculumDB(){
        List<String> queries = new ArrayList<>();
        connect("Curriculum.db");
        try {
            Statement stm = conn.createStatement();
          
            // Creates a record of testID and tests' name
            queries.add("CREATE TABLE Test(testID VARCHAR(250) PRIMARY KEY," +
                    "testName VARCHAR(250))");
            // Creates a record of topicID and topics' name
            queries.add("CREATE TABLE Topic(topicID VARCHAR(250) PRIMARY KEY," +
                    "topicName VARCHAR(250))");
            // Creates a record of each available badges
            queries.add("CREATE TABLE Badge(badgeID VARCHAR(250) PRIMARY KEY," +
                    "badgeName VARCHAR(250))");

            // Creates a record of each session
            queries.add("CREATE TABLE Session(sID VARCHAR(250) PRIMARY KEY," +
                    "sWeekNo int," +
                    "sType VARCHAR(250))");
            // Creates a record of what activities done in each session
            queries.add("CREATE TABLE Timetable(partID VARCHAR(250)," +
                    "sID VARCHAR(250)," +
                    "FOREIGN KEY (sID) REFERENCES Session(sID)," +
                    "CONSTRAINT Timetable_Key PRIMARY KEY(partID, sID))");

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
     * Creates roll call record for each class
     * @param classNo number of classes
     */
    public void createTableInAttendanceDB(int classNo){
        List<String> queries = new ArrayList<>();
        connect("Attendance.db");
        try {
            Statement stm = conn.createStatement();
            String sql;
            //each class record is stored in 1 table
            for (int i = 1; i <= classNo; i++) {
                sql = String.format("CREATE TABLE Class%d(studentID VARCHAR(250) NOT NULL," +
                        "sID VARCHAR(250) NOT NULL," +
                        "CONSTRAINT Class%d_Key PRIMARY KEY(sID, studentID))", i, i);
                queries.add(sql);
            }
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
        db.createTableInAttendanceDB(5);
        db.createTableInStudentDB();
        db.createTableInCurriculumDB();
    }
}
