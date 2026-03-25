import java.util.ArrayList;
import java.util.Random;

public class Personaggio {
    private String nome;
    private String descrizione;
    private int vita;
    private int maxVita;
    private int forza;
    private int maxForza;
    private int attacco;
    private int maxAttacco;
    private int difesa;
    private int maxDifesa;
    private int esperienza;
    private ArrayList<Trasportabile> sacca;
    private int pesoSacca;
    private final int maxPesoSacca = 100;
    private boolean isAlive;
    private Scudo scudoPosseduto;
    private boolean modalitaDifesa;
    private boolean pozioneAttiva;
    private int attaccoPrePozione;
    private int difesaPrePozione;
    private int forzaPrePozione;
    private int turniPozione;
    private ArrayList<Incantesimo> incantesimiSbloccati = new ArrayList<>();
    private boolean haSbloccatoIncantesimi = false;
    private int mana;

    public Personaggio(String nome, String descrizione, int maxVita, int maxForza, int maxAttacco, int maxDifesa) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.maxVita = maxVita;
        this.vita = maxVita;
        this.maxForza = maxForza;
        this.forza = maxForza;
        this.maxAttacco = maxAttacco;
        this.attacco = maxAttacco;
        this.maxDifesa = maxDifesa;
        this.difesa = maxDifesa;
        this.esperienza = 0;
        this.sacca = new ArrayList<>();
        this.pesoSacca = 0;
        this.isAlive = true;
        this.modalitaDifesa = false;
        this.pozioneAttiva = false;
        this.turniPozione = 0;
        this.mana =0;

