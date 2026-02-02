package studio.lemniscate.greeen.goalhistoryscreen

import androidx.lifecycle.ViewModel
import studio.lemniscate.greeen.repositories.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PunchCardSectionViewModel(private val goalRepository: GoalRepository) : ViewModel() {

    private val internTokenSoFar = MutableStateFlow(-1)
    val tokenSoFar = internTokenSoFar.asStateFlow()

    fun getTokenSoFarForPunchcard() {
         internTokenSoFar.value = goalRepository.getTokenSoFarForPunchcard()
    }

    fun resetTokenSoFarForPunchcard(spareToken: Int) {
        goalRepository.resetPunchcard()
        goalRepository.resetTokenSoFarForPunchcard()
        internTokenSoFar.value = spareToken
        goalRepository.addNewTokenAmountToPunchcard(spareToken)
    }
}