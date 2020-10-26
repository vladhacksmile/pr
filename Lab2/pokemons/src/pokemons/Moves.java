package pokemons;

import ru.ifmo.se.pokemon.*;

class AerialAce extends PhysicalMove {
    public AerialAce() {
        super(Type.FLYING, 60, 100);
    }

    @Override
    protected boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }

    @Override
    protected String describe() {
        return "использует атаку Aerial Ace и никогда не промахивается";
    }
}

class Roost extends StatusMove {
    public Roost() {
        super(Type.FLYING, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setMod(Stat.HP, (int) (p.getHP() * 1.5));
    }

    @Override
        protected String describe() {
            return "использует атаку Roost и восстанавливает 50% здоровья";
    }
}

class Rest extends StatusMove {
    public Rest() {
        super(Type.PSYCHIC, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setStats(90, 100, 90, 125, 85, 90);
        Effect e = new Effect().condition(Status.SLEEP).attack(0).turns(2);
        p.setCondition(e);
    }

    @Override
    protected String describe() {
        return "использует атаку Rest, засыпает, полностью восстанавливает свое здоровье и лечит любые другие статусы. Сон продолжается 2 хода";
    }
}

class HeatWave extends SpecialMove {
    public HeatWave() {
        super(Type.FIRE, 95, 90);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        if (Math.random() <= 0.1) Effect.burn(p);
    }

    @Override
    protected String describe() {
        return "использует атаку Heat Wave, имеющую шанс поджечь противника с 10% вероятностью";
    }
}

class RockPolish extends StatusMove {
    public RockPolish() {
        super(Type.ROCK, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p){
        p.setMod(Stat.SPEED, (int) p.getStat(Stat.SPEED) + 2);
    }

    @Override
    protected String describe() {
        return "использует атаку Rock Polish, которая поаышает скорость покемона на 2 ступени";
    }
}

class Growl extends StatusMove {
    public Growl() {
        super(Type.NORMAL, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.ATTACK, (int) p.getStat(Stat.ATTACK) - 1);
    }

    @Override
    protected String describe() {
        return "использует атаку Growl, которая снижает атаку противника на одну ступень";
    }
}

class Charm extends StatusMove {
    public Charm() {
        super(Type.NORMAL, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.ATTACK, (int) p.getStat(Stat.ATTACK) - 2);
    }

    @Override
    protected String describe() {
        return "использует атаку Charm, которая снижает атаку противника на две ступени";
    }
}

class Bulldoze extends PhysicalMove {
    public Bulldoze() {
        super(Type.GROUND, 60, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.SPEED, (int) p.getStat(Stat.SPEED) - 1);
    }

    @Override
    protected String describe() {
        return "использует атаку Bulldoze, которая снижает скорость противника на одну ступень";
    }
}

class Confide extends StatusMove {
    public Confide() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        p.setMod(Stat.SPECIAL_ATTACK, (int) (p.getStat(Stat.SPECIAL_ATTACK) - 1));
    }

    @Override
    protected String describe() {
        return "использует атаку Confide, которая снижает специальную атаку противника на одну ступень";
    }
}

class IronDefense extends StatusMove {
    public IronDefense() {
        super(Type.STEEL, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setMod(Stat.DEFENSE, (int) p.getStat(Stat.DEFENSE) + 2);
    }

    @Override
    protected String describe() {
        return "использует атаку Iron Defense, которая повышает защиту покемона на две ступени";
    }
}

class ThunderWave extends StatusMove {
    public ThunderWave() {
        super(Type.ELECTRIC, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        if(!p.hasType(Type.GROUND)) {
            Effect.paralyze(p);
        }
    }

    @Override
    protected String describe() {
        return "использует атаку Thunder Wave, которая парализует противника. Она не срабатывает на покемонах Земляного типа";
    }
}

class Swagger extends StatusMove {
    public Swagger() {
        super(Type.NORMAL, 0, 90);
    }
    
    @Override
    protected void applyOppEffects(Pokemon p) {
        p.setMod(Stat.ATTACK, (int) p.getStat(Stat.ATTACK) + 2);
        p.confuse();
    }

    @Override
    protected String describe() {
        return "использует атаку Swagger, которая повышает атаку противника на две ступени и сбивает его с толку";
    }
}