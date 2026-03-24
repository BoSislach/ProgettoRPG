public class Pozione implements Trasportabile{
    private String nome;
    private String descrizione;
    private boolean statoPozione;
    private int peso;
    private int utilizzo;
    private boolean trasportabile;
    private String tipo;
    private int valore;

    public Pozione(String nome, String descrizione, int peso, int utilizzo, boolean trasportabile, String tipo, int valore) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.peso = peso;
        this.utilizzo = utilizzo;
        this.trasportabile = trasportabile;
        this.tipo = tipo;
        this.valore = valore;
        this.statoPozione = true;
    }

    public String getNome() {
        return nome;
    }

    public int getValore(){
        return valore;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public boolean getStatoPozione() {
        return statoPozione;
    }

    public void setStatoPozione(boolean statoPozione) {
        this.statoPozione = statoPozione;
    }

    @Override
    public int getUsura() {
        return this.utilizzo;
    }

    @Override
    public void setUsura(int usura) {
        this.utilizzo = usura;
    }

    @Override
    public void aumentaUsura(int usura) {
        this.utilizzo += usura;
    }

    @Override
    public void diminuisciUsura(int usura) {
        this.utilizzo -= usura;
    }

    @Override
    public boolean isTrasportabile() {
        return this.trasportabile;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public int getPeso() {
        return this.peso;
    }

    @Override
    public void setPeso(int peso) {
        this.peso = peso;
    }

  
   
    
}
