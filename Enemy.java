import java.util.Random;

class Enemy {
    private String name;
    private int health;
    private int maxHealth;
    private int attackPower;
    private boolean isAlive;
    private Shield shield;
    private Random random;
    private int experience;

    public Enemy(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attackPower = attackPower;
        this.isAlive = true;

        random = new Random();
        experience = random.nextInt(15) + 5;

        shield = new Shield("Shield of " + name, "A shield used by " + name,5, 100, random.nextInt(10) + 5, true);
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (this.health <= 0) {
            this.health = 0;
            this.isAlive = false;
        }
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
    }

    public Random getRandom() {
        return random;
    }

    public Shield getShield() {
        return shield;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        if (health > 0) {
            return true;
        } else {
            setIsAlive(false);
            return false;
        }
    }

    public int attack() {
        int variation = random.nextInt(attackPower / 2) - (attackPower / 4);
        return attackPower + variation;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
            isAlive = false;
        }
        System.out.println(name + " takes " + damage + " damage! (Health remaining: " + health + ")");
    }

    public boolean isDefeated() {
        return health <= 0;
    }

    public int getExperience() {
        return experience;
    }

    public boolean setExperience(int experience) {
        if (experience >= 0) {
            this.experience = experience;
            return true;
        }
        return false;
    }

    public void die(Location currentLocation) {
        System.out.println(name + " has been defeated!");

        if (shield != null && shield.getShieldState()) {
            currentLocation.addItem(shield);
            System.out.println(name + " dropped: " + shield.getName());
            shield = null;
        }

        isAlive = false;
    }

    public void transferExperience(GameCharacter winner) {
        winner.gainExperience(this.experience);
        System.out.println(winner.getName() + " gains " + this.experience + " experience points!");
        this.experience = 0;
    }
}