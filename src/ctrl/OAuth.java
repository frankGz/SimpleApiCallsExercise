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
			
				  // ��queryֵ����calc��������html��ť
				  String strBackUrl = "http://" + request.getServerName() //��������ַ  
                  
				  + ":"   
				                        
				  + request.getServerPort()//�˿ں�  
				                        
				  + request.getContextPath()      //��Ŀ����  
				                       
				   + request.getServletPath() ;     //����ҳ���������ַ 
				  
					response.sendRedirect("https://www.eecs.yorku.ca/~roumani/servers/auth/oauth.cgi?back=" + strBackUrl);
			  
			  }else if(request.getQueryString()!=null&request.getParameter("calc") == null){
					//��queryֵ����calc��������authԶ�˷�����
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
				//��û����������htmlҳ��
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
