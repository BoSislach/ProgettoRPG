import java.util.ArrayList;
import java.util.Random;

public class GameCharacter {
    private String name;
    private String description;
    private int health;
    private int maxHealth;
    private int strength;
    private int maxStrength;
    private int attack;
    private int maxAttack;
    private int defense;
    private int maxDefense;
    private int experience;
    private int level;
    private ArrayList<Transportable> inventory;
    private int inventoryWeight;
    private final int maxInventoryWeightBase = 100;
    private boolean isAlive;
    private Shield equippedShield;
    private boolean defenseMode;
    private boolean potionActive;
    private int attackPrePotion;
    private int defensePrePotion;
    private int strengthPrePotion;
    private int potionTurns;
    private ArrayList<Spell> unlockedSpells = new ArrayList<>();
    private boolean hasUnlockedSpells = false;

    public GameCharacter(String name, String description, int maxHealth, int maxStrength, int maxAttack,int maxDefense) {
        this.name = name;
        this.description = description;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.maxStrength = maxStrength;
        this.strength = maxStrength;
        this.maxAttack = maxAttack;
        this.attack = maxAttack;
        this.maxDefense = maxDefense;
        this.defense = maxDefense;
        this.experience = 0;
        this.level = 1;
        this.inventory = new ArrayList<>();
        this.inventoryWeight = 0;
        this.isAlive = true;
        this.defenseMode = false;
        this.potionActive = false;
        this.potionTurns = 0;
    }

    public String showUnlockedSpells() {
        if (unlockedSpells.isEmpty()) {
            return "   No spells unlocked.";
        }
        String s = "";
        for (Spell spell : unlockedSpells) {
            s += "   • " + spell.getName() + " (" + spell.getDescription() + ")\n";
        }
        return s;
    }

    public int getMaxInventoryWeight() {
        return maxInventoryWeightBase + strength;
    }

    public int getInventoryWeight() {
        return inventoryWeight;
    }

    private void checkExperience() {
        if (experience >= 50) {
            experience -= 50;
            level++;

            maxHealth = (int) Math.round(maxHealth * 1.2);
            maxStrength = (int) Math.round(maxStrength * 1.2);
            maxAttack = (int) Math.round(maxAttack * 1.2);
            maxDefense = (int) Math.round(maxDefense * 1.2);

            health = maxHealth;
            strength = maxStrength;
            attack = maxAttack;
            defense = maxDefense;

            System.out.println("⭐ LEVEL UP! You reached level " + level + "!");
            System.out.println("   All stats increased by 20%!");
        }
    }

    public void takeDamage(int damage) {
        int damageTaken = damage - defense;
        if (damageTaken < 0)
            damageTaken = 0;

        if (equippedShield != null && equippedShield.getShieldState()) {
            Random random = new Random();
            int percentage;

            if (potionActive) {
                percentage = 20 + random.nextInt(31);
            } else if (defenseMode) {
                percentage = 50 + random.nextInt(51);
            } else {
                percentage = 20 + random.nextInt(31);
            }

            int shieldReduction = (equippedShield.getDefense() * percentage) / 100;
            damageTaken -= shieldReduction;
            equippedShield.reduceDurability();
        }

        if (damageTaken > 0) {
            health -= damageTaken;
            if (health <= 0) {
                health = 0;
                isAlive = false;
            }
        }
        defenseMode = false; 
    }

    public boolean setMaxHealth(int maxHealth) {
        if (maxHealth <= 0) {
            return false;
        }
        this.maxHealth = maxHealth;
        if (health > this.maxHealth) {
            health = this.maxHealth;
        }
        return true;
    }

    public boolean setHealth(int health) {
        if (health < 0) {
            this.health = 0;
            isAlive = false;
            return true;
        }
        if (health > maxHealth) {
            this.health = maxHealth;
        } else {
            this.health = health;
        }
        if (this.health == 0) {
            isAlive = false;
        }
        return true;
    }

    public void gainExperience(int experience) {
        this.experience += experience;
        checkExperience();
    }

    public String showWeapons() {
        String s = "";
        for (Transportable item : inventory) {
            if (item instanceof Weapon) {
                s += "   • " + item.getName() + "\n";
            }
        }
        if (s.equals(""))
            return "   No weapons in inventory.";
        return s;
    }

