package com.cisco;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation 
 */

public class ServiceList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceList() {
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
			String result = httpserver.getData("http://10.63.51.133/app/api/rest?ormatType=json&opName=userAPIGetAllCatalogs");
			
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
	}

}
