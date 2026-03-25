public class Incantesimo implements PozioneLanciabile{
    private String nome;
    private String descrizione;
    private int potenza;
    private int manaCosto;

    public Incantesimo(String nome, int potenza,String descrizione,int manaCosto) {
        this.nome = nome;
        this.potenza = potenza;
        this.descrizione = descrizione;
        this.manaCosto = manaCosto;
    }

    public void eseguiEffetto(Enemy nemico) {
        int danno = potenza;// puoi personalizzare il calcolo del danno in base al tipo di incantesimo
        nemico.setVita(nemico.getHealth() - danno);
        System.out.println("Hai usato " + nome + " e inflitto " + danno + " danni a " + nemico.getName() + "!");
    }

    public int getManaCosto() {
        return manaCosto;
    }

    public String getNome() {
        return nome;
    }

    public int getPotenza() {
        return potenza;
    }
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public void eseguiEffetto(Personaggio nemico) {
        throw new UnsupportedOperationException("non supportato");
    }
}
