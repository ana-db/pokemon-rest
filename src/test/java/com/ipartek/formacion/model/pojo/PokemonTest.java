package com.ipartek.formacion.model.pojo;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PokemonTest {
	
	Pokemon nuevoPokemon = new Pokemon();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		/* comprobamos que al crear un pokemon, su id es 0, su nombre e imagen
		 * son cadenas de texto vacías y su array de habilidades está vacío
		*/
		assertEquals( 0, nuevoPokemon.getId() ); 
		assertEquals( "", nuevoPokemon.getNombre() );
		assertEquals( "", nuevoPokemon.getImagen() );
		assertEquals( 0, nuevoPokemon.getHabilidades().size() );
	}

}
