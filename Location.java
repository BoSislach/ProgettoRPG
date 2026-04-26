import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Location {
    private String name;
    private String description;
    private ArrayList<GameCharacter> characters;
    private ArrayList<Item> items;
    private ArrayList<LockableContainer> lockableContainers;
    private Map<String, Location> connections; 

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
        this.characters = new ArrayList<>();
        this.items = new ArrayList<>();
        this.lockableContainers = new ArrayList<>();
        this.connections = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<LockableContainer> getLockableContainers() {
        return lockableContainers;
    }

    public void addLockableContainer(LockableContainer c) {
        lockableContainers.add(c);
    }

    public List<GameCharacter> getCharacters() {
        return characters;
    }

    public List<GameCharacter> getEnemies(GameCharacter protagonist) {
        List<GameCharacter> enemies = new ArrayList<>();
        for (GameCharacter character : characters) {
            if (character != protagonist && character.isAlive()) {
                enemies.add(character);
            }
        }
        return enemies;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addCharacter(GameCharacter p) {
        characters.add(p);
    }

    public void removeCharacter(GameCharacter p) {
        characters.remove(p);
    }

    public void addItem(Item o) {
        items.add(o);
    }

    public void removeItem(Item o) {
        items.remove(o);
    }

    public void connect(String direction, Location location) {
        connections.put(direction, location);
    }

    public Location getConnection(String direction) {
        return connections.get(direction);
    }

    public Map<String, Location> getConnections() {
        return connections;
    }
}
