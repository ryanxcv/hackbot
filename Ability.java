package hackbotcore;

import java.util.concurrent.Callable;

public abstract class Ability implements Callable {

    private String name;
    private String desc;

    private Type type;

    public static enum Type { ATTACK, STATUS }

    public Type getType() { return type; }
}
