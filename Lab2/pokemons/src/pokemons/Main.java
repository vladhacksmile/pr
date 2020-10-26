package pokemons;
import ru.ifmo.se.pokemon.*;
public class Main {
public static void main(String[] args) {
	Battle b = new Battle();
	b.addAlly(new Moltres("Мортис", 1));
	b.addAlly(new Pineco("Шелли", 2));
	b.addAlly(new Forretress("Леон", 3));
	b.addFoe(new Ralts("Биби", 4));
	b.addFoe(new Kirlia("Пайпер", 5));
	b.addFoe(new Gardevoir("Роза", 6));
	b.go();
}
}