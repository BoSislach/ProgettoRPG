public class Incantesimo{
    private String nome;
    private String descrizione;
    private int potenza;

    public Incantesimo(String nome, int potenza,String descrizione) {
        this.nome = nome;
        this.potenza = potenza;
        this.descrizione = descrizione;
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
