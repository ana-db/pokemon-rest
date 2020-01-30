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
		String sql = "SELECT p.id 'id_pokemon', p.nombre 'nombre_pokemon', h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
					" FROM pokemon p, pokemon_has_habilidades ph, habilidad h " + 
					" WHERE p.id = ph.id_pokemon AND ph.id_habilidad = h.id " + 
					" ORDER BY p.id DESC LIMIT 500;";

		//ArrayList<Pokemon> registros = new ArrayList<Pokemon>();
		
		HashMap<Integer, Pokemon> pokemonHM = new HashMap<Integer, Pokemon>();
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery() ) {
						
			while( rs.next() ) {
				
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
				
				//registros.add(mapper(rs));
				
			}

		} catch (Exception e) {
			LOG.error(e); 
		}
		
		//return registros;
		return new ArrayList<Pokemon>(pokemonHM.values());
	}

	
	@Override
	public Pokemon getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pokemon delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
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
	
		
	/**
	 * Utilidad para mapear un ResultSet a un pojo o a un Pokemon
	 * @param rs : result set
	 * @return p : devuelve el objeto Pokemon con los atributos del rs
	 * @throws SQLException : si no puede asignar un valor a algún atributo del objeto
	 */
	/*	private Pokemon mapper(ResultSet rs) throws SQLException{
		
		Pokemon p = new Pokemon();
		p.setId(rs.getInt("id_pokemon"));
		p.setNombre(rs.getString("nombre_pokemon"));
		
		Habilidad h = new Habilidad();
		h.setId(rs.getInt("id_habilidad"));
		h.setNombre(rs.getString("nombre_habilidad"));
		p.setHabilidad(h);
		
		return p;
		
	}

	*/	

}
