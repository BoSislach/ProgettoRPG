import java.util.Random;
import java.util.Scanner;

public class Combattimento {
    private Personaggio personaggio;
    private Personaggio nemico;
    private Luogo luogo;
    private Random random;
    private boolean combattimentoAttivo;

    public Combattimento(Personaggio personaggio, Personaggio nemico, Luogo luogo) {
        this.personaggio = personaggio;
        this.nemico = nemico;
        this.luogo = luogo;
        this.random = new Random();
        this.combattimentoAttivo = true;
    }

    public void iniziaCombattimento() {
        System.out.println("Inizia il combattimento tra " + personaggio.getNome() + " e " + nemico.getNome() + "!");

        // Decide chi attacca per primo (50% probabilità)
        boolean personaggioPrimo = random.nextBoolean();

        while (combattimentoAttivo && personaggio.isAlive() && nemico.isAlive()) {
            if (personaggioPrimo) {
                turnoPersonaggio();
                if (combattimentoAttivo && nemico.isAlive()) {
                    turnoNemico();
                }
            } else {
                turnoNemico();
                if (combattimentoAttivo && personaggio.isAlive()) {
                    turnoPersonaggio();
                }
            }
        }

        fineCombattimento();
    }

    private void turnoPersonaggio() {
        System.out.println("\nTurno di " + personaggio.getNome());
        System.out.println("Cosa vuoi fare? (attacca/difendi/pozione/scappa)");

        String azione = simulaSceltaPersonaggio();

        switch (azione) {
            case "attacca":
                eseguiAttaccoPersonaggio();
                break;
            case "difendi":
                personaggio.setModalitaDifesa(true);
                System.out.println(personaggio.getNome() + " si mette in posizione difensiva!");
                break;
            case "pozione":
                System.out.println(personaggio.mostraPozioni());
                Scanner scanner = new Scanner(System.in);
                System.out.print("Scegli una pozione da usare (inserisci il numero): ");
                int sceltaPozione = scanner.nextInt();
                personaggio.usaPozione(sceltaPozione);
                break;
            case "scappa":
                if (personaggio.scappa()) {
                    System.out.println(personaggio.getNome() + " è riuscito a scappare!");
                    combattimentoAttivo = false;
                } else {
                    System.out.println(personaggio.getNome() + " non è riuscito a scappare!");
                }
                break;
        }

        // Decrementa i turni della pozione se attiva
        personaggio.decrementaTurniPozione();
    }

    private void turnoNemico() {
        System.out.println("\nTurno di " + nemico.getNome());

        // Il nemico sceglie un'azione casuale
        String azione = simulaSceltaNemico();

        switch (azione) {
            case "attacca":
                eseguiAttaccoNemico();
                break;
            case "difendi":
                // Per semplicità, il nemico non si difende
                eseguiAttaccoNemico();
                break;
            case "scappa":
                if (random.nextInt(100) < 20) { // 20% probabilità di scappare
                    System.out.println(nemico.getNome() + " è scappato!");
                    combattimentoAttivo = false;
                } else {
                    eseguiAttaccoNemico();
                }
                break;
        }
    }

