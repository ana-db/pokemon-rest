package com.ipartek.formacion.model.pojo;

public class Pokemon {
	
	private int id;
	private String nombre;
	//private List<Habilidad> habilidades
	
	public Pokemon() {
		super();
		this.id = 0;
		this.nombre = "";
	}
	
	public Pokemon(int id, String nombre) {
		this();
		this.id = id;
		this.nombre = nombre;
	}

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

	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", nombre=" + nombre + "]";
	}
	
	

}
