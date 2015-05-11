package BusinessLogic.ClientLogic;

/**
 * Created by dyao on 10/21/14.
 */

import java.net.*;
import java.io.*;

import javax.servlet.http.HttpServlet;

import BusinessLogic.Constant;
import BusinessLogic.ServerLogic.ServerConsole;

public class ClientStudent extends HttpServlet{
    String serverName = "127.0.0.1";
    int port = 6606;
  //  boolean isLoginDone = false;
    String studentID = null;
    String studentPw = null;
    String subjectToSelect = null;
    String payLoad = null;
 //   String serverReturnMessage = null;
    Socket studentConnection;
   
    
    boolean loginStatus;
    String loginFeedbackMessage;
    String registrarFeedbackMessage;
	
    
    DataInputStream in;
    DataOutputStream out;
	
	public ClientStudent(String studID, String studentPassword){
		this.studentID = studID;
		this.studentPw = studentPassword;
		try {
			Socket sSocket = new Socket(serverName, port);
			//OutputStream outToServer = ;
	        out = new DataOutputStream(sSocket.getOutputStream());
           // InputStream inFromServer = ;
            in = new DataInputStream(sSocket.getInputStream());
            
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		studentLogin(studentID, studentPw);	// this sets loginStatus
	}
	
	public void studentLogin(String studentID_p, String studentPassword_p){
		
		try {
            studentID = studentID_p;
            studentPw = studentPassword_p;
            payLoad = "login|"+studentID+"|"+studentPw;
            out.writeUTF(payLoad);
            System.out.println("Reading server feedback...");
            this.loginFeedbackMessage = in.readUTF();
            
            // register at serverLogic side
            if(loginFeedbackMessage.equals(Constant.LoginSuccess)){
            	//ServerConsole.setConnectionRecord(studentID, sSocket);
            	this.loginStatus = true;//return true;
            	ServerConsole.setSuccessfulConnectionRecord(studentID, this);
            	System.out.println("record size: " + ServerConsole.successfulConnectionRecord.size());
            }else{
            	this.loginStatus = false;
            }
            
            //System.out.println("Server: " + loginFeedbackMessage);  

            
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean registerCourse(String courseID){
		
		try {
			
			payLoad = "enroll|"+courseID;
	        out.writeUTF(payLoad);
	        registrarFeedbackMessage = in.readUTF();
	        System.out.println("registration feedback: " + registrarFeedbackMessage);
	        return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
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