package BusinessLogic.ClientLogic;

/**
 * Created by dyao on 10/21/14.
 */

import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServlet;








import BusinessLogic.Constant;
//import BusinessLogic.ServerLogic.ClientWorker;
import BusinessLogic.ServerLogic.ServerConsole;

public class ClientStudent extends HttpServlet{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4129388079509534749L;
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


    private static final String studentTable = "Student";
    private static final String studentTable_studentID = "studentID";
    private static final String studentTable_password = "password";
	
	String serverName = Constant.Host;
    int port = Constant.PortNumber;
  //  boolean isLoginDone = false;
    String studentID = null;
    String studentPw = null;
    String subjectToSelect = null;
    String payLoad = null;
 //   String serverReturnMessage = null;
    Socket studentConnection;
//    private ClientWorker studentWorker;
    String requestType_registerCourse;
    String requestValue_courseID;
    boolean requestOpen;
   

	boolean loginStatus;
    String loginFeedbackMessage;
    String registrarFeedbackMessage;
    
    Connection dbConnection;
	
    
//    DataInputStream in;
//    DataOutputStream out;
	public ClientStudent(){
		
	}
    
	public ClientStudent(Connection dbConn, String studID, String studentPassword){
		this.studentID = studID;
		this.studentPw = studentPassword;
		this.dbConnection = dbConn;
		//loginStudent(dbConnection, studentID, studentPw);	// this sets loginStatus
	}
	
