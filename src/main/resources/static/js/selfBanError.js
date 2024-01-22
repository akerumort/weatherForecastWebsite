$(document).ready(function(){
    var errorParam = getParameterByName('error');
    if (errorParam !== null && errorParam.includes('self_ban')) {
        $(".alert").show();
        var countdown = 3;
        var countdownInterval = setInterval(function() {
            $(".alert").html("Вы не можете забанить самого себя. Сообщение исчезнет через " + countdown + " сек.");
            countdown--;
            if (countdown < 0) {
                clearInterval(countdownInterval);
                $(".alert").hide();
            }
        }, 1000);
    }
});

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}