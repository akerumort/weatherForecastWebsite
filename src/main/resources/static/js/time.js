function updateClock() {
    var clockElement = document.getElementById("clock");

    fetch("/api/time")
        .then(function (response) {
            return response.text();
        })
        .then(function (time) {
            clockElement.innerHTML = time;
        })
        .catch(function (error) {
            console.error("Ошибка при получении времени с сервера: " + error);
        });
}
setInterval(updateClock, 1000);
updateClock();