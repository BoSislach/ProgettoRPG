public interface Trasportabile {
    static final int MAXUsura = 100;

    public int getUsura();

    public void setUsura(int usura);

    public void aumentaUsura(int usura);

    public void diminuisciUsura(int usura);

    public String getNome();

    public void setNome(String nome);

    public String getDescrizione();

    public void setDescrizione(String descrizione);

    public int getPeso();

    public void setPeso(int peso);

    public boolean isTrasportabile();
}