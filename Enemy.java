import java.util.Random;

class Enemy {
    private String name;
    private int health;
    private int attackPower;
    private boolean isAlive;
    private Scudo scudo;
    private Random random;
    private int esperienza;

    public Enemy(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;

        random = new Random();

        esperienza = random.nextInt(15) + 1; // Esperienza casuale tra 1 e 15

        scudo = new Scudo("scudo nemico", "scudo che protegge il nemico", 1, 0, random.nextInt(20) + 1, true);
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public Random getRandom() {
        return random;
    }

    public Scudo getScudo() {
        return scudo;
    }

    public void setVita(int health) {
        this.health = health;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void getHealth(int health) {
        this.health = health;
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
        return attackPower + random.nextInt(10);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public boolean isDefeated() {
        return health <= 0;
    }

    public int getEsperienza() {
        return esperienza;
    }

    public void muori(Luogo luogoCorrente) {
        if (scudo != null) {
            luogoCorrente.aggiungiItem(scudo);
            scudo = null;
        }
        isAlive = false;
    }

    public boolean setEsperienza(int esperienza) {
        if (esperienza >= 0) {
            this.esperienza = esperienza;
            return true;
        }
        return false;
    }

    public void trasferisciEsperienza(Personaggio vincitore) {
        vincitore.setEsperienza(0);
        setEsperienza(vincitore.getEsperienza() + esperienza);

    }
}