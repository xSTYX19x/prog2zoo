package zoo;

import zoo.animal.*;
import zoo.enclosure.*;

import java.util.Map;
import java.util.logging.*;

public class Main {

  // Logging-Setup – einmalig static initialisieren

  private static final Logger      zooLogger = Logger.getLogger(Zoo.class.getName());
  private static final StreamHandler handler = new StreamHandler(System.out, new SimpleFormatter()) {
    @Override
    public synchronized void publish(LogRecord record) {
      super.publish(record);
      flush(); // sofort flushen, nicht puffern
    }
  };

  static {
    zooLogger.setUseParentHandlers(false); // Root-Logger-Duplikate vermeiden
    handler.setLevel(Level.ALL);
    zooLogger.addHandler(handler);
    zooLogger.setLevel(Level.ALL);         // Start: alles sichtbar
  }

  // Hilfsmethoden

  private static void setLogLevel(Level level) {
    zooLogger.setLevel(level);
    handler.setLevel(level);
    System.out.println("\n[LOG-LEVEL → " + level.getName() + "]\n");
  }

  private static void section(String title) {
    System.out.println("\n" + "═".repeat(62));
    System.out.println("  " + title);
    System.out.println("═".repeat(62));
  }

  // Main

  public static void main(String[] args) {

    // Tiere anlegen

    // Fish
    var trout1  = new Trout("Forelle Nemo");
    var trout2  = new Trout("Forelle Dory");
    var salmon1 = new Salmon("Lachs Sam");

    // Reptile
    var gecko1  = new Gecko("Gecko Gex");
    var iguana1 = new Iguana("Iguana Iggy");
    var iguana2 = new Iguana("Iguana Ivan");

    // Mammal – direkt
    var dolphin1 = new Dolphin("Flipper");
    var wolf1    = new Wolf("Balu");
    var wolf2    = new Wolf("Lobo");

    // Mammal – über Cat
    var domCat1 = new DomesticCat("Miau");
    var domCat2 = new DomesticCat("Schnurr");
    var tiger1  = new Tiger("Tony");
    var tiger2  = new Tiger("Shere Khan");

    // Mammal – über Rodent
    var mouse1   = new Mouse("Mickey");
    var mouse2   = new Mouse("Minnie");
    var hamster1 = new Hamster("Hamsti");

    // Mammal – über Primate
    var chimp1   = new Chimpanzee("Charlie");
    var gorilla1 = new Gorilla("King Kong");
    var gorilla2 = new Gorilla("Harambe");

    // Bird
    var eagle1  = new Eagle("Aar");
    var parrot1 = new Parrot("Polly");
    var parrot2 = new Parrot("Rio");

    // Gehege anlegen und befüllen (außerhalb Zoo, kein Logging hier)

    var aquarium     = new Aquarium("Großes Aquarium");
    aquarium.add(trout1);
    aquarium.add(trout2);
    aquarium.add(salmon1);

    var terrarium    = new Terrarium("Reptilienhaus");
    terrarium.add(gecko1);
    terrarium.add(iguana1);
    terrarium.add(iguana2);

    var mammalHouse  = new MammalHouse("Säugetierhaus");
    mammalHouse.add(dolphin1);
    mammalHouse.add(wolf1);
    mammalHouse.add(wolf2);

    var catHouse     = new CatHouse<DomesticCat>("Katzenhaus");
    catHouse.add(domCat1);
    catHouse.add(domCat2);

    var tigerHouse   = new CatHouse<Tiger>("Tigergehege");
    tigerHouse.add(tiger1);
    tigerHouse.add(tiger2);

    var rodentHouse  = new Enclosure<Rodent>("Kleintierhaus");
    rodentHouse.add(mouse1);
    rodentHouse.add(mouse2);
    rodentHouse.add(hamster1);

    var primateHouse = new Enclosure<Primate>("Primatenhaus");
    primateHouse.add(chimp1);
    primateHouse.add(gorilla1);
    primateHouse.add(gorilla2);

    var aviary       = new Enclosure<Bird>("Vogelhaus");
    aviary.add(eagle1);
    aviary.add(parrot1);
    aviary.add(parrot2);

    // AB HIER: Zoo-Methoden mit Logging auf Level.ALL
    
    // 1. addEnclosure → loggt INFO + FINE

    section("1. addEnclosure – Gehege hinzufügen  [→ INFO + FINE]");
    var zoo = new Zoo();
    zoo.addEnclosure(aquarium);
    zoo.addEnclosure(terrarium);
    zoo.addEnclosure(mammalHouse);
    zoo.addEnclosure(catHouse);
    zoo.addEnclosure(tigerHouse);
    zoo.addEnclosure(rodentHouse);
    zoo.addEnclosure(primateHouse);
    zoo.addEnclosure(aviary);

    // 2. getEnclosures → loggt INFO + FINE

    section("2. getEnclosures – alle Gehege auflisten  [→ INFO + FINE]");
    zoo.getEnclosures().forEach(System.out::println);

    // 3. findEnclosureByName – GEFUNDEN → INFO + FINE

    section("3. findEnclosureByName – gefunden  [→ INFO + FINE]");
    zoo.findEnclosureByName("Großes Aquarium")
            .ifPresent(e -> System.out.println("Gefunden: " + e));

    zoo.findEnclosureByName("Tigergehege")
            .ifPresent(e -> System.out.println("Gefunden: " + e));

    // 4. findEnclosureByName – NICHT GEFUNDEN → WARNING

    section("4. findEnclosureByName – nicht gefunden  [→ WARNING]");
    zoo.findEnclosureByName("Nichtexistentes Gehege");
    zoo.findEnclosureByName("Drachenhöhle");

    // 5. getAllAnimals → INFO + FINE

    section("5. getAllAnimals – alle Tiere im Zoo  [→ INFO + FINE]");
    var allAnimals = zoo.getAllAnimals();
    System.out.println("Gesamt: " + allAnimals.size() + " Tiere");
    allAnimals.forEach(a -> System.out.println("  " + a));

    // 6. getAllMammals → INFO + FINE
    //    Zeigt: Tiger/DomCat (über Cat), Mouse/Hamster (über Rodent),
    //    Chimp/Gorilla (über Primate), Dolphin/Wolf direkt → alle Mammal

    section("6. getAllMammals – inkl. Cat, Rodent, Primate  [→ INFO + FINE]");
    zoo.getAllMammals().forEach(m -> System.out.println("  " + m));

    // 7. getAnimalsByPredicate – verschiedene Lambdas → INFO + FINE

    section("7. getAnimalsByPredicate – Prädikat-Beispiele  [→ INFO + FINE]");

    System.out.println("→ Name enthält 'o' (case-insensitive):");
    zoo.getAnimalsByPredicate(a -> a.name().toLowerCase().contains("o"))
            .forEach(a -> System.out.println("    " + a));

    System.out.println("\n→ Alle Vögel (instanceof Bird):");
    zoo.getAnimalsByPredicate(a -> a instanceof Bird)
            .forEach(a -> System.out.println("    " + a));

    System.out.println("\n→ Alle Katzen (instanceof Cat):");
    zoo.getAnimalsByPredicate(a -> a instanceof Cat)
            .forEach(a -> System.out.println("    " + a));

    System.out.println("\n→ Alle Nagetiere (instanceof Rodent):");
    zoo.getAnimalsByPredicate(a -> a instanceof Rodent)
            .forEach(a -> System.out.println("    " + a));

    System.out.println("\n→ Name beginnt mit 'G':");
    zoo.getAnimalsByPredicate(a -> a.name().startsWith("G"))
            .forEach(a -> System.out.println("    " + a));

    System.out.println("\n→ Kein Treffer (Name = 'Nemo123'):");
    var empty = zoo.getAnimalsByPredicate(a -> a.name().equals("Nemo123"));
    System.out.println("    Ergebnis: " + empty.size() + " Tiere (leer erwartet)");

    // 8. countAnimalsByType → INFO + FINE

    section("8. countAnimalsByType – Anzahl pro konkretem Typ  [→ INFO + FINE]");
    zoo.countAnimalsByType().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> System.out.printf("  %-15s : %d%n", e.getKey(), e.getValue()));

