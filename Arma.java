public class Arma extends Item {
    private int danno;
    private int gittata;
    private boolean statoArma;

    public Arma(String nome, String descrizione, int peso, int usura, int danno, int gittata, boolean trasportabile) {
        super(nome, descrizione, peso, usura, trasportabile);
        this.danno = danno;
        this.gittata = gittata;
        this.statoArma = true;
    }

    public int getGittata() {
        return gittata;
    }

    @Override
    public int getUsura() {
        return super.getUsura();
    }

    @Override
    public void setUsura(int usura) {
        super.setUsura(usura);
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

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione();
    }

    @Override
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
    }

    @Override
    public int getPeso() {
        return super.getPeso();
    }

    @Override
    public void setPeso(int peso) {
        super.setPeso(peso);
    }

    public boolean getStatoArma() {
        return statoArma;
    }

    public void setStatoArma(boolean statoArma) {
        this.statoArma = statoArma;
    }

    public boolean diminuisciUsura() {
        if (usura > 0) {
            usura-=10;
            return true;
        } else if (usura == 0) {
            setStatoArma(false);
            return false;
        }
        return false;
    }

    public int getDanno() {
        return danno;
    }


}
