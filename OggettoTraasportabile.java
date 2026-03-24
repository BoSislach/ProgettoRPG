public class OggettoTraasportabile extends Item{

    public OggettoTraasportabile(String nome, String descrizione, int peso, int usura) {
        super(nome, descrizione, peso, usura, true);
    }

    public String getNome() {
        return super.getNome();
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getPeso() {
        return peso;
    }

    public int getUsura() {
        return usura;
    }

    @Override
    public void aumentaUsura(int usura) {
        super.aumentaUsura(usura);
    }

    @Override
    public void diminuisciUsura(int usura) {
        super.diminuisciUsura(usura);
    }

    @Override
    public boolean isTrasportabile() {
        return super.isTrasportabile();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public void setUsura(int usura) {
        this.usura = usura;
    }

    @Override
    public String toString() {
        return "OggettoTraasportabile [nome=" + nome + ", descrizione=" + descrizione + ", peso=" + peso + ", usura="
                + usura + "]";
    }

   
}
