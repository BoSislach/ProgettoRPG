import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Mondo mondo = InizializzaMondo.InizializzaMondo();
        Personaggio protagonista = mondo.getProtagonista();
        System.out.println("via Roma - Guerra di Strada");
        System.out.println("personaggio principale: " + protagonista.getNome());
        System.out.println("\nPOSIZIONE INIZIALE: " + mondo.getPosizioneCorrente().getNome());
        System.out.println(mondo.getPosizioneCorrente().getDescrizione());
        Scanner scanner = new Scanner(System.in);
        boolean gioco = true;

        while (gioco) {
            Luogo luogoAttuale = mondo.getPosizioneCorrente();
            System.out.println("sei in: " + luogoAttuale.getNome());
            System.out.println(luogoAttuale.getDescrizione());

            if (!luogoAttuale.getOggetti().isEmpty()) {
                System.out.print("oggetti qui: ");
                for (Item item : luogoAttuale.getOggetti()) {
                    System.out.print(item.getNome() + " ");
                }
                System.out.println();
            } else {

                System.out.println("non ci sono oggetti qui");
            }

            System.out.println("\nspostamenti disponibili: " + luogoAttuale.getConnessioni().keySet());
            System.out.println("inserisci direzione (o 'esci'): ");
            String comandi = scanner.nextLine();

            if (comandi.equals("esci")) {
                gioco = false;
            } else if (mondo.sposta(comandi)) {
                System.out.println("ti muovi verso: " + comandi);
            } else {
                System.out.println("non puoi andare in quella direzione.");
            }

            // Controlla se ci sono nemici nel luogo
            if (!luogoAttuale.getPersonaggi().isEmpty()) {
                for (Personaggio p : luogoAttuale.getPersonaggi()) {
                    if (p != protagonista) { // Assumendo che i nemici siano Personaggio, ma dovrebbero essere Enemy
                        // Per ora, assumiamo che i nemici siano Personaggio con nome diverso
                        if (!p.getNome().equals(protagonista.getNome())) {
                            System.out.println("Incontri " + p.getNome() + "!");
                            // Inizia combattimento
                            Combattimento combattimento = new Combattimento(protagonista, p, luogoAttuale);
                            combattimento.iniziaCombattimento();
                            // Dopo combattimento, rimuovi se morto
                            if (!p.isAlive()) {
                                luogoAttuale.rimuoviPersonaggio(p);
                            }
                            break; // Solo uno per volta
                        }
                    }
                }
            }
        }
    }
}