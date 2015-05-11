package AjaxController;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BusinessLogic.ServerLogic.ServerConsole;

import com.google.gson.Gson;

/**
 * Servlet implementation class StudentLogout
 */
@WebServlet("/StudentLogout")
public class StudentLogout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentLogout() {
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
		// TODO Auto-generated method stub
		System.out.println("logout request received!!!");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("message", "value");
		String username_value = (String) request.getSession().getAttribute("savedUserName");
		System.out.println("current User: " + username_value);
		
		String logoutTest = request.getParameterValues("dummyKey")[0];		
		if(logoutTest != null){
			System.out.println("logout request received!!!");
		}
		ServerConsole.getSuccessfulConnectionRecordObject(username_value).logoutStudent(username_value);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new Gson().toJson(map));

	}

}
