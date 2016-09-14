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
 * Servlet implementation class Proxy
 */
public class Proxy extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Proxy() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpServer httpserver = new HttpServer();
		
		String url = request.getParameter("url");
		String server = request.getParameter("server");
		
		System.out.println("url:" + url);
		try {
			
			//String result = httpserver.getHealth();
			System.out.println(server + URLEncoder.encode(url,"UTF-8"));
			String result = httpserver.getData("http://" + server + URLEncoder.encode(url,"UTF-8"));
			
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

}
