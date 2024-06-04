<h1 align="center" id="title">weatherForecastWebsite</h1>

<p align="center"><img src="https://socialify.git.ci/akerumort/weatherForecastWebsite/image?description=1&font=Raleway&language=1&name=1&owner=1&pattern=Circuit%20Board&theme=Dark"></p>

<p id="description">Данный сайт представляет собой прогноз погоды с возможностью регистрации на нём. Доступно 3 раздела: главная страница с погодой, блог с новостями, раздел "О нас". Для авторизированных пользователей доступен личный кабинет, функционал которого меняется в зависимости от роли пользователя (Пользователь, Админ, Модер).</p>

<h2>⚙️ Функции: </h2>

В проекте было реализовано следующее:

*   Прогноз погоды с использованием OpenWeatherMap API
*   Регистрация пользователей с использованием Spring Security
*   Двухфакторная аутентификация с отправкой кода на почту
*   Управление пользователями с помощью панели администратора
*   Управление постами в Блоге с помощью панели модератора
*   Управление данными в Личном Кабинете и загрузка изображения на портал
*   Светлая/темная тема на сайте

<h2>🛠️ Гайд по установке: </h2>

<p>1. Открыть папку с проектом в среде разработки (у меня IntellijIDEA) </p>

<p>2. Ввести в application.properties ключ OpenWeatherMap после регистрации на сайте https://openweathermap.org/</p>

```
weather.api.key=yourkeyafterregistration
```

<p>3. В том же файле ввести свою почту, с которой будет происходить отправка сообщений с кодом 2FA, а также код приложений. Подробнее: https://support.google.com/accounts/answer/185833?hl=ru </p>

```
spring.mail.username=youremail@gmail.com spring.mail.password=yourpasswordkey
```

<p>4. Запустить локальный сервер</p>

```
Я использовал OpenServer и в PhpMyAdmin создал базу данных с названием проекта
```

<p>5. Запустить исполняемый файл WeatherForecastApplication.java</p>

<h2>📃 Документация: </h2>
<a href="https://akerumort.github.io/weather_forecast_site/"> *Доступно по ссылке (кликабельно)* </a>



<h2>💻 Использованные технологии: </h2>

*   Java 17
*   Maven
*   Spring Boot (версия 2.5.3)
*   Spring Security
*   MySQL
*   Openweather API
  
<h2>🛡️ Лицензия / License: </h2>

Этот проект распространяется по лицензии MIT / This project is licensed under the MIT License
