package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Habilidad;


public class HabilidadDAO implements IDAO<Habilidad>{
	
	private final static Logger LOG = Logger.getLogger(HabilidadDAO.class);
	
	private static HabilidadDAO INSTANCE;
	
	private static final String SQL_GET_ALL = "SELECT h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
												" FROM habilidad h " +  
												" ORDER BY h.id ASC LIMIT 500;";
	
	private static final String SQL_GET_BY_ID = "SELECT h.id 'id_habilidad', h.nombre 'nombre_habilidad' " + 
												" FROM habilidad h " +  
												" WHERE h.id= ?" + 
												" ORDER BY h.id ASC LIMIT 500;";
	
	
	private HabilidadDAO() {
		super();
	}
	
	public static synchronized HabilidadDAO getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new HabilidadDAO();
		}
		return INSTANCE;
	}

	@Override
	public List<Habilidad> getAll() {

		ArrayList<Habilidad> lista = new ArrayList<Habilidad>();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery()) {
			
			LOG.debug(pst);

			while (rs.next()) {
				
				lista.add(mapper(rs));

			}

		} catch (SQLException e) {
			LOG.error(e); 
		}

		return lista;
		
	}
	

	@Override
	public Habilidad getById(int id) {

		Habilidad habilidad = null;  
		
		//obtenemos la conexión:
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID)) {

			pst.setInt(1, id);
			LOG.debug(pst);

			//ejecutamos la consulta:
			try (ResultSet rs = pst.executeQuery()) {
				while(rs.next()) {

					habilidad = mapper(rs);
				
				}
			}
		} catch (Exception e) {
			LOG.error(e); 
		}
		
		return habilidad; 
		
	}

	
	@Override
	public Habilidad delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Habilidad update(int id, Habilidad pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Habilidad create(Habilidad pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * Método para mapear un ResultSet a un pojo o a una Habilidad
	 * @param rs
	 * @return Habilidad , un objeto de tipo Habilidad
	 * @throws SQLException , si no puede rellenar alguno de los atributos del objeto
	 */
	private Habilidad mapper(ResultSet rs) throws SQLException{
		
		Habilidad h = new Habilidad();
		h.setId(rs.getInt("id_habilidad"));
		h.setNombre(rs.getString("nombre_habilidad"));
		
		return h;
	}

}