        this.incantesimiSbloccati.add(new Incantesimo("palla infuocata", 20, "Una palla di fuoco che brucia i peli del sedere del nemico", 10));
        this.incantesimiSbloccati.add(new Incantesimo("fulmine celeste", 25, "Un fulmine dal cielo che colpisce con la furia degli dei", 15));
        this.incantesimiSbloccati.add(new Incantesimo("onda gelida", 18, "Un'onda di ghiaccio che congela il sangue nelle vene del nemico", 12));
        this.incantesimiSbloccati.add(new Incantesimo("spirito dell'ombra", 30, "Uno spirito oscuro che divora l'anima del nemico", 20));
        this.incantesimiSbloccati.add(new Incantesimo("raggio purificatore", 22, "Un raggio di luce che purifica e distrugge il male", 18));
        this.incantesimiSbloccati.add(new Incantesimo("vortice di vento", 15, "Un vortice di vento che strappa la carne dal nemico", 14));
    }

    public boolean setVita(int vita) {
        if (vita > 0 && vita <= maxVita) {
            this.vita = vita;
            return true;
        } else {
            return false;
        }
    }

    public String mostraIncantesimiSbloccati() {
        String s = "";
        for (Incantesimo incantesimo : incantesimiSbloccati) {
            s += incantesimo.getNome() + "\n";
        }
        return s;
    }

    public void raccogliOggetto(Luogo luogoCorrente, String nomeOggetto) {
        if (luogoCorrente.getOggettiPresenti().contains(nomeOggetto)) {
            for (Item oggetto : luogoCorrente.getOggetti()) {
                if (oggetto.getNome().equals(nomeOggetto) && oggetto.isTrasportabile()) {
                    if (aggiungiOggetto(oggetto)) {
                        luogoCorrente.rimuoviItem(oggetto);
                        System.out.println(nome + " ha raccolto " + oggetto.getNome());
                    } else {
                        System.out.println(nome + " non può raccogliere " + oggetto.getNome() + " perché la sacca è piena.");
                    }
                    return;
                }
            }
        } else {
            System.out.println(nomeOggetto + " non è presente in " + luogoCorrente.getNome());
        }
    }
    public void controllaOggettoGenerico(Luogo luogoCorrente) {
        System.out.println("Oggetti generici nel luogo:");
        for (OggettoGenerico og : luogoCorrente.getOggettiGenerici()) {
            System.out.println(og.getNome());
        }
    }

    public void setManaPersonaggio(int mana) {
        this.mana = mana;
    }

    public int getManaPersonaggio() {
        return mana;
    }

    public void raccogliMana(Luogo luogoCorrente) {
        for (OggettoGenerico oggetto : luogoCorrente.getOggettiGenerici()) {
            if (oggetto.getManaPresente() > 0) {
                mana += oggetto.getManaPresente();
                System.out.println(nome + " ha raccolto " + oggetto.getManaPresente() + " mana da " + oggetto.getNome());
                oggetto.setManaPresente(0); // Rimuove il mana dall'oggetto dopo averlo raccolto
            }
        }
    }

    public void subisciDanno(int danno) {
        int dannoSubito = danno - difesa;

        if (scudoPosseduto != null && scudoPosseduto.getStatoScudo()) {
            Random random = new Random();
            int percentuale;
            if (pozioneAttiva) {
                percentuale = 20 + random.nextInt(31);
            } else if (modalitaDifesa) {
                percentuale = 50 + random.nextInt(51);
            } else {
                percentuale = 20 + random.nextInt(31);
            }

            int riduzioneScudo = (scudoPosseduto.getDifesa() * percentuale) / 100;
            dannoSubito -= riduzioneScudo;
            scudoPosseduto.diminuisciUsura();
        }

        if (dannoSubito > 0) {
            vita -= dannoSubito;
            if (vita <= 0) {
                vita = 0;
                isAlive = false;
            }
        }
    }

    public void ottieniEsperienza(int esperienza) {
        this.esperienza += esperienza;
        aumentaLivello(this.esperienza);
    }

    public String mostraArmi() {
        String s = "";
        for (Trasportabile oggetto : sacca) {
            if (oggetto instanceof Arma) {
                s += oggetto.getNome() + "\n";
            }
        }
        return s;
    }

    public Arma scegliArma(int posizioneArmaNellaSacca) {
        return (Arma) sacca.get(posizioneArmaNellaSacca);
    }

    public void mostraSacca() {
        for (Trasportabile oggetto : sacca) {
            System.out.println(oggetto.getNome());
        }
    }

    public int attacca(Personaggio nemico, Arma arma) {
        Random random = new Random();
        int percentuale;

        if (pozioneAttiva || modalitaDifesa) {
            percentuale = 20 + random.nextInt(31);
        } else {
            percentuale = 50 + random.nextInt(51);
        }

        int dannoInflitto = (arma.getDanno() * percentuale) / 100;
        nemico.subisciDanno(dannoInflitto);
        arma.diminuisciUsura();

        return dannoInflitto;
    }

    public Incantesimo getIncantesimoByIndex(int index) {
        if (index >= 0 && index < incantesimiSbloccati.size()) {
            return incantesimiSbloccati.get(index);
        } else {
            return null;
        }
    }

    public void muori(Luogo luogoCorrente) {
        // Il personaggio muore e lascia cadere tutti i suoi oggetti nel luogo
        for (Trasportabile oggetto : sacca) {
            luogoCorrente.aggiungiItem((Item) oggetto);
        }
        sacca.clear();

        // se aveva uno scudo cade
        if (scudoPosseduto != null) {
            luogoCorrente.aggiungiItem(scudoPosseduto);
            scudoPosseduto = null;
        }

        isAlive = false;
    }

    public void trasferisciEsperienza(Personaggio vincitore) {
        vincitore.ottieniEsperienza(this.esperienza);
    }

    public void setModalitaDifesa(boolean modalitaDifesa) {
        this.modalitaDifesa = modalitaDifesa;
    }

    public boolean getModalitaDifesa() {
        return modalitaDifesa;
    }

    public void setPozioneAttiva(boolean pozioneAttiva) {
        this.pozioneAttiva = pozioneAttiva;
    }

    public boolean getPozioneAttiva() {
        return pozioneAttiva;
    }

    public String mostraPozioni() {
        String s = "";
        for (Trasportabile oggetto : sacca) {
            if (oggetto instanceof Pozione) {
                s += oggetto.getNome() + "\n";
            }
        }
        return s;
    }

    public boolean usaPozione(int indicePozioneNellaSacca) {
        if (pozioneAttiva) {
            return false;
        } else {
            if (indicePozioneNellaSacca >= 0 && indicePozioneNellaSacca < sacca.size()) {
                Trasportabile oggetto = sacca.get(indicePozioneNellaSacca);
                if (oggetto instanceof Pozione) {
                    Pozione pozione = (Pozione) oggetto;
                    if (pozione.getStatoPozione()) {
                        attaccoPrePozione = attacco;
                        difesaPrePozione = difesa;
                        forzaPrePozione = forza;
                        attacco += pozione.getValore();
                        difesa += pozione.getValore();
                        forza += pozione.getValore();
                        turniPozione = pozione.getUsura();
                        pozioneAttiva = true;
                        sacca.remove(indicePozioneNellaSacca);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public int getTurniPozione() {
        return turniPozione;
    }

    public void decrementaTurniPozione() {
        if (pozioneAttiva) {
            turniPozione--;
            if (turniPozione <= 0) {
                attacco = attaccoPrePozione;
                difesa = difesaPrePozione;
                forza = forzaPrePozione;
                pozioneAttiva = false;
            }
        }
    }

    public boolean aggiungiOggetto(Trasportabile oggetto) {
        if (sacca.size() < maxPesoSacca) {
            sacca.add(oggetto);
            return true;
        } else {
            return false;
        }
    }

    public Scudo getScudoPosseduto() {
        return scudoPosseduto;
    }

    public boolean aggiungiScudo(Scudo scudo) {
        if (scudoPosseduto == null) {
            scudoPosseduto = scudo;
            return true;
        } else {
            return false;
        }
    }

    public boolean rimuoviOggetto(Trasportabile oggetto) {
        if (sacca.contains(oggetto)) {
            sacca.remove(oggetto);
            return true;
        } else {
            return false;
        }
    }

    public void setHaSbloccatoIncantesimi(boolean haSbloccatoIncantesimi) {
        this.haSbloccatoIncantesimi = haSbloccatoIncantesimi;
    }

    public boolean getHaSbloccatoIncantesimi() {
        return haSbloccatoIncantesimi;
    }

    public ArrayList<Incantesimo> getIncantesimiSbloccati() {
        return incantesimiSbloccati;
    }

    public int attack() {
        Random random = new Random();
        return attacco + random.nextInt(10);
    }

    public boolean setEsperienza(int esperienza) {
        if (esperienza >= 0) {
            this.esperienza = esperienza;
            return true;
        } else {
            return false;
        }
    }

    public void usaIncantesimo(int indiceIncantesimo, Personaggio nemico) {
        if (indiceIncantesimo >= 0 && indiceIncantesimo < incantesimiSbloccati.size()) {
            Incantesimo incantesimo = incantesimiSbloccati.get(indiceIncantesimo);
            incantesimo.eseguiEffetto(nemico);
        } else {
            System.out.println("Indice dell'incantesimo non valido.");
        }
    }

    private String aumentaLivello(int esperienza) {
        if (esperienza >= 25) {
            maxVita *= 1.2;
            maxForza *= 1.2;
            maxAttacco *= 1.2;
            maxDifesa *= 1.2;
            return "hai raggiunto il livello 2!";
        } else if (esperienza >= 50) {
            maxVita *= 1.35;
            maxForza *= 1.35;
            maxAttacco *= 1.35;
            maxDifesa *= 1.35;
            return "hai raggiunto il livello 3!";
        } else if (esperienza >= 75) {
            maxVita *= 1.5;
            maxForza *= 1.5;
            maxAttacco *= 1.5;
            maxDifesa *= 1.5;
            return "hai raggiunto il livello 4!";
        } else if (esperienza >= 100) {
            maxVita *= 1.75;
            maxForza *= 1.75;
            maxAttacco *= 1.75;
            maxDifesa *= 1.75;
            return "hai raggiunto il livello 5!";
        } else if (esperienza >= 125) {
            maxVita *= 2;
            maxForza *= 2;
            maxAttacco *= 2;
            maxDifesa *= 2;
            return "hai raggiunto il livello 6!";
        } else if (esperienza >= 150) {
            haSbloccatoIncantesimi = true;
            return "hai sbloccato gli incantesimi, sbloccando il livello massimo!";
        }

        return "";
    }

    public ArrayList<Trasportabile> getSacca() {
        return sacca;
    }

    public int lungSacca() {
        return sacca.size();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getVita() {
        return vita;
    }

    public int getMaxVita() {
        return maxVita;
    }

    public int getForza() {
        return forza;
    }

    public int getAttacco() {
        return attacco;
    }

    public int getDifesa() {
        return difesa;
    }

    public boolean scappa() {
        Random random = new Random();
        return random.nextBoolean(); 
    }

    public int getEsperienza() {
        return esperienza;
    }
}






