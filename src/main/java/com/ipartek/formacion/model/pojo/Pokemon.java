package com.ipartek.formacion.model.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class Pokemon {
	
	//atributos:
	private int id;
	
	@NotNull
	@NotBlank
	@Size( min = 2, max = 50 )
	private String nombre;
	
	private List<Habilidad> habilidades;
	//private Habilidad habilidad;
	private String imagen;
	
	
	//constructores:
	public Pokemon() {
		super();
		this.id = 0;
		this.nombre = "";
		this.habilidades = new ArrayList<Habilidad>();
		//	this.habilidad = new Habilidad();
		this.imagen = "";
	}
	
	public Pokemon(int id, String nombre, List<Habilidad> habilidades, String imagen) { //Habilidad habilidad) { 
		this();
		this.id = id;
		this.nombre = nombre;
		this.habilidades = habilidades;
		//	this.habilidad = habilidad;
		this.imagen = imagen;
	}

	//getters y setters:
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Habilidad> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidad> habilidades) {
		this.habilidades = habilidades;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", nombre=" + nombre + ", habilidades=" + habilidades + ", imagen=" + imagen + "]";
	}



/*		
	public Habilidad getHabilidad() {
		return habilidad;
	}

	public void setHabilidad(Habilidad habilidad) {
		this.habilidad = habilidad;
	}

	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", nombre=" + nombre + ", habilidad=" + habilidad + "]";
	}
*/		
	
}
