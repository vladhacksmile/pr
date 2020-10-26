package pokemons;
import ru.ifmo.se.pokemon.*;

class Moltres extends Pokemon {
public Moltres(String name, int level) {
	super(name, level);
	setStats(90, 100, 90, 125, 85, 90);
	setType(Type.FIRE, Type.FLYING);
	setMove(new AerialAce(), new Roost(), new Rest(), new HeatWave());
}
}
