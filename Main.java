import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Mondo mondo = InizializzaMondo.InizializzaMondo();
        Personaggio protagonista = new Personaggio("Zanca", "un negro di merda hahaha", 100, 50, 30, 20);
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
        }
    }
}