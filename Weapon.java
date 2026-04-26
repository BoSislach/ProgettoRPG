public class Weapon extends Item {
    private int damage;
    private int range;
    private boolean weaponState;
    private int minStrength;

    public Weapon(String name, String description, int weight, int durability, int damage, int range, boolean transportable, int minStrength) {
        super(name, description, weight, durability, transportable);
        this.damage = damage;
        this.range = range;
        this.weaponState = true;
        this.minStrength = minStrength;
    }

    public int getRange() {
        return range;
    }

    public int getMinStrength() {
        return minStrength;
    }

    @Override
    public int getDurability() {
        return super.getDurability();
    }

    @Override
    public void setDurability(int durability) {
        super.setDurability(durability);
    }

    @Override
    public void increaseDurability(int durability) {
        super.increaseDurability(durability);
    }

    @Override
    public void decreaseDurability(int durability) {
        super.decreaseDurability(durability);
    }

    @Override
    public boolean isTransportable() {
        return super.isTransportable();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    public int getWeight() {
        return super.getWeight();
    }

    @Override
    public void setWeight(int weight) {
        super.setWeight(weight);
    }

    public boolean getWeaponState() {
        return weaponState;
    }

    public void setWeaponState(boolean weaponState) {
        this.weaponState = weaponState;
    }

    public boolean reduceDurability() {
        if (durability > 0) {
            durability -= 10;
            if (durability <= 0) {
                durability = 0;
                weaponState = false;
            }
            return true;
        }
        weaponState = false;
        return false;
    }

    public int getDamage() {
        return damage;
    }
}
