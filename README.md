[![Build Status](https://travis-ci.org/ValentinKalinin1989/cinema.svg?branch=master)](https://travis-ci.org/ValentinKalinin1989/cinema)
<h1> Кинотеатр </h1>
<h4> Простой веб-сайт для покупки билетов в кинотеатр </h4>

<h5> 1. Начальная страница сайта index.html - зал с рядами. Данные на index.html загружаются из базы данных с применением ajax через определенный интервал. Страница имеет следующий вид: </h5>
<img src="https://raw.githubusercontent.com/ValentinKalinin1989/cinema/master/images/hall.png">
<h5> 2. После выбора места происходит переход на страницу payment.html - форма для покупки билета.</h5>
<img src="https://raw.githubusercontent.com/ValentinKalinin1989/cinema/master/images/payform.png">
<h5> 3. После покупки билета происходит атомарная запись в базу данных, если во время записи происходит ошибка, 
то операция отменяется и выводится сообщение об ошибке. Пример вывода простого alert-сообщения приведен ниже.</h5>
<img src="https://raw.githubusercontent.com/ValentinKalinin1989/cinema/master/images/alert_message.png">
<h5> 4. Информация хранится в базе данных в двух таблицах: halls - информация о местах в зале, accounts - информация о покупателе.  </h5>


