package zoo;

import java.util.logging.*;
import zoo.animal.*;
import zoo.enclosure.*;

public class Main {

  public static void main(String[] args) {

    
    // Logging konfigurieren
    Logger zooLogger = Logger.getLogger(Zoo.class.getName());
    zooLogger.setLevel(Level.ALL); // Logger selbst: alles loggen
    zooLogger.setUseParentHandlers(false); // Root-Logger-Handler abschalten

    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(Level.ALL); // Handler: auch alles ausgeben
    zooLogger.addHandler(handler);

    
    // Tiere erstellen
    var zoo = getZoo();

    // Zoo-Methoden demonstrieren
    System.out.println("\n=== Summary ===");
    System.out.println(zoo.summary());

    System.out.println("\n=== Alle Säugetiere ===");
    zoo.getAllMammals().forEach(System.out::println);

    System.out.println("\n=== Name enthält 'o' ===");
    zoo.getAnimalsByPredicate(a -> a.name().toLowerCase().contains("o"))
        .forEach(System.out::println);

    System.out.println("\n=== Tiertypen gezählt ===");
    zoo.countAnimalsByType().forEach((t, n) -> System.out.println("  " + t + ": " + n));

    System.out.println("\n=== Überfüllte Gehege (> 1 Tier) ===");
    zoo.getOvercrowdedEnclosures(1).forEach(System.out::println);

    System.out.println("\n=== Gehege suchen ===");
    zoo.findEnclosureByName("Großes Aquarium").ifPresent(System.out::println);
    zoo.findEnclosureByName("Nichtexistent"); // → WARNING im Log
    
    // Log-Level umschalten – Kernforderung Aufgabe 2
    System.out.println("\n\n>>> Log-Level auf WARNING – INFO/FINE werden unterdrückt");
    zooLogger.setLevel(Level.WARNING);
    handler.setLevel(Level.WARNING);
    zoo.getAllAnimals(); // INFO + FINE: nicht sichtbar
    zoo.findEnclosureByName("Gibt es nicht"); // WARNING: sichtbar!

    System.out.println("\n>>> Log-Level auf FINE – alles sichtbar");
    zooLogger.setLevel(Level.FINE);
    handler.setLevel(Level.FINE);
    zoo.getAllAnimals(); // INFO + FINE: sichtbar
  }

  private static Zoo getZoo() {
    var aquarium = new Aquarium("Großes Aquarium");
    aquarium.add(new Trout("Forelle Nemo"));
    aquarium.add(new Salmon("Lachs Sam"));

    var terrarium = new Terrarium("Reptilienhaus");
    terrarium.add(new Gecko("Gecko Gex"));
    terrarium.add(new Iguana("Iggy"));

    var mammalHouse = new MammalHouse("Säugetierhaus");
    mammalHouse.add(new Dolphin("Flipper"));
    mammalHouse.add(new Wolf("Balu"));

    // CatHouse<DomesticCat>: NUR Hauskatzen rein – Tiger würde nicht kompilieren!
    var catHouse = new CatHouse<DomesticCat>("Katzenhaus");
    catHouse.add(new DomesticCat("Miau"));

    // Zweites CatHouse für Tiger
    var tigerHouse = new CatHouse<Tiger>("Tigergehege");
    tigerHouse.add(new Tiger("Tony"));
    tigerHouse.add(new Tiger("Shere Khan"));

    var aviary = new Enclosure<Bird>("Vogelhaus");
    aviary.add(new Eagle("Aar"));
    aviary.add(new Parrot("Polly"));


    // Zoo aufbauen
    var zoo = new Zoo();
    zoo.addEnclosure(aquarium);
    zoo.addEnclosure(terrarium);
    zoo.addEnclosure(mammalHouse);
    zoo.addEnclosure(catHouse);
    zoo.addEnclosure(tigerHouse);
    zoo.addEnclosure(aviary);
    return zoo;
  }
}
