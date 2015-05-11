package BusinessLogic.ServerLogic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;

import BusinessLogic.Constant;


/**
 * Created by dyao on 10/24/14.
 */

public class ClientLoginManager extends HttpServlet{

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String dbURL = "jdbc:mysql://localhost/Academy";

    private static final String studentTable = "Student";
    private static final String studentTable_studentID = "studentID";
    private static final String studentTable_password = "password";

    Connection conn = null;

    private String userName;
    private String password;
    private String myQuery;


    public ClientLoginManager(int socket, Connection dbConnection){
        conn = dbConnection;
        try {
            ServerSocket sSocket = new ServerSocket(socket);

            while(true){
                Socket cSocket = sSocket.accept();
                DataInputStream dataIn = new DataInputStream(cSocket.getInputStream());
                DataOutputStream dataOut = new DataOutputStream(cSocket.getOutputStream());

                String clientInput = dataIn.readUTF();

                if(parseLogin(clientInput)==true){
                    myQuery = "select "+studentTable_password+" from "+studentTable
                            +" where "+studentTable_studentID+"="+userName;

                    ResultSet result = conn.createStatement().executeQuery(myQuery);

                    if(conn != null){
                        if(result.next()==true){
                            if(result.getString(studentTable_password).equals(password)){
                            	if(ServerConsole.getSuccessfulConnectionRecordObject(userName)==null){
                            		new Thread(new ClientWorker(cSocket, conn, userName)).start();                        
                                    //dataOut.writeUTF("Client login successful: " + userName);
                                    dataOut.writeUTF(Constant.LoginSuccess);
                                   // System.out.println("LoginManger: student logged in " + userName + "--record size: " + ServerConsole.successfulConnectionRecord.size());
                            	}else{
                            		dataOut.writeUTF(Constant.LoginFail_DuplicateLogin);	
                            	}
                            }else{
                            	dataOut.writeUTF(Constant.LoginFail_WrongPassword);
                            }
                        }else{
                            //dataOut.writeUTF("Login name does not exist: " + userName);
                            dataOut.writeUTF(Constant.LoginFail_WrongLoginName);
                        }
                    }else{
                        //dataOut.writeUTF("Server not ready");
                        dataOut.writeUTF(Constant.LoginFail_Other);
                        //System.out.println("");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private boolean parseLogin(String payLoad){
        if(payLoad.split("\\|")[0].matches("[Ll]ogin")){
            userName = payLoad.split("\\|")[1];
            password = payLoad.split("\\|")[2];
            return true;
        }else{
            return false;
        }

    }

}
