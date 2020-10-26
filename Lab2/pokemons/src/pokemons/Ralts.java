package pokemons;
import ru.ifmo.se.pokemon.*;

class Ralts extends Pokemon {
public Ralts(String name, int level) {
	super(name, level);
	setStats(28, 25, 25, 45, 35, 40);
	setType(Type.PSYCHIC, Type.FAIRY);
	setMove(new ThunderWave(), new Swagger());
}
}