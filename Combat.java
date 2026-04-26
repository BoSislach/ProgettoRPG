import java.util.Random;
import java.util.Scanner;

public class Combat {
    private final GameCharacter character;
    private final Enemy enemy;
    private final Location location;
    private final Random random;
    private final Scanner scanner;
    private boolean characterTurn;
    private boolean combatFinished = false;
    private int chargesFire;
    private int chargesLightning;
    private int chargesIce;
    private int burnTurns = 0;
    private int freezeTurns = 0;

    public Combat(
            GameCharacter character,
            Enemy enemy,
            Location location,
            Scanner scanner,
            int chargesFire,
            int chargesLightning,
            int chargesIce) {
        this.character = character;
        this.enemy = enemy;
        this.location = location;
        this.random = new Random();
        this.scanner = scanner;
        this.characterTurn = true;
        this.chargesFire = chargesFire;
        this.chargesLightning = chargesLightning;
        this.chargesIce = chargesIce;
        System.out.println("\nYou attack first!");
    }

    public boolean executeTurn() {
        if (combatFinished) {
            return true;
        }

        if (characterTurn) {
            playerTurn();
        } else {
            enemyTurn();
        }

        if (combatFinished) {
            return true;
        }

        if (!character.isAlive() || !enemy.isAlive()) {
            combatFinished = true;
            endCombat();
            return true;
        }

        characterTurn = !characterTurn;
        return false;
    }

    private void playerTurn() {
        System.out.println("\nYOUR TURN");
        System.out.println("You: " + character.getHealth() + "/" + character.getMaxHealth() + " HP");
        System.out.println(enemy.getName() + ": " + enemy.getHealth() + " HP");
        System.out.println("\n1. Attack | 2. Defend | 3. Potion | 4. Magic | 5. Flee");
        printTurnSuggestion();
        System.out.println("Type 1/2/3/4/5 or attack/defend/potion/magic/flee.");
        System.out.print("> ");

        String choice = scanner.nextLine().trim().toLowerCase();
        switch (choice) {
            case "1":
            case "attack":
                performAttack();
                break;
            case "2":
            case "defend":
                character.setDefenseMode(true);
                System.out.println("You take a defensive stance!");
                break;
            case "3":
            case "potion":
                usePotion();
                break;
            case "4":
            case "magic":
            case "spell":
                useMagic();
                break;
            case "5":
            case "flee":
                if (character.flee()) {
                    System.out.println("You fled!");
                    combatFinished = true;
                } else {
                    System.out.println("You couldn't escape!");
                }
                break;
            default:
                System.out.println("Invalid choice, you lose your turn!");
        }

        if (!combatFinished) {
            character.decrementPotionTurns();
        }
    }

    private void printTurnSuggestion() {
        boolean lowHp = character.getHealth() <= (character.getMaxHealth() / 2);
        boolean enemyAlmostDead = enemy.getHealth() <= 25;

        if (lowHp && hasHealPotion()) {
            System.out.println("Tip: your health is low, consider potion.");
            return;
        }
        if (enemyAlmostDead) {
            System.out.println("Tip: the enemy is near defeat, attack now.");
            return;
        }
        if (hasAnyPotion()) {
            System.out.println("Tip: you can power up or heal with potions.");
            return;
        }
        if (chargesFire + chargesLightning + chargesIce > 0) {
            System.out.println("Tip: you still have magic charges.");
            return;
        }
        System.out.println("Tip: alternate between attack and defend.");
    }

