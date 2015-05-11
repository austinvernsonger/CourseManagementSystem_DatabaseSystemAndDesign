package AjaxController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BusinessLogic.ServerLogic.ServerConsole;

import com.google.gson.Gson;

/**
 * Servlet implementation class StudentRegister_Servlet
 */
@WebServlet("/StudentRegister")
public class StudentRegistrar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentRegistrar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*		System.out.println("im in now");
		HashMap<String, Object> map = new HashMap<String, Object>();

		//String username_value = request.getParameter("username_name");//  getParameterValues("username_name")[0]; //getParameter("username_name");
		String username_value = "9966111";//  getParameterValues("username_name")[0]; //getParameter("username_name");
		String course_value = request.getParameter("course_name");
		System.out.println(username_value + "---" + course_value);
		Socket sSocket = BusinessLogic.ServerLogic.ServerConsole.getRecordConnection(username_value);
		OutputStream outToServer = sSocket.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToServer);
        InputStream inFromServer = sSocket.getInputStream();
        DataInputStream in = new DataInputStream(inFromServer);
		
        String payLoad = "enroll|"+course_value;
        out.writeUTF(payLoad);

        String serverFeedback = in.readUTF();
        

		map.put("message", serverFeedback);
		write(response, map);*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Student Register doPost");
		HashMap<String, Object> map = new HashMap<String, Object>();

		//String username_value = request.getParameter("username_name");//  getParameterValues("username_name")[0]; //getParameter("username_name");
		//String username_value = "9966111";//  getParameterValues("username_name")[0]; //getParameter("username_name");
		String username_value = (String) request.getSession().getAttribute("savedUserName");
		String course_value = request.getParameter("course_name");
		System.out.println(username_value + "---" + course_value);
		if(ServerConsole.getSuccessfulConnectionRecordObject(username_value)!=null){
			ServerConsole.getSuccessfulConnectionRecordObject(username_value).registerCourse(course_value);	
		}
		
		//Socket sSocket = BusinessLogic.ServerLogic.ServerConsole.getRecordConnection(username_value);
		//OutputStream outToServer = sSocket.getOutputStream();
     //   DataOutputStream out = new DataOutputStream(outToServer);
     //   InputStream inFromServer = sSocket.getInputStream();
     //   DataInputStream in = new DataInputStream(inFromServer);
		
      //  String payLoad = "enroll|"+course_value;
    //    out.writeUTF(payLoad);

    //    String serverFeedback = in.readUTF();
        

	//	map.put("message", serverFeedback);
	//	write(response, map);
	}
	
	private void write(HttpServletResponse response, HashMap<String, Object> map) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(new Gson().toJson(map));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
