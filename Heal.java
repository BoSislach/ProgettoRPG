public class Heal extends Potion {

    public Heal(String name, String description, int weight, int uses, boolean transportable, int healValue, String type) {
        super(name, description, weight, uses, transportable, type, healValue);
    }

    public int getHealValue() {
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

    public boolean healCharacter(GameCharacter character) {
        if (character.isAlive()) {
            int heal = getHealValue() * 3; 
            character.setHealth(character.getHealth() + heal);
            return true;
        } else {
            return false;
        }
    }
}
