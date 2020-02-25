-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.18 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para pokedex
DROP DATABASE IF EXISTS `pokedex_ana`;
CREATE DATABASE IF NOT EXISTS `pokedex_ana` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pokedex_ana`;

-- Volcando estructura para tabla pokedex.habilidad
DROP TABLE IF EXISTS `habilidad`;
CREATE TABLE IF NOT EXISTS `habilidad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla pokedex.habilidad: ~7 rows (aproximadamente)
DELETE FROM `habilidad`;
/*!40000 ALTER TABLE `habilidad` DISABLE KEYS */;
INSERT INTO `habilidad` (`id`, `nombre`) VALUES
	(6, 'ascua'),
	(5, 'electricidad estatica'),
	(2, 'foco interno'),
	(7, 'hedor'),
	(1, 'impasible'),
	(3, 'justiciero'),
	(4, 'pararrayos');
/*!40000 ALTER TABLE `habilidad` ENABLE KEYS */;

-- Volcando estructura para tabla pokedex.pokemon
DROP TABLE IF EXISTS `pokemon`;
CREATE TABLE IF NOT EXISTS `pokemon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL DEFAULT '0',
  `imagen` varchar(150) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla pokedex.pokemon: ~8 rows (aproximadamente)
DELETE FROM `pokemon`;
/*!40000 ALTER TABLE `pokemon` DISABLE KEYS */;
INSERT INTO `pokemon` (`id`, `nombre`, `imagen`) VALUES
	(1, 'lucario', 'https://assets.pokemon.com/assets/cms2/img/pokedex/full/448.png'),
	(2, 'pikachu', 'https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png'),
	(3, 'charmander', 'https://assets.pokemon.com/assets/cms2/img/pokedex/full/004.png'),
	(4, 'bulbasaur', 'https://assets.pokemon.com/assets/cms2/img/pokedex/full/001.png'),
	(5, 'squirtle', 'https://assets.pokemon.com/assets/cms2/img/pokedex/full/007.png'),
	(6, 'nuevo0 modif', 'https://cdn.pixabay.com/photo/2019/11/27/14/06/pokemon-4657023_960_720.png'),
	(8, 'nuevo1 modif 2', 'https://cdn.pixabay.com/photo/2019/11/27/14/06/pokemon-4657023_960_720.png');
/*!40000 ALTER TABLE `pokemon` ENABLE KEYS */;

-- Volcando estructura para tabla pokedex.pokemon_has_habilidades
DROP TABLE IF EXISTS `pokemon_has_habilidades`;
CREATE TABLE IF NOT EXISTS `pokemon_has_habilidades` (
  `id_pokemon` int(11) NOT NULL DEFAULT '0',
  `id_habilidad` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_pokemon`,`id_habilidad`),
  KEY `FK_habilidad` (`id_habilidad`),
  CONSTRAINT `FK_habilidad` FOREIGN KEY (`id_habilidad`) REFERENCES `habilidad` (`id`),
  CONSTRAINT `FK_pokemon` FOREIGN KEY (`id_pokemon`) REFERENCES `pokemon` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla pokedex.pokemon_has_habilidades: ~20 rows (aproximadamente)
DELETE FROM `pokemon_has_habilidades`;
/*!40000 ALTER TABLE `pokemon_has_habilidades` DISABLE KEYS */;
INSERT INTO `pokemon_has_habilidades` (`id_pokemon`, `id_habilidad`) VALUES
	(1, 1),
	(3, 1),
	(4, 1),
	(8, 1),
	(1, 2),
	(8, 2),
	(1, 3),
	(3, 3),
	(5, 3),
	(8, 3),
	(2, 4),
	(2, 5),
	(3, 6),
	(6, 6),
	(4, 7),
	(6, 7),
	(8, 7);
/*!40000 ALTER TABLE `pokemon_has_habilidades` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
