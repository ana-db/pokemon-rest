package com.ipartek.formacion.model.pojo;

import java.util.List;

public class Pokemon {
	
	private int id;
	private String nombre;
	//private List<Habilidad> habilidades;
	private Habilidad habilidad;
	
	//constructores:
	public Pokemon() {
		super();
		this.id = 0;
		this.nombre = "";
	//	this.habilidades = new List<Habilidad>();
		this.habilidad = new Habilidad();
	}
	
	public Pokemon(int id, String nombre, Habilidad habilidad) { //, List<Habilidad> habilidades) {
		this();
		this.id = id;
		this.nombre = nombre;
	//	this.habilidades = habilidades;
		this.habilidad = habilidad;
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
/*
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
*/


/*	
 	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", nombre=" + nombre + "]";
	}
*/	
	
	
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
		
	
}
