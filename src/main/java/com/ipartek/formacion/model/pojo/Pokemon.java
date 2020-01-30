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
	
	
	//constructores:
	public Pokemon() {
		super();
		this.id = 0;
		this.nombre = "";
		this.habilidades = new ArrayList<Habilidad>();
		//	this.habilidad = new Habilidad();
	}
	
	public Pokemon(int id, String nombre, List<Habilidad> habilidades) { //Habilidad habilidad) { 
		this();
		this.id = id;
		this.nombre = nombre;
		this.habilidades = habilidades;
		//	this.habilidad = habilidad;
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

	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", nombre=" + nombre + ", habilidades=" + habilidades + "]";
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
