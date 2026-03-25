public class OggettoChiudibile extends OggettoGenerico{
    private boolean chiuso;
    private Item itemContenuto; // L'oggetto che può essere contenuto all'interno dell'oggetto chiudibile

    public OggettoChiudibile(String nome, String descrizione, int peso, int usura, boolean richiedeChiave, boolean distruttibile) {
        super(nome, descrizione,peso,usura, richiedeChiave,distruttibile, 0);
        this.chiuso = true; // Di default, l'oggetto è chiuso
    }

    public boolean isRichiedeChiave() {
        return super.richiedeChiave();
    }

    public boolean aggiungiItem(Item item) {
        if (itemContenuto == null) {
            itemContenuto = item;
            return true; // L'item è stato aggiunto con successo
        } else {
            return false; // L'oggetto chiudibile è già occupato da un altro item
        }
    }

    public boolean togliItem() {
        if (itemContenuto != null) {
            itemContenuto = null;
            return true; // L'item è stato rimosso con successo
        } else {
            return false; // Non c'è nessun item da rimuovere
        }
    }

    public boolean isChiuso() {
        return chiuso;
    }

    public boolean apri() {
        if (chiuso) {
            return super.apri();
            
        }else {
            return false;
            
        }
        
    }
        public boolean chiudi() {
            if (!chiuso) {
                chiuso = true;
                return true;
            } else {
                return false; // L'oggetto è già chiuso
            }
        }
    
}
