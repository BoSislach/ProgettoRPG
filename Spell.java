public class Spell {
    private String name;
    private String description;
    private int power;

    public Spell(String name, int power, String description) {
        this.name = name;
        this.power = power;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public String getDescription() {
        return description;
    }
}
