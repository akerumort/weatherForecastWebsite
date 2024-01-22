document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('weatherForm').addEventListener('submit', function (event) {
        event.preventDefault();

        // Получаем данные о погоде при отправке формы
        fetch('/?city=' + document.getElementById('city').value)
            .then(response => response.json())
            .then(data => {
                document.getElementById('temperature').textContent = data.temperature;
                document.getElementById('description').textContent = data.description;
                document.getElementById('weatherForm').style.display = 'none';
                document.getElementById('weatherResult').style.display = 'block';
            })
            .catch(error => {
                console.error('Error fetching weather data:', error);
                document.getElementById('weatherResult').innerHTML = '<p>Произошла ошибка при получении данных о погоде.</p>';
                document.getElementById('weatherResult').style.display = 'block';
            });
    });
});
