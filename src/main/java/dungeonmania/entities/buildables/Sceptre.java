package dungeonmania.entities.buildables;

public class Sceptre extends Buildable {
    private int mindControlDuration;

    public static final int WOOD_NEEDED = 1;
    public static final int ARROWS_NEEDED = 2;
    public static final int KEYS_NEEDED = 1;
    public static final int TREASURABLES_NEEDED = 2;
    public static final int SUNSTONES_NEEDED = 1;

    public Sceptre(int mindControlDuration) {
        super(null);
        this.mindControlDuration = mindControlDuration;
    }

    public int getMindControlDuration() {
        return mindControlDuration;
    }

    public static boolean canBuild(int wood, int arrows, int keys, int treasurables, int sunstones) {
        return (wood >= WOOD_NEEDED || arrows >= ARROWS_NEEDED)
                && (keys >= KEYS_NEEDED || treasurables >= TREASURABLES_NEEDED) && sunstones >= SUNSTONES_NEEDED;
    }
}
