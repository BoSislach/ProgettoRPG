import java.util.ArrayList;
import java.util.List;

public class OggettoGenerico implements ApribileChiudibile, Distruttibile {
    private boolean distruttibile;
    private boolean aperto;
    private boolean richiedeChiave;
    private List<Item> contenuto;
    private String nome;
    private ArrayList<Item> listaOggettiDepositati;
    final static int MaxOggettiDepositabili = 5;

    public OggettoGenerico(String nome, String descrizione, int peso, int usura, boolean richiedeChiave,boolean distruttibile) {
        this.nome = nome;
        this.aperto = false;
        this.richiedeChiave = richiedeChiave;
        this.contenuto = new ArrayList<>();
        this.distruttibile = distruttibile;
    }

    @Override
    public boolean apri() {
        if (!richiedeChiave) {
            aperto = true;
            return true;
        }
        return false; // richiede chiave
    }

    @Override
    public boolean isDistruttibile() {
        return distruttibile;
    }

    @Override
    public boolean chiudi() {
        aperto = false;
        return true;
    }

    @Override
    public boolean isAperto() {
        return aperto;
    }

    @Override
    public boolean richiedeChiave() {
        return richiedeChiave;
    }

    public ArrayList<Item> getListaOggettiDepositatiTrasportabili() {
        ArrayList<Item> listaOggettiDepositatiTrasportabili = new ArrayList<>();
        for(int i = 0; i < listaOggettiDepositati.size(); i++) {
            if(listaOggettiDepositati.get(i).isTrasportabile()) {
                listaOggettiDepositatiTrasportabili.add(listaOggettiDepositati.get(i));
            }
        }
        return listaOggettiDepositatiTrasportabili;
    }

    @Override
    public List<Item> getContenuto() {
        return aperto ? contenuto : null;
    }

    public boolean depositaOggetto(Item oggetto) {
        if (aperto && listaOggettiDepositati.size() < MaxOggettiDepositabili) {
            listaOggettiDepositati.add(oggetto);
            return true;
        }
        return false;
    }

    @Override
    public void aggiungiOggetto(Item oggetto) {
        if (aperto) {
            contenuto.add(oggetto);
        }
    }

    @Override
    public void rimuoviOggetto(Item oggetto) {
        if (aperto) {
            contenuto.remove(oggetto);
        }
    }

    @Override

    public void distruggi() {
        // Distrugge e svuota contenuto
        contenuto.clear();
    }
}
