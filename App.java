import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Scanner;


/**
 *
 * @author Nam Pham
 * @author Do Tran
 */
public class App {
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
     * Searches for a particular student using firstName and lastName
     *
     * @param firstName
     * @param lastName
     * @param age
     */
    private void searchStudent(String firstName, String lastName, int age) {
        String sql;
        String newFirstName = "%" + firstName + "%";
        String newLastName = "%" + lastName + "%";
        connect("Student.db"); //connects to Student database

        //prepare sql query
        if(age <= -1) {
            sql = "SELECT ID, firstName, lastName FROM Student WHERE firstName LIKE ? AND lastName LIKE ?";
        } else{
            Calendar today = Calendar.getInstance();
            int year = today.get(Calendar.YEAR) - age;
            sql = String.format("SELECT ID, firstName, lastName FROM Student " +
                    "WHERE firstName LIKE ? AND lastName LIKE ? AND dob LIKE '%d'", year);
        }

        try {
            //Prevents SQL Injection
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, newFirstName);
            stm.setString(2, newLastName);

            ResultSet rs = stm.executeQuery();

            //print out result
            String repeatedLine = new String(new char[30]).replace('\0', '_');
            System.out.println(String.format(" %30s %30s", repeatedLine, repeatedLine));
            System.out.println(String.format("|%-30s|%-30s|","First Name","Last Name"));
            System.out.println(String.format(" %30s %30s", repeatedLine, repeatedLine));
            while (rs.next()){
                String str = String.format("|%-30s|%-30s|", rs.getString(firstName), rs.getString(lastName));
                System.out.println(str);
                System.out.println(String.format(" %30s %30s", repeatedLine, repeatedLine));
            }
        } catch (SQLException se) {se.printStackTrace();}
        finally {
            try{
                conn.close();
            } catch(SQLException se){
                se.printStackTrace();
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
    private void recordActivities(String sID, int sweekNo, String sType) {
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
    private void recordAttendance(int classNo, String studentID, String sID){
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
    private void addStudent(String ID, String firstName, String lastName,
                            LocalDate dob, String classID){
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
    private void recordWorkDone(String workID, String studentID, LocalDate date){
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

    public static String toSha256(String input){
        String ans = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger temp = new BigInteger(1, bytes);
            StringBuilder byte2Hex = new StringBuilder(temp.toString(16));
            while (byte2Hex.length() < 32){
                byte2Hex.insert(0, '0');
            }
            ans = byte2Hex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static String addSalt(String input){
        return String.format("spicy%sspace", input);
    }

    private boolean logIn(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Username: ");
        String userName = scan.nextLine();
        System.out.print("Password: ");
        String pass = toSha256(addSalt(scan.nextLine()));

        connect("Staff.db");
        String query = "SELECT COUNT(*) AS result FROM Staff WHERE staffID = ? AND pass = ?";
        try {
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setString(1, userName);
            stm.setString(2, pass);

            ResultSet rs = stm.executeQuery();
            if (rs.getInt(1) == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void searchStudentInterface(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Student searching:\n(Press Enter if you don't have required information)");
        System.out.print("First name: ");
        String firstName = scan.nextLine();
        System.out.println();
        System.out.print("Last name: ");
        String lastName = scan.nextLine();
        System.out.println();
        boolean check = false;
        int age = -1;
        while (!check){
            System.out.print("Age: ");
            String ageTemp = scan.nextLine();
            try {
                age = Integer.parseInt(ageTemp);
                check = true;
            }catch (Exception e){
                System.err.println("Invalid input. Re-input");
            }
        }
        System.out.println();
        searchStudent(firstName, lastName, age);
    }

    private void addStudentInterface(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Student adding:\n");
        System.out.print("ID: ");
        String id = scan.nextLine();
        System.out.println();

        System.out.print("First name: ");
        String firstName = scan.nextLine();
        System.out.println();

        System.out.print("Last name: ");
        String lastName = scan.nextLine();
        System.out.println();

        System.out.print("Class: ");
        String classID = scan.nextLine();
        System.out.println();

        boolean check = false;
        LocalDate dob = null;
        while (!check){
            System.out.print("Date of birth (yyyy-mm-yy): ");
            String dobString = scan.nextLine();
            try {
                dob = LocalDate.parse(dobString);
                check = true;
            }catch (Exception e){
                System.err.println("Invalid input. Re-input");
            }
        }
        System.out.println();

        System.out.print(String.format("New student:\n" +
                "ID: %s\n" +
                "First name: %s\n" +
                "Last name: %s \n" +
                "Date of birth: %s\n" +
                "Class: %s\n", id, firstName, lastName, dob.toString(), classID));

        System.out.print("Type 'Y' to proceed (Anything else to abort): ");
        String isProceed = scan.nextLine().toUpperCase();
        if (isProceed == "Y"){
            addStudent(id, firstName, lastName, dob, classID);
        }
    }

    private void recordActivitiesInterface(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Session content recording:\n");
        System.out.print("Session ID: ");
        String sID = scan.nextLine();
        System.out.println();

        System.out.print("Session Type: ");
        String sType = scan.nextLine();
        System.out.println();

        boolean check = false;
        int weekNo = -1;
        while (!check){
            System.out.print("Week: ");
            String weekTemp = scan.nextLine();
            try {
                weekNo = Integer.parseInt(weekTemp);
                check = true;
            }catch (Exception e){
                System.err.println("Invalid input. Re-input");
            }
        }
        System.out.println();
        System.out.print(String.format("Session content record:\n" +
                "Session ID: %s\n" +
                "Week: %d\n" +
                "Session Type: %s\n", sID, weekNo, sType));

        System.out.print("Type 'Y' to proceed (Anything else to abort): ");
        String isProceed = scan.nextLine().toUpperCase();
        if (isProceed == "Y"){
            recordActivities(sID, weekNo, sType);
        }
    }

    private void recordWorkDoneInterface(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Achievement recording:\n");
        System.out.print("Part Done: ");
        String partID = scan.nextLine();
        System.out.println();

        System.out.print("Student ID: ");
        String studentID = scan.nextLine();
        System.out.println();

        boolean check = false;
        LocalDate date = null;
        while (!check){
            System.out.print("Date of birth (yyyy-mm-yy): ");
            String dateString = scan.nextLine();
            try {
                date = LocalDate.parse(dateString);
                check = true;
            }catch (Exception e){
                System.err.println("Invalid input. Re-input");
            }
        }
        System.out.println();

        System.out.print(String.format("Achievement record:\n" +
                "Part Done: %s\n" +
                "Student ID: %s\n" +
                "Date: %s\n", partID, studentID, date.toString()));

        System.out.print("Type 'Y' to proceed (Anything else to abort): ");
        String isProceed = scan.nextLine().toUpperCase();
        if (isProceed == "Y"){
            recordWorkDone(partID, studentID, date);
        }
    }

    private void recordAttendanceInterface(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Attendance recording:\n");
        System.out.print("Student ID: ");
        String studentID = scan.nextLine();
        System.out.println();

        System.out.print("Session ID: ");
        String sID = scan.nextLine();
        System.out.println();

        boolean check = false;
        int classNo = -1;
        while (!check){
            System.out.print("Class: ");
            String classTemp = scan.nextLine();
            try {
                classNo = Integer.parseInt(classTemp);
                check = true;
            }catch (Exception e){
                System.err.println("Invalid input. Re-input");
            }
        }
        System.out.println();
        System.out.print(String.format("Session content record:\n" +
                "Class: %d\n" +
                "Student ID: %s\n" +
                "Session ID: %s\n", classNo, studentID, sID));

        System.out.print("Type 'Y' to proceed (Anything else to abort): ");
        String isProceed = scan.nextLine().toUpperCase();
        if (isProceed == "Y"){
            recordAttendance(classNo, studentID, sID);
        }
    }

    public void run(){
        int choice = -1;
        boolean checkInput = false;
        System.out.println("Welcome to Cyber Security Course Admin App ");
        while (logIn() && choice != 6){
            Scanner scan = new Scanner(System.in);
            //read user input
            while(!checkInput) {
                System.out.println("1. Add a student\n" +
                        "2. Search for a student\n" +
                        "3. Plan/Record a session's content\n" +
                        "4. Record students' achievement\n" +
                        "5. Record students' attendance\n" +
                        "6. Exit");
                try {
                    choice = scan.nextInt();
                    scan.nextLine();
                    checkInput = true;
                } catch (Exception e){
                    System.err.println("Invalid input. Re-input");
                }
            }
            switch (choice){
                case 1:
                    addStudentInterface();
                    break;
                case 2:
                    searchStudentInterface();
                    break;
                case 3:
                    recordActivitiesInterface();
                    break;
                case 4:
                    recordWorkDoneInterface();
                    break;
                case 5:
                    recordAttendanceInterface();
                    break;
                case 6:
                    System.out.println("Logging out");
                    scan.nextLine();
                    break;
            }
        }

    }
    public static void main(String[] args){
        App test = new App();
        while (true){
            test.run();
        }
    }
}
