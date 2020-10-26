package pokemons;
import ru.ifmo.se.pokemon.*;

class Kirlia extends Pokemon {
public Kirlia(String name, int level) {
	super(name, level);
	setStats(38, 35, 35, 65, 55, 50);
	setType(Type.PSYCHIC, Type.FAIRY);
	setMove(new ThunderWave(), new Swagger(), new Charm());
}
}