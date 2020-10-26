package pokemons;

import ru.ifmo.se.pokemon.*;

class Pineco extends Pokemon {
public Pineco(String name, int level) {
	super(name, level);
	setStats(50, 65, 90, 35, 35, 15);
	setType(Type.BUG);
	setMove(new Bulldoze(), new Confide(), new IronDefense());
}
}