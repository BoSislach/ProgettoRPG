public class PoisonPotion extends Potion {

    public PoisonPotion(String name, String description, int weight, int uses, boolean transportable, int damage, String type) {
        super(name, description, weight, uses, transportable, type, damage);
    }

    public int getDamage() {
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

    public boolean poison(GameCharacter character) {
        if (!character.isAlive()) {
            return false;
        }
        int newMax = character.getMaxHealth() - super.getValue();
        if (newMax < 1) {
            newMax = 1;
        }
        character.setMaxHealth(newMax);
        character.setHealth(character.getHealth() - super.getValue());
        return true;
    }
}
