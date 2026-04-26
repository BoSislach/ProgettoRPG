public class PowerPotion extends Potion {

    public PowerPotion(String name, String description, int weight, int uses, boolean transportable, int restoreValue, String type) {
        super(name, description, weight, uses, transportable, type, restoreValue);
    }

    public int getRestoreValue() {
        return super.getValue();
    }

    @Override
    public int getDurability() {
        return super.getDurability();
    }

    @Override
    public void setDurability(int durability) {
        super.setDurability(durability);
    }

    public void enhanceShield(Shield shield) {
        if (shield != null) {
            shield.setDefense((int) Math.round(shield.getDefense() * 1.3));
            shield.setDurability(100);
            shield.setShieldState(true);
        }
    }

    public void enhanceWeapon(Weapon weapon) {
        if (weapon != null) {
            weapon.setDamage((int) Math.round(weapon.getDamage() * 1.3));
            weapon.setDurability(100);
        }
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
    public void setName(String name) {
        super.setName(name);
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
}
