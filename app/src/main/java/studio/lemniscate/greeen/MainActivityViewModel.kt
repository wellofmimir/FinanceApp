package studio.lemniscate.greeen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import studio.lemniscate.greeen.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue


class MainActivityViewModel(private val repository: UserRepository): ViewModel() {
    private var internUser = MutableStateFlow<String>("")
    var user = internUser.asStateFlow()

    fun setCurrentTheme(theme: String) {
        repository.setCurrentTheme(theme)
    }

    fun getCurrentTheme(): String {
        return repository.getCurrentTheme()
    }

    fun setHomeScreenTutorialDone() {
        repository.setHomeScreenTutorialDone()
    }

    fun getHomeScreenTutorialDone(): Boolean {
        return repository.getHomeScreenTutorialDone()
    }

    fun loadUser() {
        internUser.value = repository.getUser()
    }

    fun getReceiptsTutorialDone(): Boolean {
        return repository.getReceiptsTutorialDone()
    }

    fun setReceiptsTutorialDone() {
        repository.setReceiptsTutorialDone()
    }

    fun getGoalHistoryTutorialDone(): Boolean {
        return repository.getGoalHistoryTutorialDone()
    }

    fun setGoalHistoryTutorialDone() {
        repository.setGoalHistoryTutorialDone()
    }
}