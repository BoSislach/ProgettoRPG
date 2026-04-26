public class LockableContainer extends GenericObject {
    private boolean locked;
    private Item containedItem;

    public LockableContainer(String name, String description, int weight, int durability, boolean requiresKey, boolean destructible) {
        super(name, description, weight, durability, requiresKey, destructible);
        this.locked = true; 
    }

    public boolean isRequiresKey() {
        return super.requiresKey();
    }

    public boolean addItem(Item item) {
        if (containedItem == null) {
            containedItem = item;
            return true;
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

    public boolean isLocked() {
        return locked;
    }

    public Item getContainedItem() {
        return containedItem;
    }

    public boolean openWithKey() {
        if (locked) {
            locked = false;
            return true;
        }
        return false;
    }

    public boolean open() {
        if (locked) {
            return super.open();
        } else {
            return false;
        }
    }

    public boolean close() {
        if (!locked) {
            locked = true;
            return true;
        } else {
            return false;
        }
    }
}
