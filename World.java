import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class World {
    private String name;
    private String description;
    private ArrayList<Location> locations;
    private Location currentPosition;
    private GameCharacter protagonist;

    public World(String name, String description, GameCharacter protagonist) {
        this.name = name;
        this.description = description;
        this.locations = new ArrayList<>();
        this.protagonist = protagonist;
    }

    public void addLocation(Location location) {
        locations.add(location);
        if (locations.size() == 1) {
            currentPosition = location;
            location.addCharacter(protagonist);
        }
    }

    public Location getCurrentPosition() {
        return currentPosition;
    }

    public boolean move(String direction) {
        Location next = currentPosition.getConnection(direction);
        if (next != null) {
            currentPosition.removeCharacter(protagonist);
            next.addCharacter(protagonist);
            currentPosition = next;
            return true;
        }
        return false;
    }

    public GameCharacter getProtagonist() {
        return protagonist;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public Location findLocationByName(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }
        String q = query.trim().toLowerCase();
        for (Location location : locations) {
            String locName = location.getName().toLowerCase();
            if (locName.equals(q) || locName.contains(q)) {
                return location;
            }
        }
        return null;
    }

    public String suggestFirstStep(Location start, Location target) {
        if (start == null || target == null || start == target) {
            return null;
        }

        Queue<Location> queue = new ArrayDeque<>();
        Set<Location> visited = new HashSet<>();
        Map<Location, String> firstStep = new HashMap<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Location current = queue.poll();
            for (Map.Entry<String, Location> exit : current.getConnections().entrySet()) {
                String command = exit.getKey();
                Location next = exit.getValue();
                if (!visited.add(next)) {
                    continue;
                }

                if (current == start) {
                    firstStep.put(next, command);
                } else {
                    firstStep.put(next, firstStep.get(current));
                }

                if (next == target) {
                    return firstStep.get(next);
                }
                queue.add(next);
            }
        }
        return null;
    }
}
