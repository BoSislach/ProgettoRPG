public class OggettoApribile extends OggettoGenerico {
        private boolean aperto;
        private Item itemContenuto;

        public OggettoApribile(String nome, String descrizione, int peso,int usura,boolean richiedeChiave, boolean distruttibile) {
            super(nome, descrizione,peso,usura,richiedeChiave,distruttibile, 0);
            this.aperto = false; // non e aperto di default
        }

        public boolean apri() {
        if (!aperto) {
            return super.apri();
            
        }else {
            return false;
            
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

    public boolean aggiungiItem(Item item) {
        if (itemContenuto == null) {
            itemContenuto = item;
            return true; // L'item è stato aggiunto con successo
        } else {
            return false; // L'oggetto chiudibile è già occupato da un altro item
        }
    }

        public boolean chiudi() {
            if (aperto) {
                aperto = false;
                return false;
            } else {
                return true; // L'oggetto è già chiuso
            }
        }

        public boolean isAperto() {
            return aperto;
        }
    }

