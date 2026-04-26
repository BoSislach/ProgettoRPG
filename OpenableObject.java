public class OpenableObject extends GenericObject {
    private boolean opened;
    private Item containedItem;

    public OpenableObject(String name, String description, int weight, int durability, boolean requiresKey, boolean destructible) {
        super(name, description, weight, durability, requiresKey, destructible);
        this.opened = false;
    }

    public boolean open() {
        if (!opened) {
            return super.open();
        } else {
            return false;
        }
    }

    public boolean removeItem() {
        if (containedItem != null) {
            containedItem = null;
            return true;
        } else {
            return false;
        }
    }

    public boolean addItem(Item item) {
        if (containedItem == null) {
            containedItem = item;
            return true;
        } else {
            return false;
        }
    }

    public boolean close() {
        if (opened) {
            opened = false;
            return false;
        } else {
            return true;
        }
    }

    public boolean isOpen() {
        return opened;
    }
}
