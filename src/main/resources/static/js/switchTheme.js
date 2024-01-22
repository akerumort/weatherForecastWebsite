document.addEventListener('DOMContentLoaded', function () {
    // Проверяем состояние темы при загрузке страницы
    var savedTheme = localStorage.getItem('theme');
    var prefersTheme = window.matchMedia('(prefers-color-scheme: dark)').matches;

    // Если в localStorage нет сохраненной темы, используем предпочтения пользователя или темную тему по умолчанию
    var initialTheme = savedTheme || (prefersTheme ? 'darkTheme' : 'lightTheme');
    applyTheme(initialTheme);

    document.getElementById('checkbox').addEventListener('change', function () {
        var theme = this.checked ? 'darkTheme' : 'lightTheme';
        localStorage.setItem('theme', theme);
        applyTheme(theme);
    });
});

function applyTheme(theme) {
    document.body.classList.remove('lightTheme', 'darkTheme');
    document.body.classList.add(theme);
    document.getElementById('checkbox').checked = theme === 'darkTheme';
}
