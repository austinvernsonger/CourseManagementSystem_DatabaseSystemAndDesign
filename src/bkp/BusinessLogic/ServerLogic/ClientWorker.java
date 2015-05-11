package BusinessLogic.ServerLogic;
/**
 * Created by dyao on 10/21/14.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;

public class ClientWorker extends HttpServlet implements Runnable{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String dbURL = "jdbc:mysql://localhost/Academy";
    static final String subjectTable = "Subject";
    static final String subjectID = "subjectID";
    static final String preReqTable = "Prerequisite";
    static final String preReqTable_subjID = "subjectID";
    static final String preReqTable_prerqID = "prereqID";
    static final String enrolTable = "Enrollment";
    static final String enrolTable_studentID = "studentID";
    static final String enrolTable_subjectID = "subjectID";
    static final String enrolTable_grade = "Grade";

    private String userID;
    private String courseToEnroll;

    private Socket cSocket;

    Connection conn = null;
    ResultSet result = null;


    public ClientWorker(Socket cSocket, Connection dbConnect, String userID){
        this.cSocket = cSocket;
        this.conn = dbConnect;
        this.userID = userID;
    }

    @Override

    public void run(){
      //  keep listening for client's query

        String userInputData;
        try {
            DataInputStream dataIn = new DataInputStream(cSocket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(cSocket.getOutputStream());
            while(true){
                userInputData = dataIn.readUTF();
                if(parseStudentQuery(userInputData)==false){
                    dataOut.writeUTF("Fail to parse request.");
                }else{
                    boolean isPrereqSatisfied = false;

                    if(preReqFinder(courseToEnroll)==null){
                        dataOut.writeUTF("Course not available: " + courseToEnroll);
                    }else{
                        isPrereqSatisfied = preReqChecker(Integer.parseInt(userID), preReqFinder(courseToEnroll));
                        if(isPrereqSatisfied==true){

                            if(enrollCourse(Integer.parseInt(userID), courseToEnroll)==true){
                                dataOut.writeUTF("Course registered: "+courseToEnroll);
                            }else{
                                dataOut.writeUTF("Unable to register course: "+courseToEnroll);
                            }
                        }else{
                            dataOut.writeUTF("Prerequisite condition not satisfied.");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // return null if course is not available
    // return return prerequisite result otherwise
    private ResultSet preReqFinder(String courseID){
        String myQuery = null;

        try {
            // check if the course is available for student to choose
            myQuery = "select * from "+subjectTable+" where "+subjectID+"=\""+courseID+"\"";
            result = conn.createStatement().executeQuery(myQuery);

            if(result.next()==false){// course not available in the subject table
                return null;
            }else{// course available in the subject table, return prerequisite result;
                myQuery = "select "+preReqTable_prerqID+" from "+preReqTable+" where "+preReqTable_subjID+"=\""+courseID+"\"";
                result = conn.createStatement().executeQuery(myQuery);
                return result;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private boolean preReqChecker(int studentID, ResultSet queryResult){
        String myQuery = null;
        ArrayList prereqCourses = new ArrayList();

        //parse queryResult
        try {
            while(queryResult.next()==true){
                prereqCourses.add(queryResult.getString(1));
            }
            System.out.println(prereqCourses);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //check if all returned queryResult are in the student enrollment table
        for(int i=0; i<prereqCourses.size(); i++){
            myQuery = "select "+enrolTable_grade+" from "+enrolTable
                    +" where "+enrolTable_studentID+"="+studentID
                    +" and " + enrolTable_subjectID+"=\""+prereqCourses.get(i)+"\"";
            try {
                result=conn.createStatement().executeQuery(myQuery);

                if(result.next()==false){   // if the prerequisite course(s) not taken by the student
                    return false;
                }else if(result.getString(enrolTable_grade).matches("F")||result.getString(enrolTable_grade).matches("IPG")){ // if the student failed one prerequisite course
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private boolean enrollCourse(int studentID, String courseID){
        // insert into enrollment values (studentID, subjectID, grade)
        String myQuery = "insert into "+enrolTable+" values (" +studentID+", \""+courseToEnroll+"\", \"IPG\")";
        System.out.println(myQuery);
        try {
            conn.createStatement().executeUpdate(myQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // update the courseToEnroll to the subjectID
    private boolean parseStudentQuery(String studentQuery){

        if(studentQuery.split("\\|")[0].matches("[Ee]nroll")==false){
            return false;
        }else{
            courseToEnroll = studentQuery.split("\\|")[1];
            return true;
        }
    }

    private void printPrereq(ResultSet result){
        String col = null;
        try {
            while(result.next()==true){
                col = result.getString(1);
                System.out.println(col);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
