public class Item implements Transportable {
    protected String name;
    protected String description;
    protected int weight;
    protected int durability;
    protected boolean transportable;

    public Item(String name, String description, int weight, int durability, boolean transportable) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.durability = durability;
        this.transportable = transportable;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public void increaseDurability(int durability) {
        this.durability += durability;
    }

    public void decreaseDurability(int durability) {
        this.durability -= durability;
        if (this.durability < 0) this.durability = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isTransportable() {
        return transportable;
    }

    public void setTransportable(boolean transportable) {
        this.transportable = transportable;
    }

    public String getDescription() {
        return description;
    }
}