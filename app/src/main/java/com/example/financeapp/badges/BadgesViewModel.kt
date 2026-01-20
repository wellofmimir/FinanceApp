package com.example.financeapp.badges

import com.example.financeapp.repositories.BadgesRepository
import com.example.financeapp.database.Badge

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BadgesViewModel (
    private val repository: BadgesRepository
): ViewModel() {
    private val internUserBadges = MutableStateFlow<List<Badge>>(emptyList())
    val userBadges = internUserBadges.asStateFlow()

    fun insertUserBadge(badge: Badge) {
        repository.insertUserBadge(badge)
    }

    fun loadUserBadges() {
        internUserBadges.value = repository.loadUserBadges()
    }
}