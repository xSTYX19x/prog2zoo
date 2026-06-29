package zoo.enclosure;

import java.util.*;
import zoo.animal.Animal;

public class Enclosure<T extends Animal> {

  private final String name;

  private final Set<T> inhabitants = new LinkedHashSet<>();

  public Enclosure(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public boolean add(T animal) {
    return inhabitants.add(animal);
  }

  public boolean remove(T animal) {
    return inhabitants.remove(animal);
  }

  public List<T> getInhabitants() {
    return List.copyOf(inhabitants);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + name + ", " + inhabitants.size() + "Tiere]";
  }
}
