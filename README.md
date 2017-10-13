# kappaDI
Easy to use Scala/Java DI library

### Usage

The code below presents a simple coffee machine model written with kappaDI and Java.
All classes are located in a package 'example'

```java
package example;
import pl.jaca.kappadi.service.Qualified;
import pl.jaca.kappadi.service.Service;
import pl.jaca.kappadi.service.ServiceBuilder;

@Service //service declaration
public class CoffeeMaker {

    private Heater heater;
    private Pump pump;

    @ServiceBuilder  // There is no need to use this annotation, if only one constructor is present
    public CoffeeMaker( // Dependencies are listed in a constructor
            @Qualified(qualifier = "water") Heater heater,
            Pump pump
    ) {
        this.heater = heater;
        this.pump = pump;
    }
    
    // Constructor is not annotated with a @ServiceBuilder, and thus it is ignored
    public CoffeeMaker() {
    
    }

    public void makeCoffee() {
        heater.heat();
        pump.pump();
        System.out.println("Coffee!");
    }
}
```

```java
package example;
import pl.jaca.kappadi.service.Service;

@Service
public class Pump {

    public void pump() {
        System.out.println("Pumping...");
    }

}
```

```java
package example;

public interface Heater {
    void heat();
}
```

```java
package example;
import pl.jaca.kappadi.service.Qualified;
import pl.jaca.kappadi.service.Service;

@Service
@Qualified(qualifier = "water")
public static class WaterHeater implements Heater {

    public void heat() {
        System.out.println("Heating with water...");
    }

}
```

```java
package example;
import pl.jaca.kappadi.service.Qualified;
import pl.jaca.kappadi.service.Service;

@Service
// You can create qualifiers to allow resolving services with the same type
@Qualified(qualifier = "hotPlate")
public static class HotPlateHeater implements Heater {

    public void heat() {
        System.out.println("Heating with a hot plate...");
    }

}
```


```java
package example;

import pl.jaca.kappadi.Context;

public class Example {

    public static void main(String[] args) {
        // application root package is passed as argument, creates the whole service graph
        Context context = Context.create("example"); 
        CoffeeMaker maker = context.findOne(CoffeeMaker.class).get();
        maker.makeCoffee();
    }
}
```

The resulting output is:
```
heating with water...
Pumping...
Coffee!
```

