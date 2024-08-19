package com.gridnine.testing;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();

        System.out.println("Тестовый набор");
        System.out.println(flights);

        FlightFilter filter = new FlightFilterImpl();
        List<Flight> filteredFlights = filter.filter(flights);

        System.out.println("Отфильтрованные рейсы:");
        filteredFlights.forEach(System.out::println);
    }
}