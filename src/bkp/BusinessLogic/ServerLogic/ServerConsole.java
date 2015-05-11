package BusinessLogic.ServerLogic;

import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import BusinessLogic.Constant;
import BusinessLogic.ClientLogic.ClientStudent;

/**
 * Created by dyao on 10/21/14.
 */


// Server side main
public class ServerConsole extends HttpServlet{

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String dbURL = "jdbc:mysql://localhost/Academy";
 //   private static HashMap<String, ClientStudent> connectionCache = new HashMap<String, ClientStudent>();
    public static HashMap<String, ClientStudent> successfulConnectionRecord = new HashMap<String, ClientStudent>();


    public static void main(String[] myString){

        Connection conn = startConnection(dbURL, "root", "password");
        if (conn != null){
            new ClientLoginManager(6606, conn);
        }else{
            System.out.println("Unable to connect to database");
        }

    }
    
    private static Connection startConnection(String dbURL, String userName, String passWord){
        Connection dbConnection;
        try {
            Class.forName(JDBC_DRIVER);
            dbConnection = DriverManager.getConnection(dbURL, userName, passWord);
            return dbConnection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static void loginStudent(String studentID, String studentPassword){
    	ClientStudent clientStudent = new ClientStudent(studentID, studentPassword);
    	//setConnectionCache(studentID, clientStud);
    	if(clientStudent.getLoginFeedbackMessage().equals(Constant.LoginSuccess)){// && getSuccessfulConnectionRecordObject("studentID")==null){
    	//	setSuccessfulConnectionRecord(studentID, clientStudent);
    		//System.out.println("StudentRecord is set: " + connectionCache.get(studentID).getStudentID());
    	}
    }
    
/*    public static void setConnectionCache(String recordID, ClientStudent clientStudent){
    	connectionCache.put(recordID, clientStudent);
    }
    public static ClientStudent getConnectionCacheObject(String recordID){
    	return connectionCache.get(recordID);
    }*/
    public static void setSuccessfulConnectionRecord(String recordID, ClientStudent clientStudent){
    	successfulConnectionRecord.put(recordID, clientStudent);
    }
    public static ClientStudent getSuccessfulConnectionRecordObject(String recordID){
    	return successfulConnectionRecord.get(recordID);
    }
    
}
