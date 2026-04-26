public class Shield extends Item {
    private int defense;
    private boolean shieldState;

    public Shield(String name, String description, int weight, int durability, int defense, boolean transportable) {
        super(name, description, weight, durability, transportable);
        this.defense = defense;
        this.shieldState = true;
    }

    public boolean getShieldState() {
        return shieldState;
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

    public void setShieldState(boolean shieldState) {
        this.shieldState = shieldState;
    }

    public boolean reduceDurability() {
        if (super.getDurability() > 0) {
            super.setDurability(super.getDurability() - 10);
            if (super.getDurability() <= 0) {
                super.setDurability(0);
                setShieldState(false);
            }
            return true;
        }
        setShieldState(false);
        return false;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
}
