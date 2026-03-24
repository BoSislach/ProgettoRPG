import java.util.ArrayList;

public class Mondo {
    private String nome;
    private String descrizione;
    private ArrayList<Luogo> luoghi;
    private Luogo posizioneCorrente;
    private Personaggio protagonista;

    public Mondo(String nome, String descrizione, Personaggio protagonista) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.luoghi = new ArrayList<>();
        this.protagonista = protagonista;
    }

    public void aggiungiLuogo(Luogo luogo) {
        luoghi.add(luogo);
        if (luoghi.size() == 1) {
            posizioneCorrente = luogo;
            luogo.aggiungiPersonaggio(protagonista);
        }
    }

    public Luogo getPosizioneCorrente() {
        return posizioneCorrente;
    }

    public boolean sposta(String direzione) {
        Luogo nuovo = posizioneCorrente.getConnessione(direzione);
        if (nuovo != null) {
            posizioneCorrente.rimuoviPersonaggio(protagonista);
            nuovo.aggiungiPersonaggio(protagonista);
            posizioneCorrente = nuovo;
            return true;
        }
        return false;
    }

    public Personaggio getProtagonista() {
        return protagonista;
    }
}
