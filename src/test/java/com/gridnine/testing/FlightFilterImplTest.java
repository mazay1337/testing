package com.gridnine.testing;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FlightFilterImplTest {

    private FlightFilterImpl flightFilter;

    @BeforeEach
    public void setUp() {
        flightFilter = new FlightFilterImpl();
    }

    @Test
    public void testFilterPastDeparturesPositive() {

        LocalDateTime now = LocalDateTime.now();

        List<Flight> flights = new ArrayList<>();
        flights.add(FlightBuilder.createFlight(now.plusHours(1), now.plusHours(3)));
        flights.add(FlightBuilder.createFlight(now.plusDays(1), now.plusDays(1).plusHours(1)));
        flights.add(FlightBuilder.createFlight(now.minusHours(1), now.minusHours(1).plusHours(2)));

        List<Flight> filteredFlights = flightFilter.filterPastDepartures(flights);

        assertEquals(2, filteredFlights.size());
        assertTrue(filteredFlights.stream().allMatch(flight ->
                flight.getSegments().get(0).getDepartureDate().isAfter(now)));
    }

    @Test
    public void testFilterPastDeparturesNegative() {

        LocalDateTime now = LocalDateTime.now();

        List<Flight> flights = new ArrayList<>();

        flights.add(FlightBuilder.createFlight(now.minusHours(1), now.minusHours(1).plusHours(2)));
        flights.add(FlightBuilder.createFlight(now.minusDays(1), now.minusDays(1).plusHours(1)));

        List<Flight> filteredFlights = flightFilter.filterPastDepartures(flights);

        assertTrue(filteredFlights.isEmpty());
    }


    @Test
    public void testFilterArrivalBeforeDeparturePositive() {
        LocalDateTime now = LocalDateTime.now();
        List<Flight> flights = new ArrayList<>();

        flights.add(FlightBuilder.createFlight(now.plusHours(1), now.plusHours(2)));
        flights.add(FlightBuilder.createFlight(now.plusHours(3), now.plusHours(4)));

        List<Flight> filteredFlights = flightFilter.filterArrivalBeforeDeparture(flights);

        assertEquals(2, filteredFlights.size());
    }

    @Test
    public void testFilterArrivalBeforeDepartureNegative() {

        LocalDateTime now = LocalDateTime.now();
        List<Flight> flights = new ArrayList<>();

        flights.add(FlightBuilder.createFlight(now.plusHours(1), now.plusHours(2)));
        flights.add(FlightBuilder.createFlight(now.plusHours(2), now.plusHours(1)));

        List<Flight> filteredFlights = flightFilter.filterArrivalBeforeDeparture(flights);

        assertEquals(1, filteredFlights.size());
        assertTrue(filteredFlights.stream().allMatch(flight ->
                flight.getSegments().stream().noneMatch(segment ->
                        segment.getArrivalDate().isBefore(segment.getDepartureDate()))));
    }


    @Test
    public void testFilterFlightsWithExcessiveGroundTimePositive() {

        LocalDateTime now = LocalDateTime.now();
        List<Flight> flights = new ArrayList<>();

        flights.add(FlightBuilder.createFlight(now.plusHours(1), now.plusHours(2), now.plusHours(3), now.plusHours(4)));
        flights.add(FlightBuilder.createFlight(now.plusHours(5), now.plusHours(6), now.plusHours(7), now.plusHours(8)));

        List<Flight> filteredFlights = flightFilter.filterFlightsWithExcessiveGroundTime(flights);

        assertEquals(2, filteredFlights.size());
    }

    @Test
    public void testFilterFlightsWithExcessiveGroundTimeNegative() {

        LocalDateTime now = LocalDateTime.now();
        List<Flight> flights = new ArrayList<>();

        flights.add(FlightBuilder.createFlight(now.plusHours(1), now.plusHours(2), now.plusHours(5), now.plusHours(6)));
        flights.add(FlightBuilder.createFlight(now.plusHours(7), now.plusHours(8), now.plusHours(12), now.plusHours(13)));

        List<Flight> filteredFlights = flightFilter.filterFlightsWithExcessiveGroundTime(flights);

        assertTrue(filteredFlights.isEmpty());
    }

}

