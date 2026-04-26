public class WorldInitializer {
    public static World initializeWorld() {
        GameCharacter protagonist = new GameCharacter(
                "Zanca",
                "A guy who ended up in the wrong place... or maybe the right one",
                110, 55, 32, 22);

        World world = new World(
                "Roma Street: Street War",
                "Violent night on the street",
                protagonist);

        Location station = new Location(
                "stazione",
                "Entrance to Udine, fading neon lights. It all starts here.");
        Location viaRoma = new Location(
                "Roma Street",
                "The street is alive... but in the wrong way. Scooters, screams and distant sirens.");
        Location buonissimoKebab = new Location(
                "Delicious Kebab",
                "A half-empty kebab shop. An old man passes you a can and disappears.");
        Location streetFight = new Location(
                "Roma Street - Brawl",
                "Broken bottles, blood and confusion. The real war starts here.");
        Location tobaccoBack = new Location(
                "Tobacco Shop Back",
                "Tight room, stale smoke smell, boxes and scattered goods.");
        Location tunnels = new Location(
                "Underground Tunnels",
                "Damp tunnels connecting the street's venues.");
        Location undergroundParking = new Location(
                "Underground Parking",
                "Concrete pillars, dim light. The big shots patrol here.");
        Location abandonedBuilding = new Location(
                "Abandoned Building",
                "Broken stairs and tagged walls. Every floor is a trap.");
        Location roof = new Location(
                "Building Rooftop",
                "Strong wind, view over Roma Street. The Bangla decides here.");
        Location chaosZone = new Location(
                "Central Zone",
                "Crossroads in chaos, broken streetlights and people running.");
        Location streetEnd = new Location(
                "End of the Street",
                "End of the road. Seems calm, but the final reckoning is coming.");
        Location mixMarket = new Location(
                "Mix Market",
                "Cold lights, noisy fridges and eyes on you: the place of the ending.");

        station.connect("street", viaRoma);

        viaRoma.connect("station", station);
        viaRoma.connect("kebab", buonissimoKebab);
        viaRoma.connect("forward", streetFight);
        viaRoma.connect("brawl", streetFight);

        buonissimoKebab.connect("street", viaRoma);
        buonissimoKebab.connect("forward", streetFight);
        buonissimoKebab.connect("brawl", streetFight);

        streetFight.connect("back", viaRoma);
        streetFight.connect("awakening", tobaccoBack);
        streetFight.connect("behind", tobaccoBack);

        tobaccoBack.connect("exit", streetFight);
        tobaccoBack.connect("brawl", streetFight);
        tobaccoBack.connect("tunnel", tunnels);
        tobaccoBack.connect("flee", tunnels);

        tunnels.connect("climb", tobaccoBack);
        tunnels.connect("descend", undergroundParking);
        tunnels.connect("parking", undergroundParking);

        undergroundParking.connect("back", tunnels);
        undergroundParking.connect("stairs", abandonedBuilding);
        undergroundParking.connect("building", abandonedBuilding);

        abandonedBuilding.connect("down", undergroundParking);
        abandonedBuilding.connect("parking", undergroundParking);
        abandonedBuilding.connect("up", roof);
        abandonedBuilding.connect("roof", roof);

        roof.connect("down", abandonedBuilding);
        roof.connect("descend", chaosZone);
        roof.connect("zone", chaosZone);

        chaosZone.connect("roof", roof);
        chaosZone.connect("end", streetEnd);
        chaosZone.connect("exit", streetEnd);

        streetEnd.connect("back", chaosZone);
        streetEnd.connect("zone", chaosZone);
        streetEnd.connect("market", mixMarket);
        streetEnd.connect("shop", mixMarket);

        mixMarket.connect("outside", streetEnd);

        world.addLocation(station);
        world.addLocation(viaRoma);
        world.addLocation(buonissimoKebab);
        world.addLocation(streetFight);
        world.addLocation(tobaccoBack);
        world.addLocation(tunnels);
        world.addLocation(undergroundParking);
        world.addLocation(abandonedBuilding);
        world.addLocation(roof);
        world.addLocation(chaosZone);
        world.addLocation(streetEnd);
        world.addLocation(mixMarket);

        GameCharacter lucaVassena = new GameCharacter(
                "Luca Vassena",
                "Super fast, unpredictable, always armed.",
                95, 40, 35, 20);
        GameCharacter ilTossico = new GameCharacter(
                "The Junkie",
                "Nervous giant, hits hard and doesn't give up.",
                140, 45, 48, 15);
        GameCharacter ilMuro = new GameCharacter(
                "The Wall",
                "Parking lot guardian, tough to take down.",
                150, 50, 36, 40);
        GameCharacter ilBangla = new GameCharacter(
                "The Bangla",
                "Mind of the zone. Cold, technical, lethal.",
                130, 48, 52, 35);

        streetFight.addCharacter(lucaVassena);
        undergroundParking.addCharacter(ilTossico);
        undergroundParking.addCharacter(ilMuro);
        roof.addCharacter(ilBangla);

        Item mysteriousCan = new Item(
                "Mysterious Can",
                "The old man said: 'If things go bad, drink it.'",
                1, 0, true);
        Item stolenBackpack = new Item(
                "Stolen Backpack",
                "Your scattered belongings: money, documents and anger.",
                8, 0, true);
        Item electronicKey = new Item(
                "Electronic Key",
                "Opens blocked passages and improvised safes.",
                2, 0, true);
        Item streetMap = new Item(
                "Roma Street Map",
                "Marks shortcuts and secondary paths.",
                1, 0, true);
        Item contactDossier = new Item(
                "Contact Dossier",
                "Names, debts, deliveries. Worth more than a gun.",
                3, 0, true);
        Item intercityPass = new Item(
                "Intercity Pass",
                "Open ticket to leave the city tonight.",
                1, 0, true);
        Item kebab = new Item(
                "Energy Kebab",
                "Not elegant, but gets you back on your feet.",
                2, 0, true);
        Item streetKit = new Item(
                "Street Kit",
                "Band-aids, gauze and disinfectant.",
                2, 0, true);

        Weapon knife = new Weapon(
                "Pocket Knife",
                "Rusty but reliable in dirty situations.",
                5, 100, 15, 1, true, 10);
        Weapon brassKnuckles = new Weapon(
                "Brass Knuckles",
                "Small, quick, lethal up close.",
                4, 100, 20, 1, true, 12);
        Weapon ironBar = new Weapon(
                "Iron Bar",
                "Heavy but devastating when it connects.",
                8, 100, 28, 1, true, 15);

        Shield reinforcedJacket = new Shield(
                "Reinforced Jacket",
                "Improvised protection with metal plates.",
                7, 100, 14, true);
        Shield riotShield = new Shield(
                "Riot Shield",
                "Recovered from a city depot.",
                10, 100, 20, true);

        Heal healPotion = new Heal(
                "Healing Potion",
                "Restores 30 health points.",
                2, 1, true, 30, "heal");
        Heal quickMedkit = new Heal(
                "Quick Medkit",
                "Gets you back on your feet fast (+22 health).",
                2, 1, true, 22, "heal");
        PowerPotion adrenaline = new PowerPotion(
                "Adrenaline Shot",
                "Increases attack, defense and strength for 3 turns.",
                1, 3, true, 8, "power");
        PowerPotion booster = new PowerPotion(
                "Combat Booster",
                "Intense version of adrenaline.",
                1, 3, true, 12, "power");
        PoisonPotion corrosiveVial = new PoisonPotion(
                "Corrosive Vial",
                "Throw in combat: deals direct damage.",
                1, 1, true, 18, "poison");

        buonissimoKebab.addItem(kebab);
        buonissimoKebab.addItem(adrenaline);
        streetFight.addItem(brassKnuckles);
        tobaccoBack.addItem(stolenBackpack);
        tobaccoBack.addItem(streetKit);
        tobaccoBack.addItem(quickMedkit);
        tunnels.addItem(ironBar);
        tunnels.addItem(corrosiveVial);
        undergroundParking.addItem(reinforcedJacket);
        abandonedBuilding.addItem(riotShield);
        abandonedBuilding.addItem(booster);
        roof.addItem(streetMap);

        
        LockableContainer safe = new LockableContainer(
                "Rusty Safe", "An old safe. You need a key to open it.",
                0, 100, true, false);
        Weapon hiddenDagger = new Weapon("Hidden Dagger",
                "Sharp blade found in the safe.", 2, 80, 24, 1, true, 4);
        safe.addItem(hiddenDagger);
        tobaccoBack.addLockableContainer(safe);

        LockableContainer locker = new LockableContainer(
                "Broken Locker", "A semi-open metal locker. No key needed.",
                0, 100, false, true);
        Heal fieldMedkit = new Heal("Field Medkit",
                "Complete first aid kit (+35 health).", 2, 1, true, 35, "heal");
        locker.addItem(fieldMedkit);
        abandonedBuilding.addLockableContainer(locker);

        LockableContainer crate = new LockableContainer(
                "Wooden Crate", "An abandoned crate among the cars. Opens easily.",
                0, 100, false, true);
        PoisonPotion concentratedAcid = new PoisonPotion("Concentrated Acid",
                "Extremely corrosive liquid.", 1, 1, true, 28, "poison");
        crate.addItem(concentratedAcid);
        undergroundParking.addLockableContainer(crate);

        LockableContainer chest = new LockableContainer(
                "Armored Chest", "A heavy key-locked chest on the roof.",
                0, 100, true, false);
        PowerPotion superBooster = new PowerPotion("Super Adrenaline",
                "Massive dose: +15 to all stats for 4 turns.", 1, 4, true, 15, "power");
        chest.addItem(superBooster);
        roof.addLockableContainer(chest);

        protagonist.addItem(knife);
        protagonist.addItem(healPotion);

        return world;
    }
}

