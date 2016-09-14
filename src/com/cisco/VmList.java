package com.cisco;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class VmList
 */

public class VmList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VmList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpServer httpserver = new HttpServer();
		try {
			
			//String result = httpserver.getHealth();
			String url="http://10.63.51.133/app/api/rest?formatType=json&opName=userAPIGetServiceRequests&opData=" + URLEncoder.encode("{}","UTF-8");
			System.out.println("url:" + url);
			String result = httpserver.getData(url);
			
			httpserver.close();
			
			response.setContentType("application/json");
			OutputStream out = null;
			 try {
				// init servlet output stream
				out = response.getOutputStream();
				out.write(result.getBytes("UTF-8"));
				
				out.flush();
				
			} finally {
				
				if(out != null) {
					out.close();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
