package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class OAuth
 */
@WebServlet("/OAuth.do")
public class OAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			  if (request.getQueryString()==null&request.getParameter("calc") != null) {
			
				  // 无query值，有calc，则来自html按钮
				  String strBackUrl = "http://" + request.getServerName() //服务器地址  
                  
				  + ":"   
				                        
				  + request.getServerPort()//端口号  
				                        
				  + request.getContextPath()      //项目名称  
				                       
				   + request.getServletPath() ;     //请求页面或其他地址 
				  
					response.sendRedirect("https://www.eecs.yorku.ca/~roumani/servers/auth/oauth.cgi?back=" + strBackUrl);
			  
			  }else if(request.getQueryString()!=null&request.getParameter("calc") == null){
					//有query值，无calc，则来自auth远端服务器
					try {
						response.setContentType("text/html");
				        Writer out = response.getWriter();
				        String html = "<html><body>";
				        html += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
				        html += "<b>Authentication Result:</b><br/><code>";
				        html += request.getQueryString();
				        html += "</code></body></html>";
				        out.write(html);
				        
					} catch (Exception e)
				     {
				    	 e.printStackTrace();
				        response.setContentType("text/html");
				        Writer out = response.getWriter();
				        String html = "<html><body>";
				        html += "<p><a href=' Dash.do'>Back to Dashboard</a></p>";
				        html += "<p>Error " + e.getMessage() + "</p>";
				        out.write(html);
				     }
			}else {
				//都没有则引导向html页面
				this.getServletContext().getRequestDispatcher("/OAuth.html").forward(request, response);

			}
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