    private void useMagic() {
        System.out.println("\nSpells:");
        System.out.println("  [1] Fireball (" + chargesFire + "x) -> 30 damage + burn");
        System.out.println("  [2] Sky Lightning (" + chargesLightning + "x) -> 25 instant damage");
        System.out.println("  [3] Frost Wave (" + chargesIce + "x) -> freeze + frost damage");
        System.out.println("  [0] Cancel");
        System.out.print("> ");
        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
            case "fireball":
                castFireball();
                break;
            case "2":
            case "lightning":
            case "sky":
                castSkyLightning();
                break;
            case "3":
            case "frost":
                castFrostWave();
                break;
            case "0":
            case "cancel":
                System.out.println("Magic canceled.");
                break;
            default:
                System.out.println("Invalid spell choice.");
        }
    }

    private void castFireball() {
        if (chargesFire <= 0) {
            System.out.println("Fireball exhausted.");
            return;
        }
        chargesFire--;
        enemy.takeDamage(30);
        burnTurns = 5;
        System.out.println("Fireball hits for 30 damage. Burn applied for 5 turns.");
    }

    private void castSkyLightning() {
        if (chargesLightning <= 0) {
            System.out.println("Sky Lightning exhausted.");
            return;
        }
        chargesLightning--;
        enemy.takeDamage(25);
        System.out.println("Sky Lightning hits for 25 damage.");
    }

    private void castFrostWave() {
        if (chargesIce <= 0) {
            System.out.println("Frost Wave exhausted.");
            return;
        }
        chargesIce--;
        freezeTurns = 5;
        System.out.println("Frost Wave cast. Enemy frozen for 5 turns.");
    }

    private boolean hasAnyPotion() {
        for (Transportable t : character.getInventory()) {
            if (t instanceof Potion) {
                return true;
            }
        }
        return false;
    }

    private boolean hasHealPotion() {
        for (Transportable t : character.getInventory()) {
            if (t instanceof Heal) {
                return true;
            }
        }
        return false;
    }

    private void enemyTurn() {
        System.out.println("\n--- " + enemy.getName().toUpperCase() + "'S TURN ---");

        if (burnTurns > 0) {
            enemy.takeDamage(5);
            burnTurns--;
            System.out.println(enemy.getName() + " burns for 5 damage. (" + burnTurns + " burn turns left)");
            if (!enemy.isAlive()) {
                return;
            }
        }

        if (freezeTurns > 0) {
            enemy.takeDamage(3);
            freezeTurns--;
            System.out.println(enemy.getName() + " is frozen and takes 3 damage. (" + freezeTurns + " freeze turns left)");
            if (!enemy.isAlive()) {
                return;
            }
            System.out.println(enemy.getName() + " can't move!");
            return;
        }

        if (enemy.getHealth() < 30 && random.nextInt(100) < 30) {
            System.out.println(enemy.getName() + " tries to flee... but can't make it!");
        }

        int damage = enemy.attack();
        System.out.println(enemy.getName() + " attacks you for " + damage + " damage!");
        character.takeDamage(damage);

        if (!character.isAlive()) {
            int reviveHp = Math.max(10, (int) (character.getMaxHealth() * 0.15));
            character.setAlive(true);
            character.setHealth(reviveHp);
            System.out.println("You almost collapse, but get back up with " + reviveHp + " HP!");
        }
    }

    private void performAttack() {
        System.out.println("\nAvailable weapons:");
        int weaponCount = 0;
        for (int i = 0; i < character.getInventory().size(); i++) {
            Transportable t = character.getInventory().get(i);
            if (t instanceof Weapon) {
                Weapon w = (Weapon) t;
                System.out.println("[" + i + "] " + w.getName() + " (Damage: " + w.getDamage() + ")");
                weaponCount++;
            }
        }

        if (weaponCount == 0) {
            System.out.println("No weapons. You strike with bare fists.");
            int damage = character.getAttack() / 2;
            enemy.takeDamage(damage);
            return;
        }

        System.out.print("Choose weapon (number) or ENTER for fists: ");
        String choice = scanner.nextLine().trim();

        if (choice.isEmpty()) {
            int damage = character.getAttack() / 2;
            enemy.takeDamage(damage);
            System.out.println("You strike with bare fists for " + damage + " damage!");
            return;
        }

        try {
            int idx = Integer.parseInt(choice);
            if (idx >= 0 && idx < character.getInventory().size()) {
                Transportable t = character.getInventory().get(idx);
                if (t instanceof Weapon) {
                    Weapon weapon = (Weapon) t;
                    if (!weapon.getWeaponState()) {
                        System.out.println("The weapon is broken. You use fists.");
                        enemy.takeDamage(character.getAttack() / 2);
                    } else if (character.getStrength() < weapon.getMinStrength()) {
                        System.out.println("Not enough strength. You use fists.");
                        enemy.takeDamage(character.getAttack() / 2);
                    } else {
                        int damage = character.attackEnemy(enemy, weapon);
                        System.out.println("You strike with " + weapon.getName() + " for " + damage + " damage!");
                    }
                } else {
                    System.out.println("Not a weapon. You use fists.");
                    enemy.takeDamage(character.getAttack() / 2);
                }
            } else {
                System.out.println("Invalid weapon index. You use fists.");
                enemy.takeDamage(character.getAttack() / 2);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. You use fists.");
            enemy.takeDamage(character.getAttack() / 2);
        }
    }

    private void usePotion() {
        String potions = character.showPotions();
        if (potions.contains("No potions")) {
            System.out.println("You have no potions.");
            return;
        }
        System.out.println(potions);
        System.out.print("Choose potion index: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim());
            if (idx < 0 || idx >= character.getInventory().size()) {
                System.out.println("Invalid number.");
                return;
            }

            Transportable item = character.getInventory().get(idx);
            if (item instanceof PoisonPotion) {
                PoisonPotion poison = (PoisonPotion) item;
                enemy.takeDamage(poison.getDamage());
                character.removeItem(poison);
                System.out.println("You throw " + poison.getName() + " and deal " + poison.getDamage() + " damage!");
                return;
            }

            if (character.usePotion(idx)) {
                System.out.println("Potion used!");
            } else {
                System.out.println("Can't use it now.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private void endCombat() {
        if (!character.isAlive()) {
            System.out.println("\nYou died...");
            character.die(location);
            return;
        }
        if (!enemy.isAlive()) {
            System.out.println("\nYou defeated " + enemy.getName() + "!");
            enemy.die(location);
            character.transferExperience(enemy);
        }
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Location getLocation() {
        return location;
    }

    public int getChargesFire() {
        return chargesFire;
    }

    public int getChargesLightning() {
        return chargesLightning;
    }

    public int getChargesIce() {
        return chargesIce;
    }
}
