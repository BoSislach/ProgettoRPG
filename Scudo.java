public class Scudo extends Item{
    private int difesa;
    private boolean statoScudo;

    public Scudo(String nome, String descrizione, int peso, int usura, int difesa, boolean trasportabile) {
        super(nome, descrizione, peso, usura, trasportabile);
        this.difesa = difesa;
        this.statoScudo = true;
    }

    public boolean getStatoScudo() {
        return statoScudo;
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

    public void setStatoScudo(boolean statoScudo) {
        this.statoScudo = statoScudo;
    }

    public boolean diminuisciUsura() {
        if (super.getUsura() > 0) {
            super.setUsura(super.getUsura()-10);
            return true;
        } else if (super.getUsura() == 0) {
            setStatoScudo(false);
            return false;
        }
        return false;
    }

    public int getDifesa() {
        return difesa;
    }

   
}
