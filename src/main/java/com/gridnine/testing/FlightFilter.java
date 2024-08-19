package com.gridnine.testing;

import java.util.List;

/**
 * Фильтр для перелетов
 */
public interface FlightFilter {
    List<Flight> filter(List<Flight> flights);
}
