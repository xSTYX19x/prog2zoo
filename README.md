# prog2zoo

### Aufg. 3.1 - Generics:

Wo helfen Generics, Fehler zur Compile-Zeit zu vermeiden?
+ Ohne Generics müsste Enclosure mit Object oder rohem Cast arbeiten  
+ Fehlerhafte Kombinationen (z.B. Trout in das KatzenHaus) würden erst zur laufzeit einen Fehler verursachen (ClassCastException) oder garnicht erst erkannt werden
+ Mit Generics:
  + ```Auqarium extends Enclosure<Fish> -> aquarium.add(new Tiger(Titus))``` ist ein Compilerfehler und fällt schon vor Ausführung des Programms auf
  + ```Zoo.getAllMammals()``` gibt ```List<Mammal>``` zurück - hier ist kein Cast nötig

Beispiel aus Implementierung:

_Main.java:_
```
var catHouse = new CatHouse<DomesticCat>("Katzenhaus");
catHouse.add(domCat1);       // kompiliert
catHouse.add(tiger1);        // COMPILE-FEHLER:

//   add(Tiger) not applicable for CatHouse<DomesticCat>
```

### Aufg. 3.2 - Logging:

Warum Logging besser als System.out.println?

| println                         | java.util.logging                            |
|---------------------------------|----------------------------------------------|
| Immer sichtbar, nicht filterbar | Filterbar nach Level (INFO/FINE/WARNING)     |
| Kein Timestamp / Klasse         | Automatisch: Zeitstempel, Klassenname, etc.  |
| Nur Konsole                     | Handlers: Konsole, Datei, Netzwerk           |
| Zum Ausschalten: Code anfassen  | Level.OFF ohne Code-Änderung                 |
| Nicht maschinenlesbar           | Standardformat, parsbar von Monitoring-Tools |

Wann welches Level?

+ __INFO__: Normaler Methodenaufruf (addEnclosure("Aquarium"), summary())
+ __FINE__: Zustandsinfo nach Ausführung (Anzahl Tiere, Suchergebnis)
+ __WARNING__: Angefordertes Tier/Gehege nicht gefunden
+ __SEVERE__: Datenkonsistenz kaputt (z.B. Zoo-interne Liste ist null – sollte nicht passieren)

### 3.3 - Streams:

Wo haben Streams geholfen?

+ __getAllAnimals()__: flatMap ist deutlich klarer als doppelte for-Schleife über Gehege und Bewohner
+ __countAnimalsByType()__: groupingBy + counting in einer Zeile statt manuell eine Map befüllen mit if-Checks
+ __findEnclosureByName()__: findFirst() ersetzt break-Logik in einer for-Schleife
+ __summary()__: Kette aus groupingBy → entrySet → map → joining ist deklarativ und lesbar

Wo wurde es unübersichtlich?

+ **summary() mit geschachteltem groupingBy + sorted + map + joining** hat schon einige Ebenen – eine klassische Schleife mit StringBuilder wäre dort ehrlich gesagt genauso verständlich.
+ **flatMap auf List<Enclosure<? extends Animal>>** erfordert mentale Arbeit bei den Wildcard-Typen (? extends Animal), was bei einfachen for-Schleifen kein Thema wäre.