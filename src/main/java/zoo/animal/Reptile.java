package zoo.animal;

public sealed interface Reptile extends Animal permits Gecko, Iguana {}