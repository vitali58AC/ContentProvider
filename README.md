# ContentProvider

Стэк: ContentProvider, Retrofit, coroutine, Jetpack Compose.

Что было сделано:

  - С помощью UI фреймворка создан экран со списком контактов с устройства
  - По нажатии на контакт открывайте экран детальной информации по контакту. С помощью contentProvider выведены все номера контакта, а также адрес электронной почты.
  - Реализовано удаление контакта с помощью ContentProvider.
  - На главном экране реализовано создание новых контактов. 
  - Реализован собственный контент- провайдер. Тематика - обучающие курсы и их пользователи. Поддерживается следующий функционал: получить список курсов,
получить курс по ID, добавить курс, удалить курс по ID, удалить все курсы сразу, обновить курс по
ID.
  - Реализован доступ к custom ContentProvider из другого приложения. 
  - Добавлена возможность скачивать файл из сети и делиться им с другими приложениями
  - Работа с файлами и сетью происходит в фоновом потоке.


