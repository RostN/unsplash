package com.example.attestation

import com.example.attestation.feed.searchRequest

class Repository {
    //Получение фотографий пользователя
    suspend fun getUsersPhoto(page: Int, id: String): List<LoadPhotoResponse> {
        println("Фото пользователя")
        return retrofit.loadUsersPhoto(id = id, page = page)
    }

    //Информация о моём аккаунте
    suspend fun getMyProfile(): UserInfoResponse {
        println("Загрузка профиля")
        return retrofit.loadUserInfo()
    }

    //Фото в коллекции
    suspend fun getPhotosCollection(page: Int, id: String): List<LoadPhotoResponse> {
        println("Загрузка фото коллекции")
        return retrofit.loadCollectionsDetails(id = id, page = page)
    }

    //Коллекции
    suspend fun getCollections(page: Int): List<LoadCollections> {
        println("Загрузка списка коллекций")
        return retrofit.loadCollections(page = page)
    }

    //Снять лайк
    suspend fun unsetLike(id: String): PhotoDetails {
        println("Снятие лайка")
        return retrofit.loadPhotoDetailsUnlike(id = id).photo
    }

    //Поставить лайк
    suspend fun setLike(id: String): PhotoDetails {
        println("Установка лайка")
        return retrofit.loadPhotoDetailsLike(id = id).photo
    }

    //Детальная информация по фото
    suspend fun getDetailsPhoto(id: String): PhotoDetails {
        println("Детальная информация о фото")
        return retrofit.loadPhotoDetails(id = id)
    }

    //Лента
    suspend fun getPhoto(page: Int): List<LoadPhotoResponse> {
        println("Загрузка списка фото в ленте или поиск")
        val answer = when (searchRequest) {
            "" -> retrofit.loadPhotos(page = page)
            else -> retrofit.searchPhotos(page = page).results
        }
        return answer
    }
}