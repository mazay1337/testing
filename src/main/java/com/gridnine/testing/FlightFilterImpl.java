package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Реализация интерфейса {@link FlightFilter} для фильтрации списка перелетов по критериям.
 *
 * <p>Класс предоставляет методы для фильтрации перелетов на основании следующих правил:</p>
 * <ul>
 *     <li>Удаление рейсов, которые уже состоялись.</li>
 *     <li>Удаление рейсов, где дата прибытия сегмента раньше даты вылета.</li>
 *     <li>Удаление рейсов, у которых общее время ожидания на земле превышает 2 часа.</li>
 * </ul>
 *
 * <p>Каждый метод фильтрации выводит список отфильтрованных рейсов в консоль,
 * а также возвращает новый список, содержащий только соответствующие рейсы.</p>
 */
public class FlightFilterImpl implements FlightFilter {

    @Override
    public List<Flight> filter(List<Flight> flights) {
        return filterFlightsWithExcessiveGroundTime(
                filterArrivalBeforeDeparture(
                        filterPastDepartures(flights)
                )
        );
    }

    /**
     * Метод фильтрации списка перелетов от уже прошедших.
     *
     * @param flights список рейсов
     * @return Список перелетов, которые стартуют в будущем
     */
    public List<Flight> filterPastDepartures(List<Flight> flights) {
        LocalDateTime now = LocalDateTime.now();
        return flights.stream()
                .filter(flight -> flight.getSegments().get(0).getDepartureDate().isAfter(now))
                .collect(Collectors.toList());
    }

    /**
     * Метод фильтрации списка перелетов от тех, у которых дата прибытия раньше вылета.
     *
     * @param flights список рейсов
     * @return Отфильтрованный список перелетов
     */
    public List<Flight> filterArrivalBeforeDeparture(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getSegments().stream()
                        .noneMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate())))
                .collect(Collectors.toList());
    }

    /**
     * Метод для фильтрации списка перелетов от рейсов, у которых общее время ожидания на земле превышает 2 часа.
     *
     * @param flights список рейсов
     * @return Список перелетов, у которых ожидание на земле менее 2-х часов
     */
    public List<Flight> filterFlightsWithExcessiveGroundTime(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> {
                    List<Segment> segments = flight.getSegments();
                    long totalGroundTime = 0;
                    for (int i = 0; i < segments.size() - 1; i++) {
                        long groundTime = java.time.Duration.between(segments.get(i).getArrivalDate(),
                                segments.get(i + 1).getDepartureDate()).toHours();
                        totalGroundTime += groundTime;
                    }
                    return totalGroundTime <= 2;
                })
                .collect(Collectors.toList());
    }
}
