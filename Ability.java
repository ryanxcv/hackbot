package hackbotcore;

public abstract class Ability {

    private static String name;
    private static String desc;

    private static Type type;

    public static enum Type { ATTACK, STATUS }

    public abstract void use(Unit target);

    public Type getType() { return type; }

    public static class Cut extends Ability {

        private static String name = "Cut";
        private static String desc = "A basic attack";

        private static Type type = Type.ATTACK;

        public void use(Unit target) {
            return;
        }
    }
}
