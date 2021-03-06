package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Habilidad;
import com.ipartek.formacion.model.pojo.Pokemon;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;




public class PokemonDAO implements IDAO<Pokemon>{
	
	private final static Logger LOG = Logger.getLogger(PokemonDAO.class);
	
	private static PokemonDAO INSTANCE;
	
	/*
	private static final String SQL_GET_ALL = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', p.imagen 'imagen_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
												" FROM pokemon p, pokemon_has_habilidades ph, habilidad h " + 
												" WHERE p.id = ph.id_pokemon AND ph.id_habilidad = h.id " + 
												" ORDER BY p.id DESC LIMIT 500;";
	*/
	//necesitamos hacer LEFT JOIN entre tablas pokemon y pokemon_has_habilidades para que al crear un pokemon sin habilidades, podamos verlo al hacer un getAll
	private static final String SQL_GET_ALL = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', p.imagen 'imagen_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " +
												" FROM pokemon p LEFT JOIN pokemon_has_habilidades ph ON p.id = ph.id_pokemon " + 
												" LEFT JOIN habilidad h ON ph.id_habilidad = h.id " + 
												" ORDER BY p.id DESC LIMIT 500;";
	/*
	private static final String SQL_GET_BY_ID = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', p.imagen 'imagen_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
												" FROM pokemon p, pokemon_has_habilidades ph, habilidad h " + 
												" WHERE p.id = ph.id_pokemon AND ph.id_habilidad = h.id AND p.id= ?" + 
												" ORDER BY p.id DESC LIMIT 500;";
	*/
	private static final String SQL_GET_BY_ID = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', p.imagen 'imagen_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " +
												" FROM pokemon p LEFT JOIN pokemon_has_habilidades ph ON p.id = ph.id_pokemon " + 
												" LEFT JOIN habilidad h ON ph.id_habilidad = h.id " + 
												" WHERE p.id= ?" + 
												" ORDER BY p.id DESC LIMIT 500;";
	
	private static final String SQL_GET_BY_NOMBRE = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', p.imagen 'imagen_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
														" FROM pokemon p, pokemon_has_habilidades ph, habilidad h " + 
														" WHERE p.id = ph.id_pokemon AND ph.id_habilidad = h.id AND p.nombre LIKE ? " + 
														" ORDER BY p.id DESC LIMIT 500;";
	
	private static final String SQL_DELETE = "DELETE FROM pokemon WHERE id = ?;";
	
	private static final String SQL_INSERT = "INSERT INTO `pokemon` (`nombre`, `imagen`) VALUES (?, ?);";
	
	private static final String SQL_UPDATE = "UPDATE `pokemon` SET `nombre`=?, `imagen`=? WHERE `id`=?;";

	private static final String SQL_INSERT_PhH = "INSERT INTO `pokemon_has_habilidades` (`id_pokemon`, `id_habilidad`) VALUES (?, ?);";
	
	//gestionando habilidades cuando modificamos el pokemon necesitaremos hacer SQL_DELETE__PhH y SQL_INSERT_PhH:
	private static final String SQL_DELETE_PhH = "DELETE FROM pokemon_has_habilidades WHERE id_pokemon = ?;";
	
	
	private PokemonDAO() {
		super();
	}
	
