package ru.skillbranch.devintensive.repositories

import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.utils.DataGenerator

object ChatRepository {
    fun loadChat() : List<Chat> {
        return DataGenerator.generateChats(10)
    }
}