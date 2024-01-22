package com.webprog.weatherForecast.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Класс, представляющий сущность "Пост" в системе.
 * Каждый пост содержит уникальный идентификатор, количество просмотров, заголовок, анонс, содержание и путь к файлу.
 */
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    /**
     * Уникальный идентификатор поста.
     */
    private Long id;

    /**
     * Количество просмотров поста.
     */
    private int views;

    /**
     * Заголовок поста.
     */
    private String title;

    /**
     * Анонс поста.
     */
    private String anons;

    /**
     * Содержание поста.
     */
    private String content;

    /**
     * Путь к файлу, связанному с постом.
     */
    private String filePath;

    public Post() {
    }

    /**
     * Конструктор для создания поста с указанным заголовком, анонсом и содержанием, и с путем к файлу с постом.
     *
     * @param title   Заголовок поста.
     * @param anons   Анонс поста.
     * @param content Содержание поста.
     */
    public Post(String title, String anons, String content) {
        this.title = title;
        this.anons = anons;
        this.content = content;
    }

    /**
     * Получает уникальный идентификатор поста (геттер).
     *
     * @return Уникальный идентификатор поста.
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор поста (сеттер).
     *
     * @param id Уникальный идентификатор поста.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получает количество просмотров поста (геттер).
     *
     * @return Количество просмотров поста.
     */
    public int getViews() {
        return views;
    }

    /**
     * Устанавливает количество просмотров поста (сеттер).
     *
     * @param views Количество просмотров поста.
     */
    public void setViews(int views) {
        this.views = views;
    }

    /**
     * Получает заголовок поста (геттер).
     *
     * @return Заголовок поста.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Устанавливает заголовок поста (сеттер).
     *
     * @param title Заголовок поста.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Получает анонс поста (геттер).
     *
     * @return Анонс поста.
     */
    public String getAnons() {
        return anons;
    }

    /**
     * Устанавливает анонс поста (сеттер).
     *
     * @param anons Анонс поста.
     */
    public void setAnons(String anons) {
        this.anons = anons;
    }

    /**
     * Получает содержание поста (геттер).
     *
     * @return Содержание поста.
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * Устанавливает содержание поста (сеттер).
     *
     * @param content Содержание поста.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Получает путь к файлу, связанному с постом (геттер).
     *
     * @return Путь к файлу.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Устанавливает путь к файлу, связанному с постом (сеттер).
     *
     * @param filePath Путь к файлу.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
