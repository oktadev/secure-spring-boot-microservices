package com.example.gateway.web;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
class CoolCarController {

    private final CarClient carClient;

    public CoolCarController(CarClient carClient) {
        this.carClient = carClient;
    }

    @GetMapping("/cool-cars")
    public String coolCars() {
        var cars = carClient.readCars()
                .stream()
                .filter(this::isCool)
                // Add fire emoji and line break
                .map(car -> "\uD83D\uDD25" + car.name() + "<br/>")
                .reduce("", String::concat);

        return "These are cool cars!<br/><br/>" + cars;
    }

    private boolean isCool(Car car) {
        return !car.name().equals("AMC Gremlin") &&
                !car.name().equals("Triumph Stag") &&
                !car.name().equals("Ford Pinto") &&
                !car.name().equals("Yugo GV");
    }
}

record Car(String name) {
}

@FeignClient(name = "car-service", fallback = Fallback.class)
interface CarClient {

    @GetMapping("/cars")
    Collection<Car> readCars();

}

@Component
class Fallback implements CarClient {

    @Override
    public Collection<Car> readCars() {
        return Collections.emptyList();
    }
}