    public void loginStudent(String studentID, String studentPassword){

    	String myQuery = "select "+studentTable_password+" from "+studentTable
                +" where "+studentTable_studentID+"="+studentID;

        try {
			
			if(dbConnection != null){
				ResultSet result = dbConnection.createStatement().executeQuery(myQuery);
                if(result.next()==true){
                    if(result.getString(studentTable_password).equals(studentPassword)){
                    	if(ServerConsole.getSuccessfulConnectionRecordObject(studentID)==null){
                    		ServerConsole.setSuccessfulConnectionRecord(studentID, this);
                    		//System.out.println("total client logged in: " + ServerConsole.getSuccessfulConnectionRecord().size());
                    		ServerConsole.printLoggedInClient();
                    		loginFeedbackMessage = Constant.LoginSuccess;
                    		//clientStudent.setLoginFeedbackMessage(Constant.LoginSuccess);
                    	}else{
                    		//dataOut.writeUTF(Constant.LoginFail_DuplicateLogin);
                    		//clientStudent.setLoginFeedbackMessage(Constant.LoginFail_DuplicateLogin);
                    		loginFeedbackMessage = Constant.LoginFail_DuplicateLogin;
                    	}
                    }else{
                    	//dataOut.writeUTF(Constant.LoginFail_WrongPassword);
                    	//clientStudent.setLoginFeedbackMessage(Constant.LoginFail_WrongPassword);
                    	loginFeedbackMessage = Constant.LoginFail_WrongPassword;
                    }
                }else{
                    //dataOut.writeUTF("Login name does not exist: " + userName);
                    //dataOut.writeUTF(Constant.LoginFail_WrongLoginName);
                	//clientStudent.setLoginFeedbackMessage(Constant.LoginFail_WrongLoginName);
                	loginFeedbackMessage = Constant.LoginFail_WrongLoginName;
                }
            }else{
                loginFeedbackMessage = Constant.LoginFail_Other;
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void logoutStudent(String studentID){
    	ServerConsole.getSuccessfulConnectionRecord().remove(studentID);
    	ServerConsole.printLoggedInClient();
    }
    
    public ArrayList<String> getPrerequisitedCourses(String courseID){
    	ResultSet queryResult = preReqFinder(courseID);
    	ArrayList<String> prereqCourses = new ArrayList<String>();
        try {
			while(queryResult.next()==true){
			    prereqCourses.add(queryResult.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return prereqCourses; 
    }
    
    public boolean registerCourse(String courseID){
    	String courseToEnroll = courseID;
    	String userID = studentID;
    	
    	boolean isPrereqSatisfied = false;
    	//ClientStudent studentClient = (ClientStudent) client;

        if(preReqFinder(courseToEnroll)==null){
            //dataOut.writeUTF("Course not available: " + courseToEnroll);
        	setRegistrarFeedbackMessage(Constant.RegisterCourseFail_CourseUnavailable);
        }else{
            isPrereqSatisfied = preReqChecker(Integer.parseInt(userID), preReqFinder(courseToEnroll));
            if(isPrereqSatisfied==true){

                if(enrollCourse(Integer.parseInt(userID), courseToEnroll)==true){
                    //dataOut.writeUTF("Course registered: "+courseToEnroll);
                	setRegistrarFeedbackMessage(Constant.RegisterCourseSuccess);
                }else{
                    //dataOut.writeUTF("Unable to register course: "+courseToEnroll);
                	setRegistrarFeedbackMessage(Constant.RegisterCourseFail_AlreadyEnrolled);
                }
            }else{
                //dataOut.writeUTF("Prerequisite condition not satisfied.");
            	setRegistrarFeedbackMessage(Constant.RegisterCourseFail_PrerequisiteConditionUnsatisfied);
            }
        }
    	
    	
    	return false;
    }
    
    // return null if course is not available
    // return return prerequisite result otherwise
    private ResultSet preReqFinder(String courseID){
        String myQuery = null;
        Connection conn = dbConnection;
        try {
            // check if the course is available for student to choose
            myQuery = "select * from "+subjectTable+" where "+subjectID+"=\""+courseID+"\"";
            ResultSet result = conn.createStatement().executeQuery(myQuery);

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
        Connection conn = dbConnection;
    	String myQuery = null;
        ArrayList<String> prereqCourses = new ArrayList<String>();

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
                ResultSet result=conn.createStatement().executeQuery(myQuery);

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
    	Connection conn = dbConnection;
        // insert into enrollment values (studentID, subjectID, grade)
        String myQuery = "insert into "+enrolTable+" values (" +studentID+", \""+courseID+"\", \"IPG\")";
        System.out.println(myQuery);
        try {
            conn.createStatement().executeUpdate(myQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
	
	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getLoginFeedbackMessage() {
		return loginFeedbackMessage;
	}

	public void setLoginFeedbackMessage(String loginFeedbackMessage) {
		this.loginFeedbackMessage = loginFeedbackMessage;
	}

	public String getRegistrarFeedbackMessage() {
		return registrarFeedbackMessage;
	}

	public void setRegistrarFeedbackMessage(String registrarFeedbackMessage) {
		this.registrarFeedbackMessage = registrarFeedbackMessage;
	}

	public String getRequestType_registerCourse() {
		return requestType_registerCourse;
	}

	public void setRequestType_registerCourse(String requestType_registerCourse) {
		this.requestType_registerCourse = requestType_registerCourse;
	}

	public String getRequestValue_courseID() {
		return requestValue_courseID;
	}

	public void setRequestValue_courseID(String requestValue_courseID) {
		this.requestValue_courseID = requestValue_courseID;
	}

	public boolean isRequestOpen() {
		return requestOpen;
	}

	public void setRequestOpen(boolean requestOpen) {
		this.requestOpen = requestOpen;
	}

	
/*	
	public String getServerReturnMessage(){
		return serverReturnMessage;
	}
	
	public boolean getLoginStatus(){
		return loginStatus;
	}
	
	public String getStudentID(){
		return studentID;
	}
*/	
/*	
    public static void main(String [] args) {

        String serverName = "127.0.0.1";
        int port = 6606;
        boolean isLoginDone = false;
        String studentID = null;
        String studentPw = null;
        String subjectToSelect = null;
        String payLoad = null;

        try {

            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket sSocket = new Socket(serverName, port);
            System.out.println("Just connected to " + sSocket.getRemoteSocketAddress());

            OutputStream outToServer = sSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            InputStream inFromServer = sSocket.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

           while(true){
               BufferedReader buffIn = new BufferedReader(new InputStreamReader(System.in));

               if(isLoginDone==false){
                   System.out.println("Please Login...");
                   System.out.print("Student ID: ");
                   studentID = buffIn.readLine();
                   System.out.print("Your Password: ");
                   studentPw = buffIn.readLine();
                   payLoad = "login|"+studentID+"|"+studentPw;
                   out.writeUTF(payLoad);

                   System.out.println("Reading server feedback...");
                   System.out.println("Server: " + in.readUTF());

                   isLoginDone=true;
               }else{
                   System.out.print("Course to Enroll: ");
                   subjectToSelect = buffIn.readLine();
                   payLoad = "enroll|"+subjectToSelect;
                   out.writeUTF(payLoad);

                   System.out.println("Reading server feedback...");
                   System.out.println("Server: " + in.readUTF());
               }
           }

        }catch(IOException e) {
            e.printStackTrace();
        }
    }
*/
}