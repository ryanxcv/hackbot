package hackbotcore;

import java.util.Set;

import hackbotutil.*;

/**
 * Abilities are immutable, so we don't need to worry about giving uncloned
 * references to the UI.
 */
public abstract class Ability {

    private final String name;
    private final String desc;
    private final Type type;
    private final int range;

    public static enum Type { ATTACK, STATUS }

    public Ability(String name, String desc, Type type, int range) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.range = range;
    }
    public Ability(String name, String desc, Type type) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.range = 1;
    }
    public Ability(String name, String desc, int range) {
        this.name = name;
        this.desc = desc;
        this.type = Type.ATTACK;
        this.range = range;
    }
    public Ability(String name, String desc) {
        this.name = name;
        this.desc = desc;
        this.type = Type.ATTACK;
        this.range = 1;
    }

    public Type getType() { return type; }

    public abstract void use(Unit target);

    public Set<Coordinate> rangeSet(Coordinate coord) {
        return coord.distanceSet(range);
    }

    public static class Cut extends Ability {
        public Cut() { super("Cut", "A basic attack"); }
        public void use(Unit target) { target.takeDamage(2); }
    }
}
