package com.webprog.weatherForecast.models;

import javax.persistence.*;

/**
 * Класс, представляющий счетчик посещений сайта.
 */
@Entity
@Table(name = "visits")
public class Visit {

    /**
     * Идентификатор посещения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Количество посещений.
     */
    @Column(name = "visit_count")
    private Integer visitCount = 1;

    /**
     * Возвращает количество посещений (геттер).
     *
     * @return Количество посещений.
     */
    public int getVisitCount() {
        return visitCount;
    }

    /**
     * Устанавливает количество посещений (сеттер).
     *
     * @param visitCount Количество посещений.
     */
    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * Устанавливает идентификатор посещения (сеттер).
     *
     * @param id Идентификатор посещения.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает идентификатор посещения (геттер).
     *
     * @return Идентификатор посещения.
     */
    public Long getId() {
        return id;
    }
}
