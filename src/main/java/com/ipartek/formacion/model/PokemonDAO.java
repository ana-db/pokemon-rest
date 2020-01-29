package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
		
		String sql = "SELECT id, nombre FROM pokemon ORDER BY id DESC LIMIT 500";

		ArrayList<Pokemon> registros = new ArrayList<Pokemon>();
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery() ) {
			
			while( rs.next() ) {
				// TODO mapper
				/*
				Pokemon p = new Pokemon();
				p.setId( rs.getInt("id"));
				p.setNombre( rs.getString("nombre"));
				
				registros.add(p);
				*/
				registros.add(mapper(rs));
			}

		} catch (Exception e) {
			LOG.error(e); 
		}
		
		return registros;
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
	 * Utilidad para mapear un ResultSet a un pojo o a un Producto
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Pokemon mapper(ResultSet rs) throws SQLException{
		
		Pokemon p = new Pokemon();
		p.setId(rs.getInt("id_pokemon"));
		p.setNombre(rs.getString("nombre_pokemon"));
		
		Habilidad h = new Habilidad();
		h.setId(rs.getInt("id_habilidad"));
		h.setNombre(rs.getString("nombre_habilidad"));
		p.setHabilidades(h);
		
		/*
		Categoria c = new Categoria();
		c.setId(rs.getInt("id_categoria"));
		c.setNombre(rs.getString("nombre_categoria"));
		p.setCategoria(c);
		*/
		
		return p;
	}

	

}
