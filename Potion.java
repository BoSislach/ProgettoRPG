public class Potion extends Item {
    private boolean potionActive;
    private String type;
    private int value;

    public Potion(String name, String description, int weight, int uses, boolean transportable, String type, int value) {
        super(name, description, weight, uses, transportable);
        this.type = type;
        this.value = value;
        this.potionActive = true;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public boolean getPotionActive() {
        return potionActive;
    }

    public void setPotionActive(boolean potionActive) {
        this.potionActive = potionActive;
    }
}
