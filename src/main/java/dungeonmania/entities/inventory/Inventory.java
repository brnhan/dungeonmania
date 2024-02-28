package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasurable;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.map.GameMap;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables(GameMap map) {
        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        int treasurables = count(Treasurable.class);
        int keys = count(Key.class);
        int sunstones = count(SunStone.class);
        int swords = count(Sword.class);
        List<String> result = new ArrayList<>();

        if (Bow.canBuild(wood, arrows)) {
            result.add("bow");
        }
        if (Shield.canBuild(wood, treasurables, keys)) {
            result.add("shield");
        }
        if (Sceptre.canBuild(wood, arrows, keys, treasurables, sunstones)) {
            result.add("sceptre");
        }
        if (MidnightArmour.canBuild(map, swords, sunstones)) {
            result.add("midnight_armour");
        }

        return result;
    }

    public InventoryItem checkBuildCriteria(Player p, boolean remove, String entity, EntityFactory factory) {
        if (entity.equals("bow")) {
            return buildBow(remove, factory);
        } else if (entity.equals("shield")) {
            return buildShield(remove, factory);
        } else if (entity.equals("sceptre")) {
            return buildSceptre(remove, factory);
        } else if (entity.equals("midnight_armour")) {
            return buildMidnightArmour(remove, factory);
        }

        return null;
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

    public void useWeapon(Game game) {
        BattleItem weapon = getWeapon();
        weapon.use(game);
    }

    public boolean hasValidKey(int number) {
        Key key = getFirst(Key.class);
        return (key != null && key.getnumber() == number);
    }

    public void removeKey() {
        Key key = getFirst(Key.class);
        remove(key);
    }

    private Bow buildBow(boolean remove, EntityFactory factory) {
        if (remove) {
            List<Wood> wood = getEntities(Wood.class);
            List<Arrow> arrows = getEntities(Arrow.class);
            items.remove(wood.get(0));
            items.remove(arrows.get(0));
            items.remove(arrows.get(1));
            items.remove(arrows.get(2));
        }

        return factory.buildBow();
    }

    private Shield buildShield(boolean remove, EntityFactory factory) {
        if (remove) {
            List<Wood> wood = getEntities(Wood.class);
            List<Treasure> treasure = getEntities(Treasure.class);
            List<Key> keys = getEntities(Key.class);
            items.remove(wood.get(0));
            items.remove(wood.get(1));
            if (treasure.size() >= 1) {
                items.remove(treasure.get(0));
            } else if (keys.size() >= 1) {
                items.remove(keys.get(0));
            }
        }

        return factory.buildShield();
    }

    private Sceptre buildSceptre(boolean remove, EntityFactory factory) {
        if (remove) {
            // 1 wood or 2 arrows
            List<Wood> wood = getEntities(Wood.class);
            List<Arrow> arrows = getEntities(Arrow.class);
            if (wood.size() >= 1) {
                items.remove(wood.get(0));
            } else {
                items.remove(arrows.get(0));
                items.remove(arrows.get(1));
            }

            // 1 sunstone necessary (remove)
            items.remove(getEntities(SunStone.class).get(0));

            // 1 key or 1 treasure or 1 sunstone (don't remove)
            // Prioritises keys, then treasure, then sunstone
            List<Key> keys = getEntities(Key.class);
            List<Treasure> treasure = getEntities(Treasure.class);
            if (keys.size() >= 1) {
                items.remove(keys.get(0));
            } else if (treasure.size() >= 1) {
                items.remove(treasure.get(0));
            }
        }

        return factory.buildSceptre();
    }

    private MidnightArmour buildMidnightArmour(boolean remove, EntityFactory factory) {
        if (remove) {
            List<Sword> swords = getEntities(Sword.class);
            List<SunStone> sunstones = getEntities(SunStone.class);
            items.remove(swords.get(0));
            items.remove(sunstones.get(0));
        }

        return factory.buildMidnightArmour();
    }

    public Integer getMindControlDuration() {
        Sceptre sceptre = getFirst(Sceptre.class);
        if (sceptre == null) {
            return null;
        }

        return sceptre.getMindControlDuration();
    }
}
