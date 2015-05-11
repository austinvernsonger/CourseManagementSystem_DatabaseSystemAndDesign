package BusinessLogic;

public class Constant {
	public static final String Host = "127.0.0.1";
	//public static final String Host = "localhost";
	public static final int PortNumber = 6606;
	//public static final int PortNumber = 8080;
	public static final String LoginSuccess = "LoginSuccess";
	public static final String LoginFail_WrongPassword = "LoginFail_WrongPassword";
	public static final String LoginFail_WrongLoginName = "LoginFail_WrongLoginName";
	public static final String LoginFail_DuplicateLogin = "LoginFail_DuplicateLogin";
	public static final String LoginFail_Other = "LoginFail_Other";
	
	public static final String RegisterCourseSuccess = "RegisterCourse_Success";
	public static final String RegisterCourseFail_CourseUnavailable = "RegisterCourseFail_CourseUnavailable";
	public static final String RegisterCourseFail_AlreadyEnrolled = "RegisterCourseFail_AlreadyEnrolled";
	public static final String RegisterCourseFail_Other = "RegisterCourseFail_Other";

	public static final String RegisterCourseFail_PrerequisiteConditionUnsatisfied = "RegisterCourseFail_PrerequisiteConditionUnsatisfied";
	
	
	
	
	//Client Service Type
	public static final String Student_RegiserCourse = "Student_RegiserCourse";
}
