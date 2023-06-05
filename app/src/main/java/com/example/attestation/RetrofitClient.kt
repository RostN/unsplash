package com.example.attestation

import com.example.attestation.feed.searchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

var baseUrl = "https://api.unsplash.com"

var retrofit = Retrofit
    .Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(Api::class.java)

interface Api {
    //Список фотографий пользователя
    @GET("/users/{id}/photos")
    suspend fun loadUsersPhoto(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String,
        @Query("page") page: Int,
    ): List<LoadPhotoResponse>

    //Список фотографий в коллекции
    @GET("/collections/{id}/photos")
    suspend fun loadCollectionsDetails(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String,
        @Query("page") page: Int,
    ): List<LoadPhotoResponse>

    //Загрузка коллекций
    @GET("/collections")
    suspend fun loadCollections(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Query("page") page: Int,
    ): List<LoadCollections>

    //Загрузка фото
    @GET("/photos")
    suspend fun loadPhotos(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Query("page") page: Int,
    ): List<LoadPhotoResponse>

    //Поиск фото
    @GET("/search/photos")
    suspend fun searchPhotos(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Query("page") page: Int,
        @Query("query") query: String = searchRequest
    ): SearchResult

    //Детальная информация о фото
    @GET("/photos/{id}")
    suspend fun loadPhotoDetails(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String
    ): PhotoDetails

    //Лайк
    @POST("/photos/{id}/like")
    suspend fun loadPhotoDetailsLike(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String
    ): PhotoLike

    //Отменить лайк
    @DELETE("/photos/{id}/like")
    suspend fun loadPhotoDetailsUnlike(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String
    ): PhotoLike

    //Запрос на информацию о пользователе
    @GET("/me")
    suspend fun loadUserInfo(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi"
    ): UserInfoResponse
}

//Коллекции
data class LoadCollections(
    val id: String, //ИД коллекции
    val title: String, //Заголовок
    val description: String, //Описание
    val total_photos: Int, //Количество фотографий
    val private: Boolean, //Приватная
    val user: Author, //Пользователь
    val cover_photo: PhotoDetails, //Обложка
    val tags: List<TAGS>, //Тэги
)

//Результат поиска
data class SearchResult(
    val results: List<LoadPhotoResponse>
)

//поставить лайк на фото
data class PhotoLike(
    val photo: PhotoDetails
)

//Детальная информация о фото
data class PhotoDetails(
    val id: String,
    val downloads: Int,
    val likes: Int,
    val liked_by_user: Boolean,
    val exif: Exif,
    val location: Location,
    val tags: List<TAGS>,
    val urls: URLS,
    val links: Links,
    val user: Author
)

//Теги
data class TAGS(
    val title: String
)

//Адрес
data class Location(
    val city: String,
    val country: String,
    val position: Coordinates
)

//Координаты
data class Coordinates(
    val latitude: Float,
    val longitude: Float
)

//Данные о камере
data class Exif(
    val make: String,
    val model: String,
    val exposure_time: String,
    val aperture: String,
    val focal_length: String,
    val iso: Int
)

//Загрузка фото
data class LoadPhotoResponse(
    var id: String, //Ид
    val created_at: String? = null, //Дата создания
    val width: Int? = 0, //Ширина
    val height: Int? = 0, //Высота
    val alt_description: String? = null, //Описание
    val urls: URLS, // Ссылки изображений
    val links: Links? = null, //Ссылки
    val likes: Int? = 0, //Общее количество лайков
    val liked_by_user: Boolean? = false, //Лайк от пользователя
    val user: Author //Автор
)

//Ссылки на изображения
data class URLS(
    val raw: String? = null, //RAW формат
    val full: String? = null, // Полное изображение
    val regular: String? = null, // Среднее изображение
    val small: String, // Маленькое изображение
)

//Автор картинки
data class Author(
    val id: String? = null, //Ид автора
    val name: String, //Полное имя автора
    val username: String, //Никнейм автора
    val profile_image: UserPhoto, //Аватарка пользователя
    val bio: String //Информация о пользователе
)

//Аватарки пользователя
data class UserPhoto(
    val small: String, // аватарка
    val medium: String? = null,//Средняя
    val large: String? = null //Большая
)

//Ссылки, которые были ранее, демаю они бесполезные
data class Links(
    val self: String, //API запрос именно на эту фотку (только с доступом)
    val html: String, //Ссылка на сайт
    val download: String, //Ссылка для загрузки
    val download_location: String //Ссылка только с доступом, но формата image.unsplash.com
)

//Информация об аккаунте
data class UserInfoResponse(
    val id: String, //ИД пользователя
    val updated_at: String, //Последнее обновление
    val username: String, //никнейм пользователя
    val name: String, // Полное имя
    val first_name: String, //Имя пользователя
    val last_name: String, //Фамилия пользователя
    val twitter_username: String? = null, // Twitter пользователя
    val portfolio_url: String? = null, // Сайт или иная ссылка пользователя
    val bio: String? = null, //Инфо о пользователе или биография
    val location: String? = null, //От куда
    val profile_image: ProfileImage, //Ссылки на аватарки
    val instagram_username: String? = null, //Инстаграм
    val total_collections: Int, //Всего коллекций
    val total_likes: Int, //Количество лайкнуто
    val total_photos: Int, //Количество фоток
    val followers_count: Int, //Количество подписчеков
    val following_count: Int, //Количество подписок
    val downloads: Int, //Количество загрузок
    val email: String, //Электронка
    val photos: List<LoadPhotoResponse>
)

//Информация о пользователе - аватарки
data class ProfileImage(
    val small: String,
    val medium: String,
    val large: String
)