import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Luogo {
    private String nome;
    private String descrizione;
    private ArrayList<Personaggio> personaggi;
    private ArrayList<Item> item;
    private ArrayList<OggettoGenerico> oggettiGenerici;
    private Map<String, Luogo> connessioni; // direzione -> luogo

    public Luogo(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.personaggi = new ArrayList<>();
        this.item = new ArrayList<>();
        this.oggettiGenerici = new ArrayList<>();
        this.connessioni = new HashMap<>();
    }

    public String getNome() {
        return nome;
    }

    public List<String> getOggettiPresenti() {
        List<String> presenti = new ArrayList<>();
        for (Item i : item) {
            presenti.add(i.getNome());
        }
        for (OggettoGenerico og : oggettiGenerici) {
            presenti.add(og.getNome());
        }
        return presenti;
    }

    public void aggiungiOggettoGenerico(OggettoGenerico oggetto) {
        oggettiGenerici.add(oggetto);
    }

    public String getOggettoGenericoPresente() {
        String s ="";
        for(int c=0;c<oggettiGenerici.size();c++){
            s+=oggettiGenerici.get(c).getNome()+"\n";
        }
        return s;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public List<Personaggio> getPersonaggi() {
        return personaggi;
    }

    public ArrayList<OggettoGenerico> getOggettiGenerici() {
        return oggettiGenerici;
    }

    public List<Item> getOggetti() {
        return item;
    }

    public void aggiungiPersonaggio(Personaggio p) {
        personaggi.add(p);
    }

    public void rimuoviPersonaggio(Personaggio p) {
        personaggi.remove(p);
    }

    public void aggiungiItem(Item o) {
        item.add(o);
    }

    public void rimuoviItem(Item o) {
        item.remove(o);
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