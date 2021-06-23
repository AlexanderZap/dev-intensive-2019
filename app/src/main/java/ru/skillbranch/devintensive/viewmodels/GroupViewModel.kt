package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.*
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.repositories.GroupRepository

class GroupViewModel : ViewModel() {
    private var query = mutableLiveData("")
    private val groupRepository = GroupRepository
    private val userItems = mutableLiveData(loadUser())
    private val selectedItems = Transformations.map(userItems) { users -> users.filter { it.isSelected } }

    fun getUserData(): LiveData<List<UserItem>> {
        val result = MediatorLiveData<List<UserItem>>()

        val filterF = {
            val queryStr = query.value!!
            val users = userItems.value!!

            result.value =
                if (queryStr.isEmpty()) users else users.filter { it.fullName.contains(queryStr, true) }
        }

        result.addSource(userItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }

    fun getSelectedData(): LiveData<List<UserItem>> = selectedItems

    fun handlerSelectedItem(userId: String) {
        userItems.value = userItems.value!!.map {
            if (it.id == userId) it.copy(isSelected = !it.isSelected)
            else it
        }
    }

    fun handleRemoveChip(userId: String) {
        userItems.value = userItems.value!!.map {
            if (it.id == userId) it.copy(isSelected = false)
            else it
        }
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }

    fun handleCreateGroup() {
        groupRepository.createChat(selectedItems.value!!)
    }

    private fun loadUser(): List<UserItem> = groupRepository.loadUsers().map { it.toUserItem() }
}