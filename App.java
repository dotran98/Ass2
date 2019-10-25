import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Scanner;


/**
 * @author Nam Pham
 * @author Do Tran
 */
public class App {

    private static Connection conn;

    /**
     * Creates connection to a database
     *
     * @param filename path to the database
     */
    private static void connect(String filename) {
        try {
            // db parameters
            String url = String.format("jdbc:sqlite:%s", filename);
            // create a connection to the database
            conn = DriverManager.getConnection(url);
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
        ResultSet rs = null;

        try {
            //prepare sql query
            if (age <= 0) {
                sql = "SELECT ID, firstName, lastName, DOB, class, badgeRank " +
                        "FROM Student WHERE firstName LIKE ? AND lastName LIKE ?";
                //Prevents SQL Injection
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1, newFirstName);
                stm.setString(2, newLastName);
                rs = stm.executeQuery();
            } else {
                Calendar today = Calendar.getInstance();
                int year = today.get(Calendar.YEAR) - age;
                sql = "SELECT ID, firstName, lastName, DOB, class, badgeRank FROM Student " +
                        "WHERE firstName LIKE ? AND lastName LIKE ? AND dob LIKE ?";
                //Prevents SQL Injection
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1, newFirstName);
                stm.setString(2, newLastName);
                stm.setString(3, Integer.toString(year) + "%");
                rs = stm.executeQuery();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        //print out result
        String repeatedLine = new String(new char[30]).replace('\0', '_');
        System.out.println(String.format(" %30s %30s %30s %30s %30s %30s",
                repeatedLine, repeatedLine, repeatedLine, repeatedLine, repeatedLine, repeatedLine));
        System.out.println(String.format("|%-30s|%-30s|%-30s|%-30s|%-30s|%-30s|",
                "ID", "First Name", "Last Name", "Date of Birth", "Class", "Badge Rank"));
        System.out.println(String.format("|%30s|%30s|%30s|%30s|%30s|%30s|",
                repeatedLine, repeatedLine, repeatedLine, repeatedLine, repeatedLine, repeatedLine));
        try {
            while (rs.next()) {
                String str = String.format("|%-30s|%-30s|%-30s|%-30s|%-30s|%-30s|",
                        rs.getString("ID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("DOB"),
                        rs.getInt("class"), rs.getString("badgeRank"));
                System.out.println(str);
                System.out.println(String.format(" %30s %30s %30s %30s %30s %30s",
                        repeatedLine, repeatedLine, repeatedLine, repeatedLine, repeatedLine, repeatedLine));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Records what parts have been done in each session
     *
     * @param sID
     * @param sweekNo
     * @param sType
     */
    private void recordActivities(String sID, int sweekNo, String sType) throws NullValueDetected {
        if (sID.equals("") || sType.equals("") || sweekNo < 1) throw new NullValueDetected();

        connect("Curriculum.db");
        try {
            String sql = "INSERT INTO Session(sID, sWeekNo, sType) " +
                    "VALUES(?,?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, sID);
            stm.setInt(2, sweekNo);
            stm.setString(3, sType);

            stm.execute();
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }
    }

    /**
     * Records student attendance in each session
     *
     * @param classNo
     * @param studentID
     * @param sID
     */
    private void recordAttendance(int classNo, String studentID, String sID) throws NullValueDetected {
        if (studentID.equals("") || sID.equals("") || classNo < 1) throw new NullValueDetected();

        connect("Attendance.db");
        try {
            String sql = String.format("INSERT INTO Class%d(studentID, sID) " +
                    "VALUES(?,?)", classNo);
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, studentID);
            stm.setString(2, sID);

            stm.execute();
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }
    }

    /**
     * Records each student
     *
     * @param ID
     * @param firstName
     * @param lastName
     * @param dob
     * @param classID
     */
    private void addStudent(String ID, String firstName, String lastName,
                            LocalDate dob, String classID) throws NullValueDetected {
        if (ID.equals("") || firstName.equals("") || lastName.equals("") || classID.equals("")) {
            throw new NullValueDetected();
        }

        connect("Student.db");
        try {
            String sql = "INSERT INTO Student(ID, firstName, lastName, dob, class) VALUES (?,?,?,?,?)";

            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, ID);
            stm.setString(2, firstName);
            stm.setString(3, lastName);
            stm.setString(4, dob.toString());
            stm.setString(5, classID);

            stm.execute();
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * update what badges have been achieved by each student
     *
     * @param badgeID
     * @param studentID
     */
    private void updateBadgeList(String badgeID, String studentID) throws NullValueDetected {
        if (badgeID.equals("") || studentID.equals("")) {
            throw new NullValueDetected();
        }

        connect("Student.db");
        try {
            String sql = "INSERT INTO BadgeList(badgeID, studentID) VALUES(?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, badgeID);
            stm.setString(2, studentID);

            stm.execute();

        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * update what tests has been done by each student
     *
     * @param testID
     * @param studentID
     */
    private void updateTestDone(String testID, String studentID) throws NullValueDetected {
        if (testID.equals("") || studentID.equals("")) {
            throw new NullValueDetected();
        }

        connect("Student.db");
        try {
            String sql = "INSERT INTO TestDone(testID, studentID) VALUES(?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, testID);
            stm.setString(2, studentID);

            stm.execute();

            // get the testID related to topicID
            int lastIndexDot = testID.lastIndexOf('.');
            String badgeID = testID.substring(0, lastIndexDot);

            sql = "SELECT COUNT(testID) FROM TestDone WHERE studentID = ? AND testID LIKE ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, studentID);
            stm.setString(2, badgeID + "%");

            // get the number of tests done by the student in this above badge
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.getInt("COUNT(testID)") >= 10) {

                // get testID of tests done by the student
                sql = "SELECT testID FROM TestDone WHERE studentID = ? AND testID LIKE ?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, studentID);
                stm.setString(2, badgeID + "%");
                resultSet = stm.executeQuery();

                String ID; // ID is the testID
                int lastIndex, testNum;
                // check whether the tests 1-7 is done
                boolean[] checkTest = new boolean[15];
                while (resultSet.next()) {
                    ID = resultSet.getString("testID");
                    lastIndex = ID.lastIndexOf('.');
                    // get the test's number
                    testNum = Integer.parseInt(ID.substring(lastIndex + 1));
                    checkTest[testNum] = true;
                }
                boolean compulsoryTestsComplete = true;
                for (int i = 1; i <= 7; i++)
                    if (!checkTest[i]) {
                        // one of the compulsory tests is not done
                        compulsoryTestsComplete = false;
                        break;
                    }
                // the number of tests done is greater than 10
                // all compulsory tests are completed
                if (compulsoryTestsComplete) {
                    conn.close();
                    updateBadgeList(badgeID, studentID);
                }
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * update what topics has been done by each student
     *
     * @param topicID
     * @param studentID
     * @param date
     */
    private void updateTopicDone(String topicID, String studentID, LocalDate date) throws NullValueDetected {
        if (topicID.equals("") || studentID.equals("")) {
            throw new NullValueDetected();
        }

        connect("Student.db");
        try {
            String sql = "INSERT INTO TopicDone(topicID, studentID, date) VALUES(?,?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, topicID);
            stm.setString(2, studentID);
            stm.setString(3, date.toString());

            stm.execute();

            // get the testID related to topicID
            int lastIndexDot = topicID.lastIndexOf('.');
            String testID = topicID.substring(0, lastIndexDot);

            sql = "SELECT COUNT(topicID) FROM TopicDone WHERE studentID = ? AND topicID LIKE ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, studentID);
            stm.setString(2, testID + "%");

            // get the number of topics done by the student in this above topic
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.getInt("COUNT(topicID)") == 3) {
                conn.close();
                updateTestDone(testID, studentID);
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Records what part has been done by each student
     *
     * @param workID
     * @param studentID
     * @param date
     */
    private void recordWorkDone(String workID, String studentID, LocalDate date) throws NullValueDetected {
        if (workID.equals("") || studentID.equals("")) {
            throw new NullValueDetected();
        }

        connect("Student.db");
        try {
            String sql = "INSERT INTO PartDone(partID, studentID, date) VALUES (?,?,?)";

            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, workID);
            stm.setString(2, studentID);
            stm.setString(3, date.toString());

            stm.execute();

            // get the topicID related to partID
            int lastIndexDot = workID.lastIndexOf('.');
            String topicID = workID.substring(0, lastIndexDot);


            sql = "SELECT COUNT(partID) FROM PartDone WHERE studentID = ? AND partID LIKE ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, studentID);
            stm.setString(2, topicID + "%");

            // get the number of parts done by the student in this above topic
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.getInt("COUNT(partID)") == 3) {
                conn.close();
                updateTopicDone(topicID, studentID, date);
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public static String toSha256(String input) {
        String ans = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger temp = new BigInteger(1, bytes);
            StringBuilder byte2Hex = new StringBuilder(temp.toString(16));
            while (byte2Hex.length() < 32) {
                byte2Hex.insert(0, '0');
            }
            ans = byte2Hex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static String addSalt(String input) {
        return String.format("spicy%sspace", input);
    }

    private boolean logIn() {
        Scanner scan = new Scanner(System.in);

        System.out.print("Username: ");
        String userName = scan.nextLine();
        System.out.print("Password: ");
        String password = scan.nextLine();
        String pass = toSha256(addSalt(password));

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

    private void searchStudentInterface() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Student searching:\n(Press Enter if you don't have required information)");
        System.out.print("First name: ");
        String firstName = scan.nextLine();
        System.out.print("Last name: ");
        String lastName = scan.nextLine();
        boolean check = false;
        int age = -1;
        while (!check) {
            System.out.print("Age: ");
            String ageTemp = scan.nextLine();
            if (ageTemp.equals("")) check = true;
            else {
                try {
                    age = Integer.parseInt(ageTemp);
                    check = true;
                } catch (Exception e) {
                    System.err.println("Invalid input. Re-input");
                }
            }
        }
        searchStudent(firstName, lastName, age);
        System.out.println("Finished");
    }

    private void addStudentInterface() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Student adding:");
        System.out.print("ID: ");
        String id = scan.nextLine();

        System.out.print("First name: ");
        String firstName = scan.nextLine();

        System.out.print("Last name: ");
        String lastName = scan.nextLine();

        System.out.print("Class: ");
        String classID = scan.nextLine();

        boolean check = false;
        LocalDate dob = null;
        while (!check) {
            System.out.print("Date of birth (yyyy-mm-dd): ");
            String dobString = scan.nextLine();
            try {
                dob = LocalDate.parse(dobString);
                check = true;
            } catch (Exception e) {
                System.err.println("Invalid input. Re-input");
            }
        }

        System.out.print(String.format("New student:\n" +
                "ID: %s\n" +
                "First name: %s\n" +
                "Last name: %s \n" +
                "Date of birth: %s\n" +
                "Class: %s\n", id, firstName, lastName, dob.toString(), classID));

        System.out.print("Type 'Y' to proceed (Anything else to abort): ");
        String isProceed = scan.nextLine().toUpperCase();
        if (isProceed.equals("Y")) {
            try {
                addStudent(id, firstName, lastName, dob, classID);
                System.out.println("Finished");
            } catch (NullValueDetected nul) {
                System.err.println(nul.getMessage());
            }
        }
    }

    private void recordActivitiesInterface() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Session content recording:");
        System.out.print("Session ID: ");
        String sID = scan.nextLine();

        System.out.print("Session Type: ");
        String sType = scan.nextLine();

        boolean check = false;
        int weekNo = -1;
        while (!check) {
            System.out.print("Week: ");
            String weekTemp = scan.nextLine();
            try {
                weekNo = Integer.parseInt(weekTemp);
                check = true;
            } catch (Exception e) {
                System.err.println("Invalid input. Re-input");
            }
        }
        System.out.print(String.format("Session content record:\n" +
                "Session ID: %s\n" +
                "Week: %d\n" +
                "Session Type: %s\n", sID, weekNo, sType));

        System.out.print("Type 'Y' to proceed (Anything else to abort): ");
        String isProceed = scan.nextLine().toUpperCase();
        if (isProceed.equals("Y")) {
            try {
                recordActivities(sID, weekNo, sType);
                System.out.println("Finished");
            } catch (NullValueDetected nul) {
                System.err.println(nul.getMessage());
            }
        }
    }

    private void recordWorkDoneInterface() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Achievement recording:");
        System.out.print("Part Done: ");
        String partID = scan.nextLine();

        System.out.print("Student ID: ");
        String studentID = scan.nextLine();

        boolean check = false;
        LocalDate date = null;
        while (!check) {
            System.out.print("Date (yyyy-mm-dd): ");
            String dateString = scan.nextLine();
            try {
                date = LocalDate.parse(dateString);
                check = true;
            } catch (Exception e) {
                System.err.println("Invalid input. Re-input");
            }
        }

        System.out.print(String.format("Achievement record:\n" +
                "Part Done: %s\n" +
                "Student ID: %s\n" +
                "Date: %s\n", partID, studentID, date.toString()));

        System.out.print("Type 'Y' to proceed (Anything else to abort): ");
        String isProceed = scan.nextLine().toUpperCase();
        if (isProceed.equals("Y")) {
            try {
                recordWorkDone(partID, studentID, date);
                System.out.println("Finished");
            } catch (NullValueDetected nul) {
                System.err.println(nul.getMessage());
            }
        }
    }

    private void recordAttendanceInterface() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Attendance recording:");
        System.out.print("Student ID: ");
        String studentID = scan.nextLine();

        System.out.print("Session ID: ");
        String sID = scan.nextLine();

        boolean check = false;
        int classNo = -1;
        while (!check) {
            System.out.print("Class: ");
            String classTemp = scan.nextLine();
            try {
                classNo = Integer.parseInt(classTemp);
                check = true;
            } catch (Exception e) {
                System.err.println("Invalid input. Re-input");
            }
        }
        System.out.print(String.format("Session content record:\n" +
                "Class: %d\n" +
                "Student ID: %s\n" +
                "Session ID: %s\n", classNo, studentID, sID));

        System.out.print("Type 'Y' to proceed (Anything else to abort): ");
        String isProceed = scan.nextLine().toUpperCase();
        if (isProceed.equals("Y")) {
            try {
                recordAttendance(classNo, studentID, sID);
                System.out.println("Finished");
            } catch (NullValueDetected nul) {
                System.err.println(nul.getMessage());
            }
        }
    }

    public void run() {
        int choice = -1;
        boolean checkInput = false;
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Cyber Security Course Admin App ");
        boolean isLogIn = logIn();
        while (isLogIn && choice != 6) {
            //read user input
            while (!checkInput) {
                System.out.println("1. Add a student\n" +
                        "2. Search for a student\n" +
                        "3. Plan/Record a session's content\n" +
                        "4. Record students' achievement\n" +
                        "5. Record students' attendance\n" +
                        "6. Log out\n" +
                        "7. Shut down\n");
                try {
                    choice = scan.nextInt();
                    scan.nextLine();
                    checkInput = true;
                } catch (Exception e) {
                    System.err.println("Invalid input. Re-input");
                }
            }
            switch (choice) {
                case 1:
                    addStudentInterface();
                    checkInput = false;
                    break;
                case 2:
                    searchStudentInterface();
                    checkInput = false;
                    break;
                case 3:
                    recordActivitiesInterface();
                    checkInput = false;
                    break;
                case 4:
                    recordWorkDoneInterface();
                    checkInput = false;
                    break;
                case 5:
                    recordAttendanceInterface();
                    checkInput = false;
                    break;
                case 6:
                    System.out.println("Logging out");
                    scan.nextLine();
                    break;
                default:
                    System.out.println("Shut down");
                    System.exit(0);
            }
        }

    }

    public static void main(String[] args) {

        App test = new App();
        while (true) {
            test.run();
        }

    }
}