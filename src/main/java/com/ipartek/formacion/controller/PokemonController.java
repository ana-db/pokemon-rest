package com.ipartek.formacion.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.ipartek.formacion.model.PokemonDAO;
import com.ipartek.formacion.model.pojo.Pokemon;
import com.ipartek.formacion.model.pojo.ResponseMensaje;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;



/**
 * Servlet implementation class PokemonController
 */
@WebServlet("/api/pokemon/*")
public class PokemonController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(PokemonController.class);
	
	private PokemonDAO dao;
	
	private static String pathInfo;
	private static int id;
	private static int statusCode;
	private static Object responseBody;
	private static String nombre;
	
	//Crear Factoria y Validador
	ValidatorFactory factory;
	Validator validator;
	

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		dao = PokemonDAO.getInstance();
		
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	/**
	 * @see Servlet#destroy()
	 */
	@Override
	public void destroy() {
		
		dao = null;
		
		factory = null;
		validator = null;
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
		
		//preparamos la respuesta indicando qué tipo de dato devuelve, ContentType y charSet:
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
			responseBody = new ResponseMensaje(e.getMessage());
			
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
		
		doPut(request, response);
	}

	
	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			
			//cogemos cogemos el índice del pokemon de la url con la función obtenerId() (tenemos la llamada en service): 
				
				// convertir json del request body a Objeto:
				BufferedReader reader = request.getReader(); //coge los datos del body de postman           
				Gson gson = new Gson(); //crea un objeto gson
				Pokemon pokemon = gson.fromJson(reader, Pokemon.class);	//convierte el objeto gson en uno de la clase Pokemon
				LOG.debug(" Json convertido a Objeto: " + pokemon);
			
				//validamos que el objeto esté bien creado:
				Set<ConstraintViolation<Pokemon>>  validacionesErrores = validator.validate(pokemon);		
				if ( validacionesErrores.isEmpty() ) {
					
					if ( id != -1 ) { //significa que el pokemon sí existe en la bd. Lo editamos según su id
						
						LOG.debug("PUT modificar recurso");
						
						Pokemon pEditar = dao.update(id, pokemon);
						
						//response status code:
						statusCode = HttpServletResponse.SC_OK;	//200, ok
						responseBody = pEditar;
				
					}else { //id == -1: hemos entrado por doPost, significa que el pokemon no existe en la bd, así que lo creamos
						
						LOG.debug("POST crear recurso");
						
						Pokemon pNuevo = dao.create(pokemon);
						
						//response status code:
						statusCode = HttpServletResponse.SC_CREATED;	//201, creado 
						responseBody = pNuevo;
					}
					
				}else {
					
					//response status code:			
					statusCode = HttpServletResponse.SC_BAD_REQUEST;	//400, datos incorrectos para un producto: precio negativo…
					ResponseMensaje responseMensaje = new ResponseMensaje("Los datos de este Pokemon no son correctos, revisalos por favor");
					
					//enviamos un array de errores para que el usuario tenga una idea de qué datos ha metido mal y por qué:
					ArrayList<String> errores = new ArrayList<String>();
					for (ConstraintViolation<Pokemon> error : validacionesErrores) {					 
						errores.add( error.getPropertyPath() + " " + error.getMessage() );
					}				
					responseMensaje.setErrores(errores);				
					responseBody = responseMensaje;
					
				}
				
		} catch (MySQLIntegrityConstraintViolationException e) {
			// response status code
			responseBody = new ResponseMensaje("El nombre del pokemon ya existe en la base de datos, elige otro por favor");			
			statusCode = HttpServletResponse.SC_CONFLICT;	//409, nombre duplicado en la bd
		} catch (Exception e) {
			// response status code
			responseBody = new ResponseMensaje(e.getMessage());			
			statusCode = HttpServletResponse.SC_BAD_REQUEST;	//400, datos incorrectos para un pokemon: peso negativo…
		} 
		
	}

	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOG.debug("DELETE eliminar recurso");
		
		//aquí cogemos cogemos el índice del pokemon de la url con la función obtenerId() (tenemos la llamada en service): 
			
		if ( id != -1 ) {	//significa que el producto sí existe en la bd. Lo eliminamos por su id
				
			try {
				
				Pokemon pEliminar = dao.delete(id);
				responseBody = pEliminar;
				
				//response status code:
				statusCode = HttpServletResponse.SC_OK;	//200, ok
				
			} catch (Exception e) {

				//response status code:
				statusCode = HttpServletResponse.SC_NOT_FOUND;	//404, no se encuentra el recurso solicitado
				responseBody = new ResponseMensaje(e.getMessage());
			}

		}		
	}
	
	
	/**
	 * Método auxiliar para obtener el id a partir de la URL 
	 * @param pathInfo
	 * @return
	 * @throws Exception
	 */
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
