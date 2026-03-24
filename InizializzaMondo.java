
public class InizializzaMondo {
    public static Mondo InizializzaMondo() {

        Personaggio protagonista = new Personaggio("Zanca", "Ragazzo finito nel posto sbagliato... o forse giusto", 100,50, 30, 20);
        Mondo mondo = new Mondo("Via Roma: Guerra di Strada", "Notte violenta nella via", protagonista);
        Luogo stazione = new Luogo("Stazione Ferroviaria","Ingresso di Udine, luci al neon che sbiadiscono. Da qui inizia tutto.");
        Luogo viaRoma = new Luogo("Via Roma", "La strada è viva… ma nel modo sbagliato. Motorini che sfrecciano, urla, "+ "gente che spaccia negli angoli. Qui comandano bande diverse.");
        Luogo kebab = new Luogo("Buonissimo Kebab","Un kebabbaro all'angolo, odore di carne e spezie. Qui ti ferma un vecchio "+ "con una lattina. 'Se le cose vanno male… bevila.' Poi sparisce.");
        Luogo stradaRissa = new Luogo("Strada Principale","Davanti al kebab. Scoppia una rissa enorme. Tre figure emergono: "+ "Il Gigante, Il Rosso, Il Cane. Non puoi scappare... ti colpiscono.");
        Luogo retroTabacchino = new Luogo("Retro del Tabacchino","Ti svegli chiuso qui. Buio, stretto, odore di tabacco stantio. "+ "Fuori dalla porta due tipi strafatti fanno la guardia. Il tuo zaino è lì… a pochi metri.");
        Luogo sotterranei = new Luogo("Rete di Passaggi Sotterranei","Tunnel che collegano kebab, bar e tabacchini. Stanze piene di merce rubata, "+ "gente che dorme per terra. Qui non c'è polizia. Solo traffici.");
        Luogo parcheggioSotterraneo = new Luogo("Parcheggio Sotterraneo","Area aperta sotto terra. Qui regna Il Muro, controlla il passaggio. "+ "Qualcuno lo paga per bloccare la zona.");
        Luogo edificioAbbandonato = new Luogo("Edificio Abbandonato","Palazzo abbandonato sopra Via Roma. Scale rotte, graffiti, ogni piano è controllato "+ "da gente diversa: droga, soldi, telefoni rubati.");
        Luogo tetto = new Luogo("Tetto del Palazzo", "Sopra Via Roma. Il vento forte. Il Rosso è qui, gestisce tutto. "+ "Non combatte diretto: usa gente e trappole. Ti rivela che qualcuno ti ha portato qui apposta.");
        Luogo zonaCaos = new Luogo("Zona Centrale", "Via Roma nel caos totale. Sirene lontane, gente che scappa. "+ "Vedi Il Cane correre verso il fondo della via. Lo insegui.");
        Luogo fondoVia = new Luogo("Fondo della Via","La fine di Via Roma. Tutto sembra tranquillo… troppo tranquillo. "+ "Qui affronti Il Cane. Prima di cadere ti dice: 'State lavorando per loro.'");
        Luogo mixMarket = new Luogo("Mix Market", "Un negozio nuovo. Prodotti russi, gente tranquilla, luci fredde. "+ "Ora hai una scelta: restare e controllare, o andartene prima di diventare come loro.");


        stazione.collega("via", viaRoma);
        viaRoma.collega("stazione", stazione);
        viaRoma.collega("kebab", kebab);
        kebab.collega("strada", viaRoma);
        viaRoma.collega("avanti", stradaRissa);
        kebab.collega("avanti", stradaRissa);
        stradaRissa.collega("risveglio", retroTabacchino);
        retroTabacchino.collega("tunnel", sotterranei);
        retroTabacchino.collega("scappa", sotterranei);
        sotterranei.collega("profondo", parcheggioSotterraneo);
        parcheggioSotterraneo.collega("indietro", sotterranei);
        parcheggioSotterraneo.collega("scale", edificioAbbandonato);
        edificioAbbandonato.collega("giù", parcheggioSotterraneo);
        edificioAbbandonato.collega("su", tetto);
        tetto.collega("giù", edificioAbbandonato);
        tetto.collega("scendi", zonaCaos);
        zonaCaos.collega("tetto", tetto);
        zonaCaos.collega("fondo", fondoVia);
        fondoVia.collega("indietro", zonaCaos);
        fondoVia.collega("market", mixMarket);
        mixMarket.collega("fuori", fondoVia);


        mondo.aggiungiLuogo(viaRoma);
        mondo.aggiungiLuogo(stazione);
        mondo.aggiungiLuogo(kebab);
        mondo.aggiungiLuogo(stradaRissa);
        mondo.aggiungiLuogo(retroTabacchino);
        mondo.aggiungiLuogo(sotterranei);
        mondo.aggiungiLuogo(parcheggioSotterraneo);
        mondo.aggiungiLuogo(edificioAbbandonato);
        mondo.aggiungiLuogo(tetto);
        mondo.aggiungiLuogo(zonaCaos);
        mondo.aggiungiLuogo(fondoVia);
        mondo.aggiungiLuogo(mixMarket);

        
        Personaggio ilMuro = new Personaggio("Il Muro","Il Gigante. Un tipo enorme, imbattibile a mani nude. Combatte senza paura, incassa tutto.",150, 40, 50, 10);
        Personaggio ilRosso = new Personaggio("Il Rosso","Mente della zona. Non combatte diretto, usa gente e trappole. Sa la verità.",100, 35, 30, 40);
        Personaggio ilCane = new Personaggio("Il Cane","Velocissimo, imprevedibile, sempre armato. Corre tra auto e vetrine.",120, 50, 20, 60);


        Item lattina = new Item("Lattina Misteriosa","Apparentemente normale. Il vecchio ha detto: 'Se le cose vanno male... bevila.", 0, 0, true);
        Item zaino = new Item("Zaino", "I tuoi averi, rubati durante la rissa. Contiene sigarette e pochi soldi.", 10,0, true);
        Item chiaveElettronica = new Item("Chiave Elettronica","Trovata sul Muro dopo averlo sconfitto. Apre porte blindate.", 5, 0, true);
        Item mappaVia = new Item("Mappa della Via Intera","Trovata sul Rosso. Mostra tutti i passaggi segreti. Scritta sopra: 'IL CANE NON DEVE ARRIVARE IN FONDO ALLA VIA'",15, 0, true);


        kebab.aggiungiOggetto(lattina);
        retroTabacchino.aggiungiOggetto(zaino);
        parcheggioSotterraneo.aggiungiOggetto(chiaveElettronica);
        tetto.aggiungiOggetto(mappaVia);
        parcheggioSotterraneo.aggiungiPersonaggio(ilMuro);
        tetto.aggiungiPersonaggio(ilRosso);

    
        return mondo;
    } 
}
