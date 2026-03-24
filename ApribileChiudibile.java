import java.util.List;

public interface ApribileChiudibile {
    boolean apri();

    boolean chiudi();

    boolean isAperto();

    boolean richiedeChiave();

    List<Item> getContenuto();

    void aggiungiOggetto(Item oggetto);

    void rimuoviOggetto(Item oggetto);
}