    // 9. getOvercrowdedEnclosures – KEIN Treffer → kein WARNING

    section("9. getOvercrowdedEnclosures – maxSize=10, kein Gehege überfüllt  [→ FINE]");
    var none = zoo.getOvercrowdedEnclosures(10);
    System.out.println("Überfüllte Gehege: " + none.size() + " (keines erwartet)");

    // 10. getOvercrowdedEnclosures – MIT Treffern → WARNING

    section("10. getOvercrowdedEnclosures – maxSize=2  [→ WARNING + FINE]");
    zoo.getOvercrowdedEnclosures(2)
            .forEach(e -> System.out.println("  Überfüllt: " + e));

    section("10b. getOvercrowdedEnclosures – maxSize=1 (fast alle überfüllt)  [→ WARNING]");
    zoo.getOvercrowdedEnclosures(1)
            .forEach(e -> System.out.println("  Überfüllt: " + e));

    // 11. summary → INFO + FINE

    section("11. summary  [→ INFO + FINE]");
    System.out.println(zoo.summary());

    // ENCLOSURE-VERHALTEN (kein Zoo-Logging – Enclosure hat keinen Logger)
    
    // 12. Enclosure.add – Duplikat-Ablehnung durch Set-Semantik
    //     Records: equals/hashCode komponentenbasiert →
    //     new Trout("Forelle Nemo") == trout1 (Wert-Gleichheit!)
    

