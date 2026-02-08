package studio.lemniscate.greeen.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import studio.lemniscate.greeen.database.Goal
import studio.lemniscate.greeen.repositories.GoalRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalsSectionViewModel(private val repository: GoalRepository) : ViewModel() {
    private val internGoals = MutableStateFlow<List<Goal>>(emptyList())
    val goals = internGoals.asStateFlow()

    private val internToastEvent = MutableSharedFlow<String>()
    val toastEvent = internToastEvent.asSharedFlow()

    fun showToast(message: String) {
        viewModelScope.launch {
            internToastEvent.emit(message)
        }
    }

    private val internCompletedGoals = MutableStateFlow<List<Goal>>(emptyList())
    val completedGoals = internCompletedGoals.asStateFlow()

    private val internPercentageOfCurrentGoal = MutableStateFlow<Float>(0f)
    val percentageOfCurrentGoal = internPercentageOfCurrentGoal.asStateFlow()

    private val internCurrentGoal = MutableStateFlow<Goal?>(null)
    val currentGoal = internCurrentGoal.asStateFlow()

    fun getExampleGoals() {
        val exampleGoals = listOf (
            Goal(1, "my awesome goal", 1100.0f, 0f, 2, "January 01, 2022", 5, ""),
            Goal(1, "Save $500 for a guitar", 1100.0f, 0f, 2, "October 29, 2024", 2, ""),
            Goal(1, "pay the Loch Ness Monster", 1101.0f, 0f, 2, "June 05, 2020", 4, "")
        )

        internCompletedGoals.value = exampleGoals
    }
    fun getCompletedGoals() {
        internCompletedGoals.value = repository.getCompletedGoals()
    }
    fun getCurrentGoalPercentage(): Int {
        return internPercentageOfCurrentGoal.value.toInt()
    }
    fun calculateCurrentGoalPercentage() {
        val goal = internCurrentGoal.value

        if (goal != null && goal.amount != 0.0f) {

            val zaehler = goal.saved
            val nenner = goal.amount
            val result = ((zaehler / nenner) * 100)

            internPercentageOfCurrentGoal.value = when {
                result >= 100.0f -> 100.0f
                result <= 0.0f -> 0.0f
                else -> result
            }

            if (internPercentageOfCurrentGoal.value >= 100.0f) {
                setGoalCompleted(goal)
            }
        }
    }
    fun getCurrentGoal() {
        val result = repository.getCurrentGoal()

        if (result == null) {
            reloadGoals()

            if (internGoals.value.isNotEmpty())
                internCurrentGoal.value = internGoals.value.first()
            else
                internCurrentGoal.value = null

            return
        }

        internCurrentGoal.value = result
    }
    fun updateGoal(goal: Goal) {
        repository.updateGoal(goal)
    }

    fun deleteGoal(id: Int) {
        repository.deleteGoal(id)
        showToast("Goal deleted.")
        reloadGoals()
        getCurrentGoal()
    }

    fun setGoalCompleted(goal: Goal) {
        repository.setGoalCompleted(goal)
    }

    fun updateImageToGoal(idGoal: Int, pathToImage: String) {
        repository.updateImageToGoal(idGoal, pathToImage)
        showToast("Picture of treat saved.")
    }

    fun addToTotalTokensEarned(amount: Int) {
        repository.addToTotalTokensEarned(amount)
    }

    fun insertGoal(goal: String, amount: Float, statusDescription: String, amountOfTokens: Int) {
        repository.insertGoal(goal, amount, 0f, statusDescription, amountOfTokens)
        getCompletedGoals()
    }

    fun getNewestGoalId(): Int {
        return repository.getNewestGoalId()
    }

    fun setCurrentGoal(goal: Goal) {
        repository.setCurrentGoal(goal)
        internCurrentGoal.value = goal
    }

    fun reloadGoals() {
        internGoals.value = repository.getInProgressGoals()
    }
}