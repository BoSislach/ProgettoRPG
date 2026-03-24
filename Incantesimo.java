public class Incantesimo implements PozioneLanciabile{
    private String nome;
    private String descrizione;
    private int potenza;

    public Incantesimo(String nome, int potenza,String descrizione) {
        this.nome = nome;
        this.potenza = potenza;
        this.descrizione = descrizione;
    }

    public void eseguiEffetto(Enemy nemico) {
        int danno = potenza;// Puoi personalizzare il calcolo del danno in base al tipo di incantesimo
        nemico.setVita(nemico.getHealth() - danno);
        System.out.println("Hai usato " + nome + " e inflitto " + danno + " danni a " + nemico.getName() + "!");
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
}
