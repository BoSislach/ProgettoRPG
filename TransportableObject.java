public class TransportableObject extends Item {

    public TransportableObject(String name, String description, int weight, int durability) {
        super(name, description, weight, durability, true);
    }

    public String getName() {
        return super.getName();
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }

    public int getDurability() {
        return durability;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public String toString() {
        return "TransportableObject [name=" + name + ", description=" + description + ", weight=" + weight + ", durability=" + durability + "]";
    }
}
