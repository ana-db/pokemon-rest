package com.ipartek.formacion.model.pojo;

public class Habilidad {
	
	//variables:
	private int id;
	private String nombre;
	
	//constructores:
	public Habilidad() {
		super();
		this.id = 0;
		this.nombre = "";
	}
	
	public Habilidad(int id, String nombre) {
		this();
		this.id = id;
		this.nombre = nombre;
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

	//toString
	@Override
	public String toString() {
		return "Habilidad [id=" + id + ", nombre=" + nombre + "]";
	}
	
	
	
	
}
