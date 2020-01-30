package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Habilidad;
import com.ipartek.formacion.model.pojo.Pokemon;


public class PokemonDAO implements IDAO<Pokemon>{
	
	private final static Logger LOG = Logger.getLogger(PokemonDAO.class);
	
	private static PokemonDAO INSTANCE;
	
	private static final String SQL_GET_ALL = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
												" FROM pokemon p, pokemon_has_habilidades ph, habilidad h " + 
												" WHERE p.id = ph.id_pokemon AND ph.id_habilidad = h.id " + 
												" ORDER BY p.id DESC LIMIT 500;";
	
	private static final String SQL_GET_BY_ID = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
												" FROM pokemon p, pokemon_has_habilidades ph, habilidad h " + 
												" WHERE p.id = ph.id_pokemon AND ph.id_habilidad = h.id AND p.id= ?" + 
												" ORDER BY p.id DESC LIMIT 500;";
	
	private static final String SQL_GET_BY_NOMBRE = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
														" FROM pokemon p, pokemon_has_habilidades ph, habilidad h " + 
														" WHERE p.id = ph.id_pokemon AND ph.id_habilidad = h.id AND p.nombre LIKE ? " + 
														" ORDER BY p.id DESC LIMIT 500;";
	
	private static final String SQL_DELETE = "DELETE FROM pokemon WHERE id = ?;";
	
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pokemon create(Pokemon pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
		
	private Pokemon mapper(ResultSet rs, HashMap<Integer, Pokemon> pokemonHM) throws SQLException {
		
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
		
		return p;
	}

}
