public class Item implements Trasportabile {
    protected String nome;
    protected String descrizione;
    protected int peso;
    protected int usura;
    protected boolean trasportabile;

    public Item(String nome, String descrizione, int peso, int usura, boolean trasportabile) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.peso = peso;
        this.usura = usura;
        this.trasportabile = trasportabile;
    }

    public int getUsura() {
        return usura;
    }

    public void setUsura(int usura) {
        this.usura = usura;
    }

    public void aumentaUsura(int usura) {
        this.usura += usura;
    }

    public void diminuisciUsura(int usura) {
        this.usura -= usura;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public boolean isTrasportabile() {
        return trasportabile;
    }

    public void setTrasportabile(boolean trasportabile) {
        this.trasportabile = trasportabile;
    }

    public String getDescrizione() {
        return descrizione;
    }

}
