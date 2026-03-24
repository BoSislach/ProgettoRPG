public class OggettoDistruttibili extends OggettoGenerico{
    private boolean distruttibile;
    public OggettoDistruttibili(String nome, String descrizione, int peso, int usura, boolean richiedeChiave) {
        super(nome, descrizione, peso, usura, richiedeChiave, true);
        this.distruttibile = true;
    }

    @Override
    public void distruggi() {
        super.distruggi();
    }



    @Override
    public boolean isDistruttibile() {
        return super.isDistruttibile();
    }
}
