import java.util.List;

public interface OpenableClosable {
    boolean open();
    boolean close();
    boolean isOpen();
    boolean requiresKey();
    List<Item> getContents();
    void addObject(Item object);
    void removeObject(Item object);
}
