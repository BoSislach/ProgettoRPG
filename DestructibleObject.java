public class DestructibleObject extends GenericObject {
    private boolean destructible;

    public DestructibleObject(String name, String description, int weight, int durability, boolean requiresKey) {
        super(name, description, weight, durability, requiresKey, true);
        this.destructible = true;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public boolean isDestructible() {
        return super.isDestructible();
    }
}
