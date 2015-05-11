package BusinessLogic.ServerLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import BusinessLogic.ClientLogic.ClientStudent;

/**
 * Created by dyao on 10/21/14.
 */


// Server side main
public class ServerConsole extends HttpServlet{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4329121443952126054L;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String dbURL = "jdbc:mysql://localhost/Academy";
 //   private static HashMap<String, ClientStudent> connectionCache = new HashMap<String, ClientStudent>();
    private static HashMap<String, ClientStudent> successfulConnectionRecord = new HashMap<String, ClientStudent>();
   // public static HashMap<String, ClientWorker> clientWorkerRecord = new HashMap<String, ClientStudent>();
    private static Connection dbConn;
   // private static ClientLoginManager loginManager;
    
    public ServerConsole(){
    	dbConn = startConnection(dbURL, "root", "password");
    	 if (dbConn != null){
             //loginManager = new ClientLoginManager(dbConn);
         }else{
             System.out.println("Unable to connect to database");
         }
    }

    public static void main(String[] myString){
        new ServerConsole();
    }
    
    private static Connection startConnection(String dbURL, String userName, String passWord){
        Connection dbConnection;
        try {
            Class.forName(JDBC_DRIVER);
            dbConnection = DriverManager.getConnection(dbURL, userName, passWord);
            return dbConnection;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    
    public static void loginStudent(String studentID, String studentPassword){
    	ClientStudent clientStudent = new ClientStudent(dbConn, studentID, studentPassword);
    	clientStudent.loginStudent(studentID, studentPassword);
    }
    
    public static void printLoggedInClient(){
    	System.out.println(successfulConnectionRecord.keySet());
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
    public static HashMap<String, ClientStudent> getSuccessfulConnectionRecord(){
    	return successfulConnectionRecord;
    }
    public static ClientStudent getSuccessfulConnectionRecordObject(String recordID){
    	return successfulConnectionRecord.get(recordID);
    }
}
