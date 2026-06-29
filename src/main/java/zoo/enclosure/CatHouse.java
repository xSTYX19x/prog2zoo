package zoo.enclosure;

import zoo.animal.Cat;

public class CatHouse<T extends Cat> extends Enclosure<T> {
  public CatHouse(String name) {
    super(name);
  }
}
