public interface Transportable {
    static final int MAX_DURABILITY = 100;

    public int getDurability();
    public void setDurability(int durability);
    public void increaseDurability(int durability);
    public void decreaseDurability(int durability);
    public String getName();
    public void setName(String name);
    public String getDescription();
    public void setDescription(String description);
    public int getWeight();
    public void setWeight(int weight);
    public boolean isTransportable();
}