    section("12. Enclosure.add – Duplikat-Ablehnung (Set-Semantik, kein Logger)");

    boolean addExisting  = aquarium.add(trout1);                   // gleiche Referenz
    boolean addSameValue = aquarium.add(new Trout("Forelle Nemo")); // neues Objekt, gleicher Wert
    boolean addNew       = aquarium.add(new Trout("Forelle Fritz")); // wirklich neu
    System.out.println("add(bestehende Referenz)        → " + addExisting  + "  (false erwartet)");
    System.out.println("add(neues Obj, gleicher Name)   → " + addSameValue + "  (false erwartet)");
    System.out.println("add(echtes neues Tier)          → " + addNew       + "  (true erwartet)");
    System.out.println("Aquarium hat jetzt: " + aquarium.getInhabitants().size() + " Tiere");

    
    // 13. Enclosure.remove – vorhanden vs. nicht vorhanden
    

    section("13. Enclosure.remove – vorhanden vs. nicht vorhanden (kein Logger)");

    boolean removedOk  = aquarium.remove(new Trout("Forelle Fritz")); // gerade hinzugefügt
    boolean removedNot = aquarium.remove(new Trout("Forelle Atlantis")); // nie da
    System.out.println("remove(vorhandenes Tier)     → " + removedOk  + "  (true erwartet)");
    System.out.println("remove(nicht vorhandenes Tier) → " + removedNot + "  (false erwartet)");
    System.out.println("Aquarium hat jetzt: " + aquarium.getInhabitants().size() + " Tiere");

    
    // 14. getInhabitants – unveränderliche Kopie
    

    section("14. Enclosure.getInhabitants – unmodifiable (kein Logger)");
    var inhabitants = aquarium.getInhabitants();
    System.out.println("Bewohner: " + inhabitants);
    try {
      inhabitants.add(new Trout("Schmuggelfisch")); // muss UnsupportedOperationException werfen
      System.out.println("FEHLER: Liste hätte unveränderlich sein sollen!");
    } catch (UnsupportedOperationException e) {
      System.out.println("UnsupportedOperationException ✓ – Liste ist korrekt unveränderlich.");
    }
    
    // LOG-LEVEL-DEMO – Umschalten zur Laufzeit

    section("15. Log-Level-Demo – Umschalten ohne Code-Änderung");

    // WARNING: nur Warnings sichtbar, INFO + FINE unterdrückt
    System.out.println("--- Level: WARNING ---");
    setLogLevel(Level.WARNING);
    zoo.getAllAnimals();                       // INFO + FINE: unterdrückt, keine Ausgabe
    zoo.summary();                            // INFO + FINE: unterdrückt, keine Ausgabe
    zoo.findEnclosureByName("Nichtexistent"); // WARNING: sichtbar ✓
    zoo.getOvercrowdedEnclosures(1);          // WARNING: sichtbar ✓

    // INFO: Methodenaufrufe sichtbar, Zustandsdetails (FINE) unterdrückt
    System.out.println("--- Level: INFO ---");
    setLogLevel(Level.INFO);
    zoo.getAllAnimals();  // INFO: sichtbar / FINE: unterdrückt
    zoo.summary();       // INFO: sichtbar / FINE: unterdrückt

    // FINE: alles sichtbar
    System.out.println("--- Level: FINE (= alles) ---");
    setLogLevel(Level.FINE);
    zoo.getAllAnimals();  // INFO + FINE: beide sichtbar ✓
    zoo.summary();       // INFO + FINE: beide sichtbar ✓
  }
}