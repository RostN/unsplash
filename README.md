<h1>Промежуточная аттестация проводится по результатам проверки выполнения проекта слушателя: в результате должна получиться программа, позволяет просматривать фотографии и коллекции фотографий пользователей (UNSPLASH)</h1>
<H2>Общая информация</H2>
<br>Приложение для популярного сервиса с фотографиями Unsplash.
<br>Позволяет смотреть фотографии, коллекции фотографий. 
<br>Позволяет лайкать и скачивать понравившиеся фотографии.
<h3><a href="https://play.google.com/store/apps/details?id=com.unsplash.free&hl=ru&gl=US">Оригинальное приложение</a></h3>
<li>Обрабатываются состояния экранов («Ошибка»/«Лоадинг»/«Пусто»).
<li>Добавлена иконка.
<li>Приложение имеет минимум два языка: русский и английский.
<li>Приложение может работать в любой ориентации: ландшафтной и портретной (отдельного дизайна для ландшафтной ориентации нет, использовать дизайн от портретной).
<li>Приложение должно выглядеть похоже на экранах с различной плотностью и размерами.
<li>Основная часть приложения доступна после авторизации с помощью аккаунта Unsplash (необходимо зарегистрироваться).
<li>В основной части три раздела в bottom navigation menu: «Фотографии», «Коллекции», «Пользователь».
<li>Общее описание API.
<li>Target sdk = 30.
<li>Min sdk = 21.
<li>Подготовить приложение к релизу.</li>

<h3>Сценарии использования</h3>
<h4>Хочу ознакомиться с основными функциями приложения на экране онбординга</h4>
<li>Онбординг отображается сразу при первом запуске приложения.
<h4>Хочу авторизоваться</h4>
<li>Экран авторизации отображается всегда следующим экраном после онбординга.
<li>Пользоваться приложением без авторизации нельзя.
<br>API<br>
<a href="https://unsplash.com/documentation/user-authentication-workflow">Аутентификация</a>

<br><a href="https://github.com/openid/AppAuth-Android">  AppAuth for Android </a>
<br>Удостоверьтесь, что вы запрашиваете все требуемые scope во время авторизации.
<br>Требуемые скоупы описаны в документации к методам API.

<h4>Хочу посмотреть список фотографий</h4>

<li>Для каждой фотографии в списке отобразить картинку, лайк от текущего пользователя (или отсутствие лайка), общее количество лайков, создателя фотографии (имя/аватар).
<li>Для списка есть пагинация.
<li>Список кешируется в БД.
<li>В случае отсутствия соединения с сетью отобразить закешированные фотографии из БД с оповещением пользователя.
<br><a href="https://unsplash.com/documentation#list-photos">API</a>



<h4>Хочу посмотреть детальную информацию о фотографии</h4>

<li>При нажатии на фотографию пользователь переходит на экран детальной информации фотографии.</li>
Пользователь видит расширенную информацию:
<li>Фотография.
<li>Exif.
<li>Локация (по нажатию на локацию открыть её в приложении с картами на устройстве).
<li>Теги.
<li>Информация об авторе.
<li>Количество скачиваний.
<li>Количество лайков.
<li>Лайк от меня.
<li>Кнопка «Скачать».
  <br><a href="https://unsplash.com/documentation#get-a-photo">API</a>



<h4>Хочу поделиться ссылкой на фотографию</h4>

<li>На экране с детальной информацией по фото можно нажать на кнопку «Поделиться» в тулбаре.
<li>В этот момент генерируется ссылка вида https://unsplash.com/photos/<photo_id>, которая может быть расшарена в другие приложения.
<br>API не требуется, ссылка генерируется на клиенте.



<h4>Хочу открыть ссылку на фотографию в приложении</h4>

<li>В приложении должна быть возможность поддержать открытие ссылок вида https://unsplash.com/photos/<photo_id>.
<li>В этом случае должен открываться экран с детальной информацией по фото с указанным ID.
<br>API не требуется.



<h4>Хочу скачать фотографию на устройство</h4>

<li>По нажатию на кнопку «Скачать» на экране детальной информации о фотографии необходимо скачать фотографию в исходном качестве на устройство и сохранить в общедоступное место на диске.
<li>В случае отсутствия сети отложить скачивание до момента появления сети и автоматически скачать фотографию позже.
<li>По окончании загрузки показать snackbar, по нажатию на который откроется просмотр фотографии на устройстве с помощью внешнего приложения.
<li>Если фотография загружалась в фоне, то показать оповещение после успешной загрузки.
<li>По нажатию на оповещение открывается просмотр фотографии на устройстве с помощью внешнего приложения.
<br>API
<br><a href="https://unsplash.com/documentation#get-a-photo">Скачать фотографию </a> ― фотографию в исходном качестве можно получить из поля photo.urls.raw
<br><a href="https://unsplash.com/documentation#track-a-photo-download">Отслеживание загрузки фотографии</a>



<h4>Хочу лайкнуть фотографию</h4>

<li>На экране с детальной информацией и списке фото пользователь может поставить лайк фотографии и убрать лайк, если он уже стоит. В таком случае должны дополнительно обновиться счётчики лайков.
<li>Операция производится только при наличии доступа в интернет.
<br>API
<br><a href="https://unsplash.com/documentation#like-a-photo">  Поставить лайк</a>
<br><a href="https://unsplash.com/documentation#unlike-a-photo">  Убрать лайк</a>



<h4>Хочу найти фотографию</h4>

<li>На списке фотографий есть элемент поиска в тулбаре. По нажатию на элемент поиска он превращается в поле для ввода. 
<li>Поиск реализовать либо на экране со списком, либо на отдельном экране, который будет открываться по нажатию на лупу поиска.
<br><a href="https://unsplash.com/documentation#search-photos">API</a>



<h4>Хочу посмотреть список коллекций</h4>

<li>По нажатию на таб с коллекциями фотографий открыть список коллекций фотографий.
<li>Поведение списка аналогично списку фотографий.
<li>По возможности ― кешировать информацию о полученных коллекциях.
<br><a href="https://unsplash.com/documentation#list-collections">API</a>



<h4>Хочу посмотреть список фотографий в коллекции</h4>

<li>По нажатию на коллекцию открыть экран с детальной информацией по коллекции и списком фотографий по коллекции.
<li>По нажатию на фото можно открыть экран детальной информации.
<li>По возможности ― кешировать информацию о полученных фотографиях в коллекции.
<br>API
<br><a href="https://unsplash.com/documentation#get-a-collections-photos">  Фотографии коллекции</a>
<br><a href="https://unsplash.com/documentation#get-a-collection">Коллекция</a>



<h4>Хочу посмотреть свой профиль и понравившиеся мне фотографии</h4>

<li>На вкладке с профилем вывести доступную информацию о текущем пользователе при наличии информации о местоположении. Его можно посмотреть на карте во внешнем приложении.
API
<br><a href="https://unsplash.com/documentation#get-the-users-profile">  Информация обо мне</a>
<br><a href="https://unsplash.com/documentation#list-a-users-liked-photos">  Лайкнутые фотографии</a>



<h4>Хочу выйти из профиля</h4>

<li>При нажатии на кнопку выхода в профиле появляется диалог: «Вы уверены, что хотите выйти? Все локальные данные будут удалены.».
<li>Если пользователь подтверждает, то происходит логаут и удаление всех локальных данных приложения. 
<li>Сразу после очистки и при последующих запусках приложения открывается экран авторизации.
<br>API не требуется.



<h3>Материалы для работы</h3>
<a href="https://www.figma.com/file/OHji64ZI8ZRSOsuY48tCZd/Unfold?node-id=0%3A1">Прототип в Figma</a><br>
<a href="https://api.selcdn.ru/v1/SEL_72086/prodLMS/files/cloud/%D0%98%D0%BD%D1%81%D1%82%D1%80%D1%83%D0%BA%D1%86%D0%B8%D1%8F._OAuth_Authorization_code_flow.pdf">OAuth Authorization Code Flow</a>
