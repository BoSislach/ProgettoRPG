import java.util.ArrayList;
import java.util.List;

public class GenericObject implements OpenableClosable, Destructible {
    private boolean destructible;
    private boolean opened;
    private boolean requiresKey;
    private List<Item> contents;
    private String name;
    private ArrayList<Item> storedItems;
    final static int MAX_STORABLE_ITEMS = 5;

    public GenericObject(String name, String description, int weight, int durability, boolean requiresKey, boolean destructible) {
        this.name = name;
        this.opened = false;
        this.requiresKey = requiresKey;
        this.contents = new ArrayList<>();
        this.destructible = destructible;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean open() {
        if (!requiresKey) {
            opened = true;
            return true;
        }
        return false; 
    }

    @Override
    public boolean isDestructible() {
        return destructible;
    }

    @Override
    public boolean close() {
        opened = false;
        return true;
    }

    @Override
    public boolean isOpen() {
        return opened;
    }

    @Override
    public boolean requiresKey() {
        return requiresKey;
    }

    public ArrayList<Item> getTransportableStoredItems() {
        ArrayList<Item> transportableItems = new ArrayList<>();
        for (int i = 0; i < storedItems.size(); i++) {
            if (storedItems.get(i).isTransportable()) {
                transportableItems.add(storedItems.get(i));
            }
        }
        return transportableItems;
    }

    @Override
    public List<Item> getContents() {
        return opened ? contents : null;
    }

    public boolean depositItem(Item item) {
        if (opened && storedItems.size() < MAX_STORABLE_ITEMS) {
            storedItems.add(item);
            return true;
        }
        return false;
    }

    @Override
    public void addObject(Item item) {
        if (opened) {
            contents.add(item);
        }
    }

    @Override
    public void removeObject(Item item) {
        if (opened) {
            contents.remove(item);
        }
    }

    @Override
    public void destroy() {
        
        contents.clear();
    }
}
