public class Veleno extends Pozione {

    public Veleno(String nome, String descrizione, int peso, int utilizzo, boolean trasportabile, int danno,String tipo) {
        super(nome, descrizione, peso, utilizzo, trasportabile, tipo,danno);
    }

    public int getDanno() {
        return super.getValore();
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
    public void setNome(String nome) {
        super.setNome(nome);
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

    public boolean avvelena(Personaggio personaggio){
        if(personaggio.getVita() > 0){
            personaggio.setVita(personaggio.getVita() - super.getValore());
            return true;
        }else{
            return false;
        }
    }
}
