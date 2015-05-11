package AjaxController;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BusinessLogic.ClientLogic.ClientStudent;
import BusinessLogic.ServerLogic.ServerConsole;
import BusinessLogic.Constant;

import com.google.gson.Gson;

/**
 * Servlet implementation class UpdateUsername
 * gson jar files - http://code.google.com/p/google-gson/
 */
@WebServlet("/StudentLogin")
public class StudetnLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudetnLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean isValid = false;
		String username_value = request.getParameterValues("username_name")[0]; //getParameter("username_name");
		String password_value = request.getParameterValues("password_name")[0];
		
	//	HttpSession savedUsername = request.getSession();
	//	savedUsername.setAttribute("savedUserName", username_value);
		request.getSession().setAttribute("savedUserName", username_value);
		
		System.out.println("in login servlet doPost");
		ServerConsole.loginStudent(username_value, password_value);
	
		if(ServerConsole.getSuccessfulConnectionRecordObject(username_value)!=null){
			String ServerLogicLoginMessage = ServerConsole.getSuccessfulConnectionRecordObject(username_value).getLoginFeedbackMessage();// getServerReturnMessage();
			map.put("message", ServerLogicLoginMessage);
			write(response, map);
		}else{
			map.put("message", "Unable to Login Student Please Try again");
			//map.put("message", ServerLogicLoginMessage);

			write(response, map);
		}
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