	public static synchronized PokemonDAO getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new PokemonDAO();
		}
		return INSTANCE;
	}

	
	@Override
	public List<Pokemon> getAll() {
		
		//String sql = "SELECT id 'id_pokemon', nombre 'nombre_pokemon' FROM pokemon ORDER BY id DESC LIMIT 500";
		/* String sql = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
					" FROM pokemon p, pokemon_has_habilidades ph, habilidad h " + 
					" WHERE p.id = ph.id_pokemon AND ph.id_habilidad = h.id " + 
					" ORDER BY p.id DESC LIMIT 500;"; */

		//ArrayList<Pokemon> registros = new ArrayList<Pokemon>();
		
		HashMap<Integer, Pokemon> pokemonHM = new HashMap<Integer, Pokemon>();
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery() ) {
						
			while( rs.next() ) {
/*				
				int idPokemon = rs.getInt("id_pokemon");
				
				Pokemon p = pokemonHM.get(idPokemon);
				
				if(p == null) {
					p = new Pokemon();
					p.setId(idPokemon);
					p.setNombre(rs.getString("nombre_pokemon"));
				}
				
				Habilidad h = new Habilidad();
				h.setId(rs.getInt("id_habilidad"));
				h.setNombre(rs.getString("nombre_habilidad"));
				
				p.getHabilidades().add(h); 
				
				pokemonHM.put(idPokemon, p);
				
				pokemonHM.put(idPokemon, mapper(rs, idPokemon, pokemonHM));
*/
				mapper(rs, pokemonHM);
				
				//registros.add(mapper(rs));
				
			}

		} catch (Exception e) {
			LOG.error(e); 
		}
		
		return new ArrayList<Pokemon>(pokemonHM.values());
	}	
	

	@Override
	public Pokemon getById(int id) {
		
		Pokemon p = null;
		
		HashMap<Integer, Pokemon> pokemonHM = new HashMap<Integer, Pokemon>(); 
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID);
				) {
						
			//sustituimos parámetros en la SQL, en este caso 1º ? por id:
			pst.setInt(1, id);
			LOG.debug(pst);

			//ejecutamos la consulta:
			try (ResultSet rs = pst.executeQuery()) {
				while(rs.next()) {

					p = mapper(rs, pokemonHM);
				}
			}

		} catch (Exception e) {
			LOG.error(e); 
		}
		
		return p;
	}
	

	/**
	 * Método para devolver todas las coincidencias de objetos Pokemon que coincidan con el parámetro de entrada nombre (parámetro de la url)
	 * @param nombre , nombre o cadena de caracteres que queremos buscar
	 * @return ArrayList<Pokemon> , devuelve una lista con todos los pokemon que tengan en su nombre la cadena de acarcteres "nombre"
	 */
	public List<Pokemon> getByNombre(String nombre) {
				
		HashMap<Integer, Pokemon> pokemonHM = new HashMap<Integer, Pokemon>();
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_NOMBRE);
				) {
						
			//sustituimos parámetros en la SQL, en este caso 1º ? por nombre:
			pst.setString(1, "%" + nombre + "%");
			LOG.debug(pst);

			//ejecutamos la consulta:
			try (ResultSet rs = pst.executeQuery()) {
				while(rs.next()) {

					mapper(rs, pokemonHM);
				}
			}

		} catch (Exception e) {
			LOG.error(e); 
		}
		
		return new ArrayList<Pokemon>(pokemonHM.values());
	}	
	
	

	@Override
	public Pokemon delete(int id) throws Exception {

		Pokemon pBorrar = null;
		
		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(SQL_DELETE);) {

			pst.setInt(1, id);
			LOG.debug(pst);
			
			//obtenemos el id antes de elimianrlo:
			pBorrar = this.getById(id);

			//eliminamos el prodcuto:
			int affetedRows = pst.executeUpdate();
			if (affetedRows != 1) {
				pBorrar = null; //eliminamos
				throw new Exception("No se puede borrar el pokemon con id " + id);
			}
		} //si salta alguna excepción, la recoge PokemonController
		
		return pBorrar;
		
	}

	
	@Override
	public Pokemon update(int id, Pokemon pojo) throws Exception {
		
		/*
		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(SQL_UPDATE);) {

			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getImagen());
			pst.setInt(3, id);
			LOG.debug(pst);

			int affetedRows = pst.executeUpdate();
			if (affetedRows == 1) {
				pojo.setId(id);
			} else {
				throw new Exception ("No se ha encontrado registro para el Pokemon con id = " + id);
			}
		}
		 
		return pojo;
		*/
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//al modificar un pokemon, también queremos modificar sus habilidades:
		//establecemos conexión:
		Connection con = null;
		try {
				con = ConnectionManager.getConnection(); 
				PreparedStatement pst = con.prepareStatement(SQL_UPDATE);
				
				con.setAutoCommit(false); //no guarda nada en la base de datos

				pst.setString(1, pojo.getNombre());
				pst.setString(2, pojo.getImagen());
				pst.setInt(3, id);
				LOG.debug(pst);
	
				int affetedRows = pst.executeUpdate();
				if (affetedRows == 1) {
					
					pojo.setId(id);
					
					//nuevo gestionamos HABILIDADES: 1)eliminamos habilidades que tenga el pokemon, 2)insertamos las nuevas
					ArrayList<Habilidad> habilidades = (ArrayList<Habilidad>) pojo.getHabilidades();
					
					//1)eliminamos habilidades:
					PreparedStatement pst_eliminar_habilidad = con.prepareStatement(SQL_DELETE_PhH);
					pst_eliminar_habilidad.setInt(1, id); 
					LOG.debug(pst_eliminar_habilidad);
					
					int affectedRows_eliminar_habilidad = pst_eliminar_habilidad.executeUpdate();
					
					for(Habilidad habilidad : habilidades) {
											
						//2)insertamos nuevas habilidades:
						PreparedStatement pst_insertar_habilidad = con.prepareStatement(SQL_INSERT_PhH, Statement.RETURN_GENERATED_KEYS);
						//SQL_INSERT_PhH = "INSERT INTO `pokemon_has_habilidades` (`id_pokemon`, `id_habilidad`) VALUES (?, ?);";
						pst_insertar_habilidad.setInt(1, pojo.getId()); //id_pokemon
						pst_insertar_habilidad.setInt(2, habilidad.getId()); //id_habilidad
						LOG.debug(pst_insertar_habilidad);
						//
						int affectedRows2 = pst_insertar_habilidad.executeUpdate();
						if (affectedRows2 > 0) { //queremos modificar un registro, así que afectará a 1 fila
							// conseguimos el ID que acabamos de crear
							ResultSet rs_insertar_habilidad = pst_insertar_habilidad.getGeneratedKeys();
							if (rs_insertar_habilidad.next()) {
								pojo.setId(rs_insertar_habilidad.getInt(1));
								habilidad.setId(rs_insertar_habilidad.getInt(1));
							}
						}//fin if affectedRows2
						
					
					}//fin for
					
					
					//3) si todo funciona bien:
					con.commit();
					
				} else {
					throw new Exception ("No se ha encontrado registro para el Pokemon con id = " + id);
				}
				
		}catch(MySQLIntegrityConstraintViolationException e) { 
			//MySQLIntegrityConstraintViolationException --> 400 Bad request --> librería import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
			con.rollback();
			String error = e.getMessage();
			if( error.contains("Duplicate") ) {
				throw new Exception("El nombre ya existe en la base de datos, elige otro, por favor");
			}else if( error.contains("Cannot add or update a child row") ){
				throw new Exception("La habilidad no existe en la base de datos, elige otra por favor");
			}
			
		}catch(Exception e) { 
			//hacemos rb y throw cada vez que capturemos una excepcion
			con.rollback();
			String error = e.getMessage();
			throw new Exception("Se ha producido un error" + error);
			
		}finally{
			if(con != null) {
				con.close();
			}
		}
		 
		return pojo;
		
	}

	
	@Override
	public Pokemon create(Pokemon pojo) throws Exception {

		//establecemos conexión:
		Connection con = null;
		try { 
			con = ConnectionManager.getConnection(); 
			PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			
			con.setAutoCommit(false); //no guarda nada en la base de datos
			
			pst.setString(1, pojo.getNombre()); //1er interrogante con el nombre del registro que se quiere modificar; en ese caso, nombre
			pst.setString(2, pojo.getImagen());
			LOG.debug(pst);
			
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) { //queremos modificar un registro, así que afectará a 1 fila
				// conseguimos el ID que acabamos de crear
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					pojo.setId(rs.getInt(1));
					
					//nuevo HABILIDADES:
					
					// 1) for por cada habilidad (si tiene habilidades) para ir rellenado la/s habilidades
						// 2) tenemos que preparar la consulta con PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)
						// 3) insert en laq tabla intremedia
				  			//INSERT INTO `pokemon_has_habilidades` (`id_pokemon`, `id_habilidad`) VALUES ('7', '6');
							//habilidad.getId();
					
					ArrayList<Habilidad> habilidades = (ArrayList<Habilidad>) pojo.getHabilidades();
					
					for(Habilidad habilidad : habilidades) {
						
						PreparedStatement pst2 = con.prepareStatement(SQL_INSERT_PhH, Statement.RETURN_GENERATED_KEYS);
						//SQL_INSERT_PhH = "INSERT INTO `pokemon_has_habilidades` (`id_pokemon`, `id_habilidad`) VALUES (?, ?);";
						pst2.setInt(1, pojo.getId()); //id_pokemon
						pst2.setInt(2, habilidad.getId()); //id_habilidad
						LOG.debug(pst2);
						//
						int affectedRows2 = pst2.executeUpdate();
						if (affectedRows2 == 1) { //queremos modificar un registro, así que afectará a 1 fila
							// conseguimos el ID que acabamos de crear
							ResultSet rs2 = pst2.getGeneratedKeys();
							if (rs2.next()) {
								pojo.setId(rs2.getInt(1));
								habilidad.setId(rs2.getInt(1));
							}
						}//fin if affectedRows2
					
					}//fin for
					
					// si todo funciona bien:
					con.commit();
				}
			}	
			
		}catch(MySQLIntegrityConstraintViolationException e) { 
			//MySQLIntegrityConstraintViolationException --> 400 Bad request --> librería import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
			con.rollback();
			String error = e.getMessage();
			if( error.contains("Duplicate") ) {
				throw new Exception("El nombre ya existe en la base de datos, elige otro, por favor");
			}else if( error.contains("Cannot add or update a child row") ){
				throw new Exception("La habilidad no existe en la base de datos, elige otra por favor");
			}
		}catch(Exception e) { 
			//hacemos rb y throw cada vez que capturemos una excepcion
			con.rollback();
			String error = e.getMessage();
			throw new Exception("Se ha producido un error " + error);
		}finally{
			if(con != null) {
				con.close();
			}
		}
		
		
		return pojo;
		
	}
	
		
	/**
	 * Método para mapear un ResultSet y un HashMap a un pojo o a un Pokemon
	 * @param rs
	 * @param pokemonHM
	 * @return un objeto de tipo Pokemon
	 * @throws SQLException , si no puede rellenar alguno de los atributos del objeto
	 */
	private Pokemon mapper(ResultSet rs, HashMap<Integer, Pokemon> pokemonHM) throws SQLException {
		
		int idPokemon = rs.getInt("id_pokemon");

		Pokemon p = pokemonHM.get(idPokemon);
		
		if(p == null) {
			p = new Pokemon();
			p.setId(idPokemon);
			p.setNombre(rs.getString("nombre_pokemon"));
			p.setImagen(rs.getString("imagen_pokemon"));
		}
		
		Habilidad h = new Habilidad();
		h.setId(rs.getInt("id_habilidad"));
		h.setNombre(rs.getString("nombre_habilidad"));
		
		p.getHabilidades().add(h); 
		
		pokemonHM.put(idPokemon, p);
		
		return p;
	}

}
