import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Game {
    private static final Set<String> MAIN_BOSSES = Set.of(
            "Luca Vassena", "The Junkie", "The Wall", "The Bangla");

    private final Scanner scanner = new Scanner(System.in);
    private final World world;
    private final GameCharacter protagonist;

    private final Set<String> visitedLocations = new HashSet<>();
    private final Set<String> defeatedEnemies = new HashSet<>();

    private boolean inCombat = false;
    private boolean gameFinished = false;
    private Combat currentCombat = null;
    private Location previousLocation = null;
    private String endingEpilogue = "";
    private int chargesFire = 2;
    private int chargesLightning = 1;
    private int chargesIce = 1;

    public Game(World world) {
        this.world = world;
        this.protagonist = world.getProtagonist();
    }

    public void start() {
        printIntro();
        while (protagonist.isAlive() && !gameFinished) {
            if (inCombat && currentCombat != null) {
                handleCombat();
            } else {
                handleExploration();
            }
        }
        printConclusion();
        scanner.close();
    }

    private void printIntro() {
        System.out.println("======================================");
        System.out.println("      Roma Street: STREET WAR      ");
        System.out.println("======================================");
        System.out.println("protagonist: " + protagonist.getName());
        System.out.println("Key commands: exit number, go [exit], inv, fight, objective");
        System.out.println("======================================");
    }

    private void printConclusion() {
        System.out.println("\n======================================");
        if (protagonist.isAlive() && gameFinished) {
            System.out.println("              END OF ADVENTURE          ");
            System.out.println(endingEpilogue);
        } else {
            System.out.println("               GAME OVER              ");
            System.out.println(protagonist.getName() + " died in the street war.");
        }
        System.out.println("======================================");
    }

    private void handleExploration() {
        Location currentLocation = world.getCurrentPosition();
        showEntryEvent(currentLocation);

        List<GameCharacter> enemies = currentLocation.getEnemies(protagonist);
        showLocation(currentLocation, enemies);
        List<String> exits = showExits(currentLocation);
        showQuickActions(currentLocation, enemies, exits);

        System.out.print("> ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return;
        }

        if (handleCommand(currentLocation, enemies, exits, input)) {
            return;
        }

        System.out.println("Invalid command. Type 'help'.");
    }

    private void showLocation(Location Location, List<GameCharacter> enemies) {
        System.out.println("\n[" + Location.getName().toUpperCase() + "]");
        System.out.println(Location.getDescription());

        if (!Location.getItems().isEmpty()) {
            System.out.println("Items:");
            for (int i = 0; i < Location.getItems().size(); i++) {
                Item item = Location.getItems().get(i);
                System.out.println("  [" + i + "] " + item.getName());
            }
        }

        if (!enemies.isEmpty()) {
            System.out.println("Enemies:");
            for (GameCharacter enemy : enemies) {
                System.out.println(
                        "  - " + enemy.getName() + " [" + enemy.getHealth() + "/" + enemy.getMaxHealth() + "]");
            }
        }

        if (!Location.getLockableContainers().isEmpty()) {
            System.out.println("Containers:");
            for (int i = 0; i < Location.getLockableContainers().size(); i++) {
                LockableContainer container = Location.getLockableContainers().get(i);
                String status = container.isLocked() ? "LOCKED" : "OPEN";
                String needsKey = container.isRequiresKey() ? " (Key)" : "";
                System.out.println("  [" + i + "] [" + status + "]" + needsKey + " " + container.getName());
            }
        }

        System.out.println("Status: HP " + protagonist.getHealth() + "/" + protagonist.getMaxHealth()
                + " | ATK " + protagonist.getAttack()
                + " | DEF " + protagonist.getDefense()
                + " | XP " + protagonist.getExperience());
    }

    private List<String> showExits(Location Location) {
        LinkedHashMap<String, Location> uniqueExits = new LinkedHashMap<>();
        for (Map.Entry<String, Location> exitEntry : Location.getConnections().entrySet()) {
            if (!uniqueExits.containsValue(exitEntry.getValue())) {
                uniqueExits.put(exitEntry.getKey(), exitEntry.getValue());
            }
        }

        List<String> commands = new ArrayList<>(uniqueExits.keySet());
        System.out.println("Exits:");
        if (commands.isEmpty()) {
            System.out.println("  (none)");
            return commands;
        }

        for (int i = 0; i < commands.size(); i++) {
            String cmd = commands.get(i);
            System.out.println("  [" + (i + 1) + "] " + cmd + " -> " + uniqueExits.get(cmd).getName());
        }
        return commands;
    }

    private void showQuickActions(Location Location, List<GameCharacter> enemies, List<String> exits) {
        System.out.println("Actions:");
        if (!exits.isEmpty()) {
            System.out.println("  - Move: " + exits.get(0) + " or 1");
        }
        if (!Location.getItems().isEmpty()) {
            System.out.println("  - Pick up item: take 0");
        }
        if (!Location.getLockableContainers().isEmpty()) {
            System.out.println("  - Open container: open 0");
            System.out.println("  - Loot container: loot 0");
        }
        if (!enemies.isEmpty()) {
            System.out.println("  - Fight: fight");
        }
        System.out.println("  - Inventory: inv");
    }

    private boolean handleCommand(Location Location, List<GameCharacter> enemies, List<String> exits, String input) {
        String command = input.toLowerCase();
        String[] parts = command.split("\\s+", 2);
        String action = parts[0];
        String arg = parts.length > 1 ? parts[1].trim() : "";

        switch (action) {
            case "exit":
            case "quit":
                gameFinished = true;
                endingEpilogue = "You left the story before the epilogue.";
                return true;
            case "look":
            case "l":
                return true;
            case "inv":
            case "inventory":
            case "i":
                showInventory();
                return true;
            case "stats":
            case "statistics":
                showStats();
                return true;
            case "help":
            case "?":
                showHelp();
                return true;
            case "objective":
            case "objectives":
            case "mission":
                showObjectives();
                return true;
            case "ending":
            case "end":
            case "finale":
                attemptEnding(Location);
                return true;
            case "take":
            case "pick":
                takeItem(Location, arg);
                return true;
            case "open":
                openContainer(Location, arg);
                return true;
            case "loot":
                lootContainer(Location, arg);
                return true;
            case "drop":
            case "leave":
                dropItem(Location, arg);
                return true;
            case "talk":
            case "speak":
                talk(Location, arg);
                return true;
            case "use":
            case "drink":
                useItem(arg);
                return true;
            case "equip":
            case "wear":
                equip(arg);
                return true;
            case "fight":
            case "attack":
                startCombat(Location, arg);
                return true;
            default:
                return handleMovement(Location, exits, input);
        }
    }

    private boolean handleMovement(Location currentLocation, List<String> exits, String inputRaw) {
        String input = sanitizeMovementInput(inputRaw.toLowerCase());
        if (input.isEmpty()) {
            return false;
        }

        if (input.equals("back") || input.equals("return")) {
            return goBack(currentLocation);
        }

        if (input.matches("\\d+")) {
            int idx = Integer.parseInt(input) - 1;
            if (idx < 0 || idx >= exits.size()) {
                System.out.println("Invalid exit index.");
                return true;
            }
            return moveTo(currentLocation, exits.get(idx));
        }

        if (currentLocation.getConnections().containsKey(input)) {
            return moveTo(currentLocation, input);
        }

        String prefix = matchExitPrefix(currentLocation, input);
        if (prefix != null) {
            return moveTo(currentLocation, prefix);
        }

        String toDestination = matchAdjacentDestination(currentLocation, input);
        if (toDestination != null) {
            return moveTo(currentLocation, toDestination);
        }

        Location destination = world.findLocationByName(input);
        if (destination != null) {
            String firstStep = world.suggestFirstStep(currentLocation, destination);
            if (firstStep == null) {
                System.out.println("There is no path from here to " + destination.getName() + ".");
            } else {
                System.out.println("You can't go directly to " + destination.getName() + ".");
                System.out.println("Suggested first step: " + firstStep);
            }
            return true;
        }

        return false;
    }

    private String sanitizeMovementInput(String input) {
        String cleaned = input.trim();
        String[] prefixes = { "go ", "go ", "move ", "move ", "v ", "toward " };
        for (String prefix : prefixes) {
            if (cleaned.startsWith(prefix)) {
                return cleaned.substring(prefix.length()).trim();
            }
        }
        return cleaned;
    }

    private String matchExitPrefix(Location Location, String input) {
        for (String exitEntry : Location.getConnections().keySet()) {
            if (exitEntry.toLowerCase().startsWith(input)) {
                return exitEntry;
            }
        }
        return null;
    }

    private String matchAdjacentDestination(Location Location, String input) {
        for (Map.Entry<String, Location> entry : Location.getConnections().entrySet()) {
            String destinationName = entry.getValue().getName().toLowerCase();
            if (destinationName.equals(input) || destinationName.contains(input)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean moveTo(Location currentLocation, String exitCommand) {
        if (isProgressionBlocked(currentLocation, exitCommand)) {
            return true;
        }
        if (!world.move(exitCommand)) {
            return false;
        }
        previousLocation = currentLocation;
        Location newLocation = world.getCurrentPosition();
        System.out.println("You move toward " + newLocation.getName() + ".");
        if ("Mix Market".equals(newLocation.getName()) && !gameFinished) {
            attemptEnding(newLocation);
        }
        return true;
    }

    private boolean isProgressionBlocked(Location currentLocation, String direction) {
        String currentName = currentLocation.getName();
        if ("Roma Street - Brawl".equals(currentName) && !defeatedEnemies.contains("Luca Vassena")) {
            if (!"back".equals(direction)) {
                System.out.println("You must defeat Luca Vassena before proceeding.");
                return true;
            }
        } else if ("Underground Parking".equals(currentName)
                && (!defeatedEnemies.contains("The Junkie") || !defeatedEnemies.contains("The Wall"))) {
            if (!"back".equals(direction)) {
                System.out.println("You must defeat The Junkie and The Wall before proceeding.");
                return true;
            }
        } else if ("Building Rooftop".equals(currentName) && !defeatedEnemies.contains("The Bangla")) {
            if (!"down".equals(direction) && !"descend".equals(direction)) {
                System.out.println("You must defeat The Bangla before proceeding.");
                return true;
            }
        }
        return false;
    }

    private boolean goBack(Location currentLocation) {
        if (previousLocation == null) {
            System.out.println("You don't have a previous room.");
            return true;
        }
        for (Map.Entry<String, Location> entry : currentLocation.getConnections().entrySet()) {
            if (entry.getValue() == previousLocation) {
                return moveTo(currentLocation, entry.getKey());
            }
        }
        System.out.println("Cannot go back directly to the previous room from here.");
        return true;
    }

    private void startCombat(Location loc, String targetName) {
        GameCharacter combatTarget = findEnemy(loc, targetName);
        if (combatTarget == null) {
            System.out.println("No enemies to fight.");
            return;
        }

        Enemy enemy = new Enemy(combatTarget.getName(), combatTarget.getHealth(), combatTarget.getAttack());
        enemy.setHealth(combatTarget.getHealth());

        currentCombat = new Combat(protagonist, enemy, loc, scanner, chargesFire, chargesLightning, chargesIce);
        inCombat = true;
    }

    private GameCharacter findEnemy(Location Location, String filtro) {
        List<GameCharacter> enemies = Location.getEnemies(protagonist);
        if (enemies.isEmpty()) {
            return null;
        }
        if (filtro == null || filtro.isBlank()) {
            return enemies.get(0);
        }
        String f = filtro.toLowerCase();
        for (GameCharacter enemy : enemies) {
            if (enemy.getName().toLowerCase().contains(f)) {
                return enemy;
            }
        }
        return enemies.get(0);
    }

    private void handleCombat() {
        if (!currentCombat.executeTurn()) {
            return;
        }

        Enemy enemy = currentCombat.getEnemy();
        Location Location = currentCombat.getLocation();
        chargesFire = currentCombat.getChargesFire();
        chargesLightning = currentCombat.getChargesLightning();
        chargesIce = currentCombat.getChargesIce();

        if (!enemy.isAlive()) {
            GameCharacter defeated = null;
            for (GameCharacter p : Location.getCharacters()) {
                if (p != protagonist && p.getName().equals(enemy.getName())) {
                    defeated = p;
                    break;
                }
            }
            if (defeated != null) {
                defeated.setAlive(false);
                defeated.setHealth(0);
                Location.removeCharacter(defeated);
            }
            defeatedEnemies.add(enemy.getName());
            dropBossLoot(enemy.getName(), Location);
        }

        inCombat = false;
        currentCombat = null;
        if (protagonist.isAlive()) {
            System.out.println("--- End of Combat ---");
            System.out.print("Press ENTER...");
            scanner.nextLine();
        }
    }

    private void dropBossLoot(String bossName, Location location) {
        Item loot = null;
        if ("Luca Vassena".equals(bossName)) {
            loot = new Item("Mysterious Can", "The old man said: 'If things go bad, drink it.'", 1, 0, true);
        } else if ("The Junkie".equals(bossName)) {
            loot = new Item("Electronic Key", "Opens blocked passages and improvised safes.", 2, 0, true);
        } else if ("The Wall".equals(bossName)) {
            loot = new Item("Contact Dossier", "Names, debts, deliveries. Worth more than a gun.", 3, 0, true);
        } else if ("The Bangla".equals(bossName)) {
            loot = new Item("Intercity Pass", "Open ticket to leave the city tonight.", 1, 0, true);
        }

        if (loot == null) {
            return;
        }
        if (protagonist.addItem(loot)) {
            System.out.println("Loot added to inventory: " + loot.getName());
        } else {
            location.addItem(loot);
            System.out.println("Loot dropped on ground: " + loot.getName());
        }
    }

    private GameCharacter findEnemyByName(Location Location, String name) {
        for (GameCharacter p : Location.getEnemies(protagonist)) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    private void showInventory() {
        List<Transportable> inventoryItems = protagonist.getInventory();
        System.out.println("\nInventory:");
        if (inventoryItems.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (int i = 0; i < inventoryItems.size(); i++) {
                Transportable item = inventoryItems.get(i);
                System.out.println("  [" + i + "] " + itemType(item) + " " + item.getName());
            }
        }
        System.out.println("Weight: " + protagonist.getInventoryWeight() + "/" + protagonist.getMaxInventoryWeight());
    }

    private String itemType(Transportable item) {
        if (item instanceof Weapon) {
            return "[Weapon]";
        }
        if (item instanceof Shield) {
            return "[Shield]";
        }
        if (item instanceof Potion) {
            return "[Potion]";
        }
        return "[ITEM]";
    }

    private void showStats() {
        System.out.println("\n" + protagonist.getName() + " - Level " + protagonist.getLevel());
        System.out.println("HP: " + protagonist.getHealth() + "/" + protagonist.getMaxHealth());
        System.out.println("Strength: " + protagonist.getStrength() + "/" + protagonist.getMaxStrength());
        System.out.println("Attack: " + protagonist.getAttack() + "/" + protagonist.getMaxAttack());
        System.out.println("Defense: " + protagonist.getDefense() + "/" + protagonist.getMaxDefense());
        System.out.println("Experience: " + protagonist.getExperience());
    }

    private void showHelp() {
        System.out.println("\nCommands:");
        System.out.println("  movement: [exit number], [exit name], go [exit], back");
        System.out.println("  actions: look, inv, stats, take, drop, use, equip, talk, fight");
        System.out.println("  story: objective, ending");
        System.out.println("  system: exit");
    }

    private void takeItem(Location Location, String argument) {
        if (Location.getItems().isEmpty()) {
            System.out.println("There are no items here.");
            return;
        }
        Integer idx = readIndex(argument, "Item index: ");
        if (idx == null || idx < 0 || idx >= Location.getItems().size()) {
            System.out.println("Invalid index.");
            return;
        }

        Item item = Location.getItems().get(idx);
        if (!item.isTransportable()) {
            System.out.println("Object not transportable.");
            return;
        }
        if (!protagonist.addItem(item)) {
            System.out.println("Inventory full or too heavy.");
            return;
        }
        Location.removeItem(item);
        System.out.println("Picked up: " + item.getName());
    }

    private void openContainer(Location Location, String argument) {
        List<LockableContainer> containers = Location.getLockableContainers();
        if (containers.isEmpty()) {
            System.out.println("There are no containers here.");
            return;
        }
        Integer idx = readIndex(argument, "Container index: ");
        if (idx == null || idx < 0 || idx >= containers.size()) {
            System.out.println("Invalid index.");
            return;
        }

        LockableContainer container = containers.get(idx);
        if (!container.isLocked()) {
            System.out.println(container.getName() + " is already open.");
            return;
        }

        if (container.isRequiresKey() && !protagonist.hasItem("Electronic Key")) {
            System.out.println(container.getName() + " is locked. You need the Electronic Key.");
            return;
        }

        container.openWithKey();
        System.out.println("Opened: " + container.getName());
    }

    private void lootContainer(Location Location, String argument) {
        List<LockableContainer> containers = Location.getLockableContainers();
        if (containers.isEmpty()) {
            System.out.println("There are no containers here.");
            return;
        }
        Integer idx = readIndex(argument, "Container index: ");
        if (idx == null || idx < 0 || idx >= containers.size()) {
            System.out.println("Invalid index.");
            return;
        }

        LockableContainer container = containers.get(idx);
        if (container.isLocked()) {
            System.out.println(container.getName() + " is locked.");
            return;
        }

        Item loot = container.getContainedItem();
        if (loot == null) {
            System.out.println(container.getName() + " is empty.");
            return;
        }
        if (!protagonist.addItem(loot)) {
            System.out.println("Inventory full or too heavy.");
            return;
        }

        container.removeItem();
        System.out.println("Taken from " + container.getName() + ": " + loot.getName());
    }

    private void dropItem(Location Location, String argument) {
        if (protagonist.getInventory().isEmpty()) {
            System.out.println("Inventory empty.");
            return;
        }
        Integer idx = readIndex(argument, "Inventory index: ");
        if (idx == null || idx < 0 || idx >= protagonist.getInventory().size()) {
            System.out.println("Invalid index.");
            return;
        }

        Transportable item = protagonist.getInventory().get(idx);
        protagonist.removeItem(item);
        if (item instanceof Item) {
            Location.addItem((Item) item);
        }
        System.out.println("Dropped: " + item.getName());
    }

    private void talk(Location Location, String filtro) {
        List<GameCharacter> enemies = Location.getEnemies(protagonist);
        if (enemies.isEmpty()) {
            System.out.println("There is nobody to talk to.");
            return;
        }

        for (GameCharacter enemy : enemies) {
            if (filtro != null && !filtro.isBlank()
                    && !enemy.getName().toLowerCase().contains(filtro.toLowerCase())) {
                continue;
            }
            System.out.println(enemy.getName() + ": \"Don't underestimate me.\"");
            return;
        }
        System.out.println("No one matches '" + filtro + "'.");
    }

    private void useItem(String argument) {
        if (protagonist.getInventory().isEmpty()) {
            System.out.println("Inventory empty.");
            return;
        }

        Integer idx = readIndex(argument, "Inventory index: ");
        if (idx == null || idx < 0 || idx >= protagonist.getInventory().size()) {
            System.out.println("Invalid index.");
            return;
        }

        Transportable item = protagonist.getInventory().get(idx);
        if (item instanceof PoisonPotion) {
            System.out.println("PoisonPotion can only be used in combat.");
            return;
        }

        if (item instanceof Potion) {
            boolean usedPotion = protagonist.usePotion(idx);
            System.out.println(usedPotion ? "Potion used." : "You cannot use it now.");
            return;
        }

        if (item instanceof Item) {
            useGenericItem((Item) item);
            return;
        }

        System.out.println("Object not usable.");
    }

    private void useGenericItem(Item item) {
        String itemName = item.getName().toLowerCase();
        if (itemName.contains("kebab")) {
            protagonist.setHealth(protagonist.getHealth() + 20);
            protagonist.removeItem(item);
            System.out.println("You recovered 20 HP.");
            return;
        }
        if (itemName.contains("can")) {
            protagonist.setHealth(protagonist.getMaxHealth());
            protagonist.gainExperience(10);
            protagonist.removeItem(item);
            System.out.println("Health maxed and +10 XP.");
            return;
        }
        if (itemName.contains("kit") || itemName.contains("bandage")) {
            protagonist.setHealth(protagonist.getHealth() + 15);
            protagonist.removeItem(item);
            System.out.println("You recovered 15 HP.");
            return;
        }
        System.out.println("No effect.");
    }

    private void equip(String argument) {
        if (protagonist.getInventory().isEmpty()) {
            System.out.println("Inventory empty.");
            return;
        }
        Integer idx = readIndex(argument, "Inventory index: ");
        if (idx == null || idx < 0 || idx >= protagonist.getInventory().size()) {
            System.out.println("Invalid index.");
            return;
        }
        Transportable item = protagonist.getInventory().get(idx);
        if (!(item instanceof Shield)) {
            System.out.println("You can equip shields only.");
            return;
        }
        if (!protagonist.equipShield((Shield) item)) {
            System.out.println("You already have a shield equipped.");
            return;
        }
        protagonist.removeItem(item);
        System.out.println("Shield equipped: " + item.getName());
    }

    private Integer readIndex(String argument, String prompt) {
        if (argument != null && !argument.isBlank()) {
            try {
                return Integer.parseInt(argument.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void showObjectives() {
        int count = 0;
        System.out.println("\nMain bosses:");
        for (String boss : MAIN_BOSSES) {
            boolean completed = defeatedEnemies.contains(boss);
            if (completed) {
                count++;
            }
            System.out.println("  [" + (completed ? "X" : " ") + "] " + boss);
        }
        System.out.println("Completed: " + count + "/" + MAIN_BOSSES.size());

        System.out.println("Key items:");
        if (protagonist.hasItem("Electronic Key")) {
            System.out.println("  [X] Electronic Key");
        } else {
            System.out.println("  [ ] Electronic Key");
        }
        if (protagonist.hasItem("Contact Dossier")) {
            System.out.println("  [X] Contact Dossier");
        } else {
            System.out.println("  [ ] Contact Dossier");
        }
        if (protagonist.hasItem("Intercity Pass")) {
            System.out.println("  [X] Intercity Pass");
        } else {
            System.out.println("  [ ] Intercity Pass");
        }
        if (protagonist.hasItem("Mysterious Can")) {
            System.out.println("  [X] Mysterious Can");
        } else {
            System.out.println("  [ ] Mysterious Can");
        }
    }

    private void attemptEnding(Location currentLocation) {
        if (!"Mix Market".equals(currentLocation.getName())) {
            System.out.println("You can choose the ending only at Mix Market.");
            return;
        }

        System.out.println("\n=== CHOOSE YOUR ENDING ===");
        System.out.println("1) Justice - Bring down the criminal network");
        System.out.println("2) Power   - Take control of Roma Street");
        System.out.println("3) Escape  - Leave the city and survive");
        System.out.println("0) Cancel");
        System.out.print("> ");
        String choice = scanner.nextLine().trim();

        int defeatedBosses = countDefeatedBosses();
        boolean hasKey = protagonist.hasItem("Electronic Key");
        boolean hasDossier = protagonist.hasItem("Contact Dossier");
        boolean hasCan = protagonist.hasItem("Mysterious Can");
        boolean hasPass = protagonist.hasItem("Intercity Pass");

        switch (choice) {
            case "1":
                if (defeatedBosses >= 3 && hasKey && hasDossier) {
                    endingEpilogue = "Justice Ending: the criminal network collapses.";
                } else {
                    endingEpilogue = "Failed Ending: decisive evidence is missing.";
                }
                gameFinished = true;
                break;
            case "2":
                if (defeatedBosses >= 3 && hasCan) {
                    endingEpilogue = "Power Ending: take control of Roma Street.";
                } else {
                    endingEpilogue = "Broken Ambition Ending: not enough power to command.";
                }
                gameFinished = true;
                break;
            case "3":
                if (hasPass) {
                    endingEpilogue = "Escape Ending: you leave the city and survive.";
                } else {
                    endingEpilogue = "Dead End Ending: without a pass you remain trapped.";
                }
                gameFinished = true;
                break;
            case "0":
                System.out.println("Ending selection canceled.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private int countDefeatedBosses() {
        int count = 0;
        for (String boss : MAIN_BOSSES) {
            if (defeatedEnemies.contains(boss)) {
                count++;
            }
        }
        return count;
    }

    private void showEntryEvent(Location Location) {
        if (!visitedLocations.add(Location.getName())) {
            return;
        }
        switch (Location.getName()) {
            case "Roma Street - Brawl":
                System.out.println("\n[Event] The brawl erupts: Luca Vassena targets you.");
                break;
            case "Underground Parking":
                System.out.println("\n[Event] The Junkie and The Wall rule this place.");
                break;
            case "Building Rooftop":
                System.out.println("\n[Event] The Bangla controls everything from above.");
                break;
            case "Mix Market":
                System.out.println("\n[Event] Point of no return: choose your ending here.");
                break;
            default:
                break;
        }
    }
}