    private void eseguiAttaccoPersonaggio() {
        // Mostra armi con indice
        System.out.println("Armi disponibili:");
        int numeroArmi = 0;
        for (int i = 0; i < personaggio.getSacca().size(); i++) {
            Trasportabile item = personaggio.getSacca().get(i);
            if (item instanceof Arma) {
                Arma a = (Arma) item;
                System.out.println(i + ": " + a.getNome() + " (danno=" + a.getDanno() + ")");
                numeroArmi++;
            }
        }

        if (numeroArmi == 0) {
            System.out.println(personaggio.getNome() + " non ha armi!");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        if (personaggio.getHaSbloccatoIncantesimi()) {
            System.out.println("Incantesimi disponibili:");
            System.out.println(personaggio.mostraIncantesimiSbloccati());

            System.out.print("Vuoi usare un incantesimo? (si/no): ");
            String sceltaIncantesimo = scanner.nextLine().trim();

            if (sceltaIncantesimo.equalsIgnoreCase("si")) {
                System.out.println("Mana attuale: " + personaggio.getManaPersonaggio());
                System.out.print("Scegli l'indice dell'incantesimo da usare: ");
                int indiceIncantesimo = scanner.nextInt();
                scanner.nextLine();

                Incantesimo incantesimo = personaggio.getIncantesimoByIndex(indiceIncantesimo);
                if (incantesimo == null) {
                    System.out.println("indice dell'incantesimo non valido, usa l'arma.");
                } else {
                    boolean usaIncantesimo = true;

                    if(personaggio.getManaPersonaggio() < incantesimo.getManaCosto()) {
                        System.out.println("Non hai abbastanza mana per usare questo incantesimo. Usa l'arma.");
                        usaIncantesimo = false;
                    } else {
                        personaggio.setManaPersonaggio(personaggio.getManaPersonaggio() - incantesimo.getManaCosto());
                    }

                    String nome = incantesimo.getNome();
                    if (nome.equalsIgnoreCase("fulmine celeste") || nome.equalsIgnoreCase("onda gelida")) {
                        System.out.println("questi incantesimi ti fanno perdere 10 punti salute ogni volta che li usi, sei sicuro di volerli usare? (si/no)");
                        String conferma = scanner.nextLine().trim();

                        if (conferma.equalsIgnoreCase("si")) {
                            personaggio.setVita(personaggio.getVita() - 10);
                        } else {
                            System.out.println("Incantesimo annullato. Usa l'arma.");
                            usaIncantesimo = false;
                        }
                    }

                    if (usaIncantesimo) {
                        personaggio.usaIncantesimo(indiceIncantesimo, nemico);
                        return; // Salta l'attacco con arma se incantesimo usato
                    }
                }
            }
        } else {
            System.out.println("Non hai ancora sbloccato incantesimi");
        }

        System.out.print("Scegli l'indice dell'arma da usare: ");
        int indiceScelto = scanner.nextInt();

        if (indiceScelto < 0 || indiceScelto >= personaggio.getSacca().size()) {
            System.out.println("Indice non valido. Il turno viene perso.");
            return;
        }

        Trasportabile oggettoSelezionato = personaggio.getSacca().get(indiceScelto);
        if (!(oggettoSelezionato instanceof Arma)) {
            System.out.println("Hai scelto un oggetto non arma. Il turno viene perso.");
            return;
        }

        Arma arma = (Arma) oggettoSelezionato;
        if (!arma.getStatoArma()) {
            System.out.println("L'arma selezionata è inutilizzabile. Il turno viene perso.");
            return;
        }

        int danno = personaggio.attacca(nemico, arma);
        System.out.println(personaggio.getNome() + " attacca con " + arma.getNome() +
                " e infligge " + danno + " danni!");

        if (!nemico.isAlive()) {
            System.out.println(nemico.getNome() + " è stato sconfitto!");
            combattimentoAttivo = false;
        }
    }

    private void eseguiAttaccoNemico() {
        int danno = nemico.attack();
        System.out.println(nemico.getNome() + " attacca e infligge " + danno + " danni!");
        personaggio.subisciDanno(danno);

        if (!personaggio.isAlive()) {
            System.out.println(personaggio.getNome() + " è stato sconfitto!");
            combattimentoAttivo = false;
        }
    }

    private void fineCombattimento() {
        System.out.println("\n--- FINE COMBATTIMENTO ---");

        if (!personaggio.isAlive()) {
            System.out.println(personaggio.getNome() + " è morto!");
            personaggio.muori(luogo);
            nemico.trasferisciEsperienza(personaggio);
            System.out.println(nemico.getNome() + " ottiene " + nemico.getEsperienza() + " esperienza!");
        } else if (!nemico.isAlive()) {
            System.out.println(nemico.getNome() + " è morto!");
            nemico.muori(luogo);
            personaggio.trasferisciEsperienza(nemico);
            System.out.println(personaggio.getNome() + " ottiene " + nemico.getEsperienza() + " esperienza!");
        } else {
            System.out.println("Il combattimento è terminato senza un vincitore chiaro.");
        }
    }

    // Metodi di simulazione per testare (dovrebbero essere sostituiti con input
    // utente)
    private String simulaSceltaPersonaggio() {
        String[] azioni = { "attacca", "difendi", "pozione", "scappa" };
        return azioni[random.nextInt(azioni.length)];
    }

    private String simulaSceltaNemico() {
        String[] azioni = { "attacca", "attacca", "attacca", "scappa" }; // 75% attacca, 25% scappa
        return azioni[random.nextInt(azioni.length)];
    }
}