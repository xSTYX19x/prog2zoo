package zoo;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import zoo.animal.*;
import zoo.enclosure.Enclosure;

public class Zoo {
  private static final Logger logger = Logger.getLogger(Zoo.class.getName());

  private final List<Enclosure<? extends Animal>> enclosures = new ArrayList<>();

  public void addEnclosure(Enclosure<? extends Animal> enclosure) {
    logger.info("addEnclosure aufgerufen: " + enclosure.getName());
    enclosures.add(enclosure);
    logger.fine("Zoo hat jetzt " + enclosures.size() + " Gehege.");
  }

  public List<Enclosure<? extends Animal>> getEnclosures() {
    logger.info("getEnclosures aufgerufen.");
    logger.fine("Zurückgegeben: " + enclosures.size() + " Gehege.");
    return Collections.unmodifiableList(enclosures);
  }

  public Optional<Enclosure<? extends Animal>> findEnclosureByName(String name) {
    logger.info("findEnclosureByName aufgerufen: name = " + name);
    Optional<Enclosure<? extends Animal>> result =
        enclosures.stream().filter(e -> e.getName().equals(name)).findFirst();
    if (result.isEmpty()) {
      logger.warning("Gehege " + name + " nicht gefunden.");
    } else {
      logger.fine("Gehege " + name + " gefunden.");
    }
    return result;
  }

  public List<Animal> getAllAnimals() {
    logger.info("getAllAnimals aufgerufen.");
    List<Animal> animals =
        enclosures.stream()
            .<Animal>flatMap(e -> e.getInhabitants().stream()) // ← <Animal> explizit
            .toList();
    logger.fine("Gesamt: " + animals.size() + " Tiere.");
    return animals;
  }

  public List<Mammal> getAllMammals() {
    logger.info("getAllMammals aufgerufen.");
    List<Mammal> result =
        getAllAnimals().stream().filter(a -> a instanceof Mammal).map(a -> (Mammal) a).toList();
    logger.fine("Säugetiere gefunden: " + result.size());
    return result;
  }

  public List<Animal> getAnimalsByPredicate(Predicate<Animal> predicate) {
    logger.info("getAnimalsByPredicate aufgerufen.");
    List<Animal> result = getAllAnimals().stream().filter(predicate).toList();
    logger.fine("Ergebnis: " + result.size() + " Tiere.");
    return result;
  }

  public Map<String, Long> countAnimalsByType() {
    logger.info("countAnimalsByType aufgerufen.");
    Map<String, Long> counts =
        getAllAnimals().stream()
            .collect(
                Collectors.groupingBy(a -> a.getClass().getSimpleName(), Collectors.counting()));
    logger.fine("Tiertypen gezählt: " + counts);
    return counts;
  }

  public List<Enclosure<? extends Animal>> getOvercrowdedEnclosures(int maxSize) {
    logger.info("getOvercrowdedEnclosures aufgerufen: maxSize=" + maxSize);
    List<Enclosure<? extends Animal>> result =
        enclosures.stream().filter(e -> e.getInhabitants().size() > maxSize).toList();
    if (!result.isEmpty()) {
      logger.warning("Überfüllte Gehege gefunden: " + result.size());
    }
    logger.fine("Überfüllte Gehege: " + result.size());
    return result;
  }

  public String summary() {
    logger.info("summary aufgerufen.");
    List<Animal> all = getAllAnimals();
    Map<String, Long> byFamily =
        all.stream()
            .collect(
                Collectors.groupingBy(
                    a -> {
                      if (a instanceof Mammal) return "Mammals";
                      if (a instanceof Bird) return "Birds";
                      if (a instanceof Fish) return "Fish";
                      return "Reptiles"; // nur noch Reptile möglich (sealed!)
                    },
                    Collectors.counting()));

    // Collectors.joining: wie String.join, aber aus Stream
    String details =
        byFamily.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(e -> e.getValue() + " " + e.getKey())
            .collect(Collectors.joining(", "));

    String result =
        "Zoo mit " + enclosures.size() + " Gehegen und " + all.size() + " Tieren: " + details;
    logger.fine("Summary: " + result);
    return result;
  }
}
