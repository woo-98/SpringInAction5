package tacos.Reactor;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class ReactorTest {
    @Test
    @DisplayName("배열로부터 flux 생성")
    public void createAFlux_fromArray() {
        String[] fruits = new String[] {
                "Apple", "Orange", "Grape", "Banana", "Strawberry"};
    Flux<String> fruitFlux = Flux.fromArray(fruits);

    StepVerifier.create(fruitFlux)
            .expectNext("Apple")
            .expectNext("Orange")
            .expectNext("Grape")
            .expectNext("Banana")
            .expectNext("Strawberry")
            .verifyComplete();
    }

    @Test
    @DisplayName("스트림으로부터 flux 생성")
    public void createAFlux_fromStream() {
        Stream<String> fruitStream = Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

        Flux<String> fruitFlux = Flux.fromStream(fruitStream);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    @DisplayName("컬렉션으로부터 flux 생성")
    public void createAFlux_fromIterable() {
        List<String> fruitList = new ArrayList<>(); {
            fruitList.add("Apple");
            fruitList.add("Orange");
            fruitList.add("Grape");
            fruitList.add("Banana");
            fruitList.add("Strawberry");

            Flux<String> fruitFlux = Flux.fromIterable(fruitList);

            StepVerifier.create(fruitFlux)
                    .expectNext("Apple")
                    .expectNext("Orange")
                    .expectNext("Grape")
                    .expectNext("Banana")
                    .expectNext("Strawberry")
                    .verifyComplete();
        }
    }

    @Test
    public void mergeFluxes() {
        Flux<String> charcaterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
                                         .delayElements(Duration.ofMillis(500));
        Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = charcaterFlux.mergeWith(foodFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("Apples")
                .verifyComplete();
    }

    @Test
    public void collectMap() {
        Flux<String> animalFlux = Flux.just(
                "aardvark", "elephant", "koala", "eagle", "kangaroo");

        Mono<Map<Character, String>> animalMapMono = animalFlux.collectMap(a -> a.charAt(0));

        StepVerifier
                .create(animalMapMono)
                .expectNextMatches(map -> {
                    return
                            map.size() == 3 &&
                                    map.get('a').equals("aardvark") &&
                                    map.get('e').equals("eagle") &&
                                    map.get('k').equals("kangaroo");
                })
                .verifyComplete();
    }
}

