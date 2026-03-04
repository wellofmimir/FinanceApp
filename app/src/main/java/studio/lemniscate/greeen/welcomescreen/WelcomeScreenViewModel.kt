package studio.lemniscate.greeen.welcomescreen

import androidx.lifecycle.ViewModel
import studio.lemniscate.greeen.database.Goal
import studio.lemniscate.greeen.repositories.GoalRepository
import studio.lemniscate.greeen.repositories.UserRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class WelcomeScreenViewModel (
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository
): ViewModel() {
    fun insertGoal(goal: Goal) {

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        var newGoal = Goal(
            goal.id,
            goal.goal,
            goal.amount,
            goal.saved,
            goal.idStatus,
            formattedDate,
            goal.tokenCount,
            ""
        )
        goalRepository.insertGoal(newGoal)

        val newestGoalId = goalRepository.getNewestGoalId()
        newGoal = Goal(
            newestGoalId,
            goal.goal,
            goal.amount,
            goal.saved,
            goal.idStatus,
            formattedDate,
            goal.tokenCount,
            ""
        )

        goalRepository.setCurrentGoal(newGoal)
        setFirstLaunchDone()
    }

    fun updateUser(user: String) {
        userRepository.updateUser(user)
    }

    fun setFirstLaunchDone() {
        userRepository.setFirstLaunchDone()
    }

    fun getFirstLaunchDone(): Boolean {
        return userRepository.getFirstLaunchDone()
    }

    fun setCurrency(currency: String) {
        userRepository.setCurrency(currency)
    }
}