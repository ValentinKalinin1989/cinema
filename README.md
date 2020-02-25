[![Build Status](https://travis-ci.org/ValentinKalinin1989/cinema.svg?branch=master)](https://travis-ci.org/ValentinKalinin1989/cinema)
<h1> Кинотеатр </h1>
<h4> Простой веб-сайт для покупки билетов в кинотеатр </h4>

<h5> 1. Начальная страница сайта index.html - зал с рядами. </h5>
![Форма для выбора мест в кинозале](https://github.com/ValentinKalinin1989/cinema/master/images/hall.png)
<h5> 2. После выбора места происходит переход на страницу payment.html - форма для покупки билета. </h5>
![Форма для заполнения данных покупателя](https://github.com/ValentinKalinin1989/cinema/master/images/payform.png)
<h5> 3. Данные на index.html загружаются из базы данных с применением ajax через определенный интервал. </h5>
<h5> 4. После покупки билета происходит атомарная запись в базу данных, если во время записи происходит ошибка, 
то операция отменяется и выводится сообщение об ошибке.  </h5>
![Сообщение об успешной покупке билета](https://github.com/ValentinKalinin1989/cinema/master/images/alert_message.png)
<h5> 5. Информация хранится в базе данных в двух таблица: halls - информация о местах в зале, accounts - информация о покупателе.  </h5>
