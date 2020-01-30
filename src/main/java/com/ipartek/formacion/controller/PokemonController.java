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
import com.ipartek.formacion.model.PokemonDAO;
import com.ipartek.formacion.model.pojo.Pokemon;


/**
 * Servlet implementation class PokemonController
 */
@WebServlet("/api/pokemon/*")
public class PokemonController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = Logger.getLogger(PokemonController.class);
	
	private PokemonDAO dao;
	
	private String pathInfo;
	private int id;
	private int statusCode;
	private Object responseBody;
	private String nombre;
	

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		dao = PokemonDAO.getInstance();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		
		dao = null;
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		responseBody = null;
		
		pathInfo = request.getPathInfo();
		LOG.debug("Ver pathInfo:" + pathInfo + " para saber si es listado o detalle" );
		
		nombre = request.getParameter("nombre");
		LOG.debug("Nombre Pokemon a buscar: " + nombre);
		
		try {
			
			id = obtenerId(pathInfo); //buscamos el valor del índice del producto en la url con la función obtenerId
			super.service(request, response); //llama a doGet, doPost, doPut o doDelete  
			
		} catch (Exception e) {
			statusCode = HttpServletResponse.SC_BAD_REQUEST; //400, error del cliente: solicitud mal formada, sintaxis errónea...
			
		}finally {	
			
			response.setStatus( statusCode );
			
			if ( responseBody != null ) { 
				
				//response body (lo ponemos en el finally porque lo utilizamos para listar todos los pokemon y para el detalle)
				PrintWriter out = response.getWriter(); //se encarga de escribir los datos en el body de la response
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
		
		LOG.trace("peticion GET");
		
		/*
		// lista de pokemons:
		ArrayList<Pokemon> pokemons = (ArrayList<Pokemon>) dao.getAll();
		
		try( PrintWriter out = response.getWriter() ){
			
			Gson json = new Gson();
			out.print( json.toJson(pokemons) );
			out.flush();
			
		}
		
		response.setStatus(200);
		*/
		
		//diferenciamos entre listar o buscar:
		if( nombre != null) { //buscamos por nombre
			
			responseBody = dao.getByNombre(nombre);
			
			if ( ((ArrayList<Pokemon>) responseBody).isEmpty() ) {
				statusCode = HttpServletResponse.SC_NO_CONTENT;
			}
			
		}else { //listamos 
		
			///////////////////////// tenemos que diefenciar entre la lista total y un pokemon en concreto ///////////////////////////////////
			
			//aquí cogemos cogemos el índice del pokemon de la url con la función obtenerId() (tenemos la llamada en service): 
			if ( id != -1 ) {	//detalle de un pokemon por su id
				
				//recuperamos un pokemon por su id:
				responseBody = dao.getById(id);
				
				//response status code:
				if ( null != responseBody ) {
					statusCode = HttpServletResponse.SC_OK;	//200, ok
				}else {
					statusCode = HttpServletResponse.SC_NOT_FOUND;	//404, no se encuentra el recurso solicitado
				}
				
			}else {		//listado de todos los pokemon de la bd
				
				//recuperamos la lista de todos los pokemon de la bd:
				responseBody = (ArrayList<Pokemon>) dao.getAll();
				
				//response status code:
				if (  ((ArrayList<Pokemon>)responseBody).isEmpty()  ) {
					statusCode = HttpServletResponse.SC_NO_CONTENT;	//204, no hay contenido: encuentra el recurso pero está vacío
				}else {
					statusCode = HttpServletResponse.SC_OK;	//200, ok
				}
				
			}	
		
		}
		
		
	
		
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	
	
	
	public static int obtenerId(String pathInfo) throws Exception {
		
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
