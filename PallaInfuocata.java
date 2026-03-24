class PallaInfuocata extends Incantesimo implements PozioneLanciabile{
    
    public PallaInfuocata(int danno,String nome,String descrizione) {
        super(nome,danno,descrizione);
 
    }

    @Override
    public void lanciaPozione(Enemy bersaglio) {
        bersaglio.takeDamage(super.getPotenza());
    }
} 