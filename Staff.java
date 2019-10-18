import org.jetbrains.annotations.NotNull;

import java.sql.*;

/**
 *
 * @author Nam Pham
 * @author Do Tran
 */
public class Staff {
    private static Connection conn;
    private Statement stm;

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
     * Searches for a particular student using firstName and lastName
     *
     * @param firstName
     * @param lastName
     */
    public void searchStudent(String firstName, String lastName) {
        connect("Student.db"); //connects to Student database
        try {
            //print out details about the student
            String sql = "SELECT * FROM Student WHERE firstName = ? AND lastName = ?";

            //Prevents SQL Injection
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, firstName);
            stm.setString(2, lastName);

            ResultSet rs = stm.executeQuery();
            System.out.println(" _______________ _______________");
            System.out.println("|\tFirst Name\t|\tLast Name\t|");
            System.out.println("|_______________|_______________|");
            while (rs.next()){
                String str = String.format("|\t%s\t|\t%s\t", rs.getString(firstName), rs.getString(lastName));
                System.out.println(str);
                System.out.println("|_______________|_______________|");
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
     * Records what parts have been done in each session
     * @param sID
     * @param sweekNo
     * @param sType
     */
    public void recordActivities(String sID, int sweekNo, String sType ) {
        connect("Curriculum.db");
        try {
            String sql = "INSERT INTO Session(sID, sWeekNo, sType) " +
                    "VALUES(?,?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, sID);
            stm.setInt(2, sweekNo);
            stm.setString(3, sType);

            stm.execute();
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
     * Records student attendance in each session
     * @param classNo
     * @param studentID
     * @param sID
     */
    public void recordAttendance(int classNo, String studentID, String sID){
        connect("Attendance.db");
        try {
            String sql = String.format("INSERT INTO Class%d(studentID, sID) " +
                    "VALUES(?,?)", classNo);
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, studentID);
            stm.setString(2, sID);

            stm.execute();
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
     * Records each student
     * @param ID
     * @param firstName
     * @param lastName
     * @param dob
     * @param classID
     */
    public void addStudent(String ID, String firstName, String lastName,
                           Date dob, String classID){
        connect("Student.db");
        try{
            String sql = "INSERT INTO Student(ID, firstName, lastName, dob, class) VALUES (?,?,?,?,?)";

            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, ID);
            stm.setString(2, firstName);
            stm.setString(3, lastName);
            stm.setString(4, dob.toString());
            stm.setString(5, classID);

            stm.execute();
        } catch (SQLException sql){sql.printStackTrace();}
    }

    /**
     * Records what part has been done by each student
     * @param workID
     * @param studentID
     * @param date
     */
    public void recordWorkDone(String workID, String studentID, Date date){
        connect("Student.db");
        try{
            String sql = "INSERT INTO WorkDone(workID, studentID, date) VALUES (?,?,?)";

            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, workID);
            stm.setString(2, studentID);
            stm.setString(3, date.toString());

            stm.execute();
        } catch (SQLException sql){sql.printStackTrace();}
    }

    public static void main(String[] args){

    }
}
