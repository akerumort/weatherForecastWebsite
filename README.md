<h1 align="center" id="title">weather_forecast_site</h1>

<p align="center"><img src="https://socialify.git.ci/akerumort/weather_forecast_site/image?font=Raleway&amp;language=1&amp;name=1&amp;owner=1&amp;pattern=Solid&amp;theme=Dark" alt="project-image"></p>

<p id="description">Данный сайт представляет собой прогноз погоды с возможностью регистрации на нём. Доступно 3 раздела: главная страница с погодой, блог с новостями, раздел "О нас". Для авторизированных пользователей доступен личный кабинет, функционал которого меняется в зависимости от роли пользователя (Пользователь, Админ, Модер).</p>

<h2>Project Screenshots:</h2>

В разработке...
<img src="" alt="project-screenshot" width="300" height="300/">

<h2>🧐 Features </h2>

Here're some of the project's best features:

*   Прогноз погоды с использованием OpenWeatherMap API
*   Регистрация пользователей с использованием Spring Security
*   Двухфакторная аутентификация с отправкой кода на почту
*   Управление пользователями с помощью панели администратора
*   Управление постами в Блоге с помощью панели модератора
*   Управление данными в Личном Кабинете и загрузка изображения на портал
*   Светлая/темная тема на сайте
*   Использование MySQL в качестве базы данных

<h2>🛠️ Installation Steps:</h2>

<p>1. Открыть папку с проектом в среде разработки</p>

<p>2. Ввести в application properties ключ OpenWeatherMap после регистрации</p>

```
yourkeyafterregistration
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

  
<h2>💻 Built with </h2>

Technologies used in the project:

*   Java
*   Spring Security

<h2>🛡️ License:</h2>

This project is licensed under the MIT License.
