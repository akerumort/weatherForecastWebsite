package com.webprog.weatherForecast.services;

import com.webprog.weatherForecast.models.Visit;
import com.webprog.weatherForecast.repos.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с посещениями и подсчета количества посещений.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;

    /**
     * Получает текущее количество посещений.
     *
     * @return Количество посещений.
     */
    public int getVisitCount() {
        Visit visit = visitRepository.findById(1L).orElse(new Visit());
        return visit.getVisitCount();
    }

    /**
     * Увеличивает количество посещений на единицу и сохраняет изменения.
     */
    public void incrementVisitCount() {
        Visit visit = visitRepository.findById(1L).orElse(new Visit());
        int visitCount = visit.getVisitCount();
        visit.setVisitCount(visitCount + 1);
        visitRepository.save(visit);
    }
}
