package com.ipartek.formacion.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.ipartek.formacion.model.HabilidadDAO;
import com.ipartek.formacion.model.pojo.Habilidad;



/**
 * Servlet implementation class HabilidadController
 */
@WebServlet("/habilidad/*")
public class HabilidadController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(HabilidadController.class);
	
	private HabilidadDAO dao;
	
	private static String pathInfo;
	private static int id;
	private static int statusCode;
	private static Object responseBody;
      

	/**
	 * @see Servlet#init(ServletConfig)
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dao = HabilidadDAO.getInstance();
	}

	/**
	 * @see Servlet#destroy()
	 */
	@Override
	public void destroy() {
		dao = null;
	}

	
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//habilitar cors (habría que hacerlo en un filtro), para pokemon-cliente-angular
		response.addHeader("Access-Control-Allow-Origin", "*"); //permiso para todas las ips
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT"); //permiso para todos los métodos
		response.addHeader("Access-Control-Allow-Headers", "Content-Type");
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		responseBody = null;
		
		pathInfo = request.getPathInfo();
		LOG.debug("Ver pathInfo:" + pathInfo + " para saber si es listado o detalle" );
		
		try {
			
			id = obtenerId(pathInfo); //buscamos el valor del índice del libro en la url con la función obtenerId
			super.service(request, response); //llama a doGet, doPost, doPut o doDelete  
			
		} catch (Exception e) {
			statusCode = HttpServletResponse.SC_BAD_REQUEST; //400, error del cliente: solicitud mal formada, sintaxis errónea...
			
		}finally {	
			
			response.setStatus( statusCode );
			
			if ( responseBody != null ) { 
				
				//response body (lo utilizamos para listar todos los libros y para el detalle)
				PrintWriter out = response.getWriter(); //escribimos los datos en el body de la response
				String jsonResponseBody = new Gson().toJson(responseBody); //convertir java -> json (usando la librería gson)
				out.print(jsonResponseBody.toString()); //retornamos un array vacío en json dentro del body
				out.flush(); //termina de escribir los datos en el body      
			}	
		}
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//listamos 
		
		LOG.trace("Listamos habilidades");
	
		// diferenciamos entre la lista total y una habilidad en concreto:
		
		//cogemos el índice de la habildiad de la url con la función obtenerId() (service): 
		if ( id != -1 ) {	//detalle de una habilidad por su id
			
			//recuperamos una habilidad por su id:
			responseBody = dao.getById(id);
			
			//response status code:
			if ( null != responseBody ) {
				statusCode = HttpServletResponse.SC_OK;	//200, ok
			}else {
				statusCode = HttpServletResponse.SC_NOT_FOUND;	//404, no se encuentra el recurso solicitado
			}
			
		}else {		//listado de todas las habilidades de la bd
			
			//recuperamos la lista de todas las habilidades de la bd:
			responseBody = (ArrayList<Habilidad>) dao.getAll();
			
			//response status code:
			if (  ((ArrayList<Habilidad>)responseBody).isEmpty()  ) {
				statusCode = HttpServletResponse.SC_NO_CONTENT;	//204, no hay contenido: encuentra el recurso pero está vacío
			}else {
				statusCode = HttpServletResponse.SC_OK;	//200, ok
			}
			
		}	
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TO DO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TO DO Auto-generated method stub
	}
	
	
	protected static int obtenerId(String pathInfo) throws Exception {
		
		int id = -1;
		
		if (pathInfo != null ) {
			
			//buscamos el valor del índice del producto en la url
			String[] partes = pathInfo.split("/"); 
			if(partes.length == 2) {
				id = Integer.parseInt(partes[1]); 
			}else if(partes.length > 2) {
				throw new Exception("El pathInfo está mal formado" + pathInfo);
			}
				
		}

		return id;
	}

}
