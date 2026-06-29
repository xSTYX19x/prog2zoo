package zoo.animal;

public sealed interface Animal permits Fish, Reptile, Mammal, Bird {
  String name();
}
