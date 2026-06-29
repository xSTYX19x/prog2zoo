package zoo.animal;

public sealed interface Mammal extends Animal permits Cat, Dolphin, Wolf, Primate, Rodent {}