import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Luogo {
    private String nome;
    private String descrizione;
    private ArrayList<Personaggio> personaggi;
    private ArrayList<Item> oggetti;
    private Map<String, Luogo> connessioni; // direzione -> luogo

    public Luogo(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.personaggi = new ArrayList<>();
        this.oggetti = new ArrayList<>();
        this.connessioni = new HashMap<>();
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public List<Personaggio> getPersonaggi() {
        return personaggi;
    }

    public List<Item> getOggetti() {
        return oggetti;
    }

    public void aggiungiPersonaggio(Personaggio p) {
        personaggi.add(p);
    }

    public void rimuoviPersonaggio(Personaggio p) {
        personaggi.remove(p);
    }

    public void aggiungiOggetto(Item o) {
        oggetti.add(o);
    }

    public void rimuoviOggetto(Item o) {
        oggetti.remove(o);
    }

    public void collega(String direzione, Luogo luogo) {
        connessioni.put(direzione, luogo);
    }

    public Luogo getConnessione(String direzione) {
        return connessioni.get(direzione);
    }

    public Map<String, Luogo> getConnessioni() {
        return connessioni;
    }
}