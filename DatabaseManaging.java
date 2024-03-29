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
        connect("Student.db");
        List<String> queries = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();

            //Creates a record of each student
            queries.add("CREATE TABLE Student(ID VARCHAR(250) PRIMARY KEY," +
                    "firstName VARCHAR(250) NOT NULL," +
                    "lastName VARCHAR(250) NOT NULL," +
                    "DOB Date NOT NULL," +
                    "class VARCHAR(250) NOT NULL," +
                    "badgeRank VARCHAR(250) DEFAULT 'Unranked')");

            // Creates a record of badges achieved
            queries.add("CREATE TABLE BadgeList(badgeID VARCHAR(250) NOT NULL," +
                    "studentID VARCHAR(250) NOT NULL," +
                    "FOREIGN KEY (studentID) REFERENCES Student(ID) " +
                    "ON DELETE CASCADE ON UPDATE CASCADE," +
                    "CONSTRAINT BadgeList_Key PRIMARY KEY(badgeID, studentID))");
            // Creates a record of tests done
            queries.add("CREATE TABLE TestDone(testID VARCHAR(250) NOT NULL," +
                    "studentID VARCHAR(250) NOT NULL," +
                    "FOREIGN KEY (studentID) REFERENCES Student(ID) " +
                    "ON DELETE CASCADE ON UPDATE CASCADE," +
                    "CONSTRAINT TestDone_Key PRIMARY KEY(testID, studentID))");
            // Creates a record of topics done
            queries.add("CREATE TABLE TopicDone(topicID VARCHAR(250) NOT NULL," +
                    "studentID VARCHAR(250) NOT NULL," +
                    "date DATE NOT NULL," +
                    "FOREIGN KEY (studentID) REFERENCES Student(ID) " +
                    "ON DELETE CASCADE ON UPDATE CASCADE," +
                    "CONSTRAINT TopicDone_Key PRIMARY KEY(topicID, studentID))");
            // Creates a record of parts done
            queries.add("CREATE TABLE PartDone(partID VARCHAR(250) NOT NULL," +
                    "studentID VARCHAR(250) NOT NULL," +
                    "date DATE NOT NULL," +
                    "FOREIGN KEY (studentID) REFERENCES Student(ID) " +
                    "ON DELETE CASCADE ON UPDATE CASCADE," +
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
        connect("Student.db");
        List<String> queries = new ArrayList<>();
        try {
            Statement stm = conn.createStatement();

            // Creates a record of testID and tests' name
            queries.add("CREATE TABLE Test(testID VARCHAR(250) PRIMARY KEY," +
                    "testName VARCHAR(250) NOT NULL)");
            // Creates a record of topicID and topics' name
            queries.add("CREATE TABLE Topic(topicID VARCHAR(250) PRIMARY KEY," +
                    "topicName VARCHAR(250) NOT NULL)");
            // Creates a record of each available badges
            queries.add("CREATE TABLE Badge(badgeID VARCHAR(250) PRIMARY KEY," +
                    "badgeName VARCHAR(250) NOT NULL)");

            // Creates a record of each session
            queries.add("CREATE TABLE Session(sID VARCHAR(250) PRIMARY KEY NOT NULL," +
                    "sWeekNo int NOT NULL," +
                    "sType VARCHAR(250) NOT NULL)");
            // Creates a record of what activities done in each session
            queries.add("CREATE TABLE Timetable(partID VARCHAR(250) NOT NULL," +
                    "sID VARCHAR(250) NOT NULL," +
                    "FOREIGN KEY (sID) REFERENCES Session(sID) ON DELETE CASCADE ON UPDATE CASCADE," +
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
        connect("Student.db");
        List<String> queries = new ArrayList<>();
        connect("Student.db");
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

    public void createTableINStaffDB(){
        connect("Student.db");
        String sql = "CREATE TABLE Staff(staffID VARCHAR(250) PRIMARY KEY," +
                "pass VARCHAR(250) NOT NULL)";
        try {
            Statement stm = conn.createStatement();
            stm.execute(sql);

            //create initial value for Staff table
            String defaultID = "root";
            String defaultPass = App.toSha256(App.addSalt("root"));
            sql = String.format("INSERT INTO Staff(staffID, pass) VALUES ('%s', '%s')",
                    defaultID, defaultPass);
            stm.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
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
        db.createTableINStaffDB();
        try {
            conn.close();
        } catch(SQLException es){};
    }
}