    public Weapon chooseWeapon(int weaponIndex) {
        if (weaponIndex < 0 || weaponIndex >= inventory.size()) {
            return null;
        }
        if (!(inventory.get(weaponIndex) instanceof Weapon)) {
            return null;
        }
        return (Weapon) inventory.get(weaponIndex);
    }

    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        for (Transportable item : inventory) {
            System.out.println("   • " + item.getName() + " (weight: " + item.getWeight() + ")");
        }
    }

    public int attackEnemy(Enemy enemy, Weapon weapon) {
        if (weapon == null || !weapon.getWeaponState()) {
            return 0;
        }
        if (strength < weapon.getMinStrength()) {
            System.out.println("Not enough strength to use this weapon!");
            return 0;
        }

        Random random = new Random();
        int percentage;

        if (potionActive) {
            percentage = 80 + random.nextInt(51);
        } else if (defenseMode) {
            percentage = 20 + random.nextInt(31);
        } else {
            percentage = 60 + random.nextInt(41);
        }

        int damageDealt = (weapon.getDamage() * percentage) / 100;
        enemy.takeDamage(damageDealt);
        weapon.reduceDurability();
        return damageDealt;
    }

    public void die(Location currentLocation) {
        for (Transportable item : inventory) {
            if (item instanceof Item) {
                currentLocation.addItem((Item) item);
            }
        }
        inventory.clear();
        inventoryWeight = 0;

        if (equippedShield != null) {
            currentLocation.addItem(equippedShield);
            equippedShield = null;
        }
        isAlive = false;
        health = 0;
    }

    public void transferExperience(GameCharacter winner) {
        winner.gainExperience(this.experience);
        this.experience = 0;
    }

    public void setDefenseMode(boolean defenseMode) {
        this.defenseMode = defenseMode;
    }

    public boolean getDefenseMode() {
        return defenseMode;
    }

    public void setPotionActive(boolean potionActive) {
        this.potionActive = potionActive;
    }

    public boolean getPotionActive() {
        return potionActive;
    }

    public String showPotions() {
        String s = "";
        boolean found = false;
        for (int i = 0; i < inventory.size(); i++) {
            Transportable item = inventory.get(i);
            if (item instanceof Potion) {
                s += "   [" + i + "] " + item.getName() + "\n";
                found = true;
            }
        }
        if (!found)
            return "   No potions in inventory.";
        return s;
    }

    public boolean usePotion(int potionIndex) {
        if (potionIndex < 0 || potionIndex >= inventory.size()) {
            return false;
        }
        Transportable item = inventory.get(potionIndex);
        if (!(item instanceof Potion)) {
            return false;
        }
        Potion potion = (Potion) item;
        if (!potion.getPotionActive()) {
            return false;
        }

        if (potion instanceof Heal) {
            int effectiveHeal = potion.getValue() * 3;
            boolean ok = setHealth(getHealth() + effectiveHeal);
            if (ok) {
                inventoryWeight -= potion.getWeight();
                inventory.remove(potionIndex);
                System.out.println("Used: " + potion.getName() + " and recovered " + effectiveHeal + " HP!");
            }
            return ok;
        }

        if (potion instanceof PowerPotion) {
            if (potionActive) {
                System.out.println("You already have an active power potion!");
                return false;
            }
            attackPrePotion = attack;
            defensePrePotion = defense;
            strengthPrePotion = strength;
            attack += potion.getValue();
            defense += potion.getValue();
            strength += potion.getValue();
            potionTurns = potion.getDurability();
            potionActive = true;
            inventoryWeight -= potion.getWeight();
            inventory.remove(potionIndex);
            System.out.println("Used: " + potion.getName() + "! Attack, defense and strength increased!");
            return true;
        }
        return false;
    }

    public int getPotionTurns() {
        return potionTurns;
    }

    public void decrementPotionTurns() {
        if (potionActive) {
            potionTurns--;
            if (potionTurns <= 0) {
                attack = attackPrePotion;
                defense = defensePrePotion;
                strength = strengthPrePotion;
                potionActive = false;
                System.out.println("The power potion effects have worn off.");
            }
        }
    }

    public boolean addItem(Transportable item) {
        if (item == null) {
            return false;
        }
        if (inventoryWeight + item.getWeight() <= getMaxInventoryWeight()) {
            inventory.add(item);
            inventoryWeight += item.getWeight();
            return true;
        }
        return false;
    }

    public Shield getEquippedShield() {
        return equippedShield;
    }

    public boolean equipShield(Shield shield) {
        if (equippedShield == null) {
            equippedShield = shield;
            return true;
        }
        return false;
    }

    public boolean removeItem(Transportable item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            inventoryWeight -= item.getWeight();
            if (inventoryWeight < 0) {
                inventoryWeight = 0;
            }
            return true;
        }
        return false;
    }

    public void setHasUnlockedSpells(boolean hasUnlockedSpells) {
        this.hasUnlockedSpells = hasUnlockedSpells;
    }

    public boolean getHasUnlockedSpells() {
        return hasUnlockedSpells;
    }

    public ArrayList<Spell> getUnlockedSpells() {
        return unlockedSpells;
    }

    public boolean flee() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public boolean setExperience(int experience) {
        if (experience >= 0) {
            this.experience = experience;
            return true;
        }
        return false;
    }

    public void transferExperience(Enemy enemy) {
        this.gainExperience(enemy.getExperience());
        enemy.setExperience(0);
    }

    public ArrayList<Transportable> getInventory() {
        return inventory;
    }

    public boolean hasItem(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            return false;
        }
        for (Transportable item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public Integer findFirstHealIndex() {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) instanceof Heal) {
                return i;
            }
        }
        return null;
    }

    public int inventorySize() {
        return inventory.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getStrength() {
        return strength;
    }

    public int getMaxStrength() {
        return maxStrength;
    }

    public int getAttack() {
        return attack;
    }

    public int getMaxAttack() {
        return maxAttack;
    }

    public int getDefense() {
        return defense;
    }

    public int getMaxDefense() {
        return maxDefense;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }
}
