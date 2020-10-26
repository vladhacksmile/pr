package pokemons;
import ru.ifmo.se.pokemon.*;

class Forretress extends Pokemon {
public Forretress(String name, int level) {
	super(name, level);
	setStats(75, 90, 140, 60, 60, 40);
	setType(Type.BUG, Type.STEEL);
	setMove(new Bulldoze(), new Confide(), new IronDefense(), new RockPolish());
}
}