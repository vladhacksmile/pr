package pokemons;
import ru.ifmo.se.pokemon.*;

class Gardevoir extends Pokemon {
public Gardevoir(String name, int level) {
	super(name, level);
	setStats(68, 65, 65, 125, 115, 80);
	setType(Type.PSYCHIC, Type.FAIRY);
	setMove(new ThunderWave(), new Swagger(), new Charm(), new Growl());
}
}