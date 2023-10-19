package com.majelan.androidtechnicaltest.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MVIViewModel<STATE: MVIState, EVENT: MVIEvent, ACTION: MVIAction>(
   initialState: STATE
): ViewModel() {
   private val _stateFlow: MutableStateFlow<STATE> = MutableStateFlow(initialState)
   val stateFlow: StateFlow<STATE> = _stateFlow

   protected val state get() = _stateFlow.value

   private val _actionFlow: MutableSharedFlow<ACTION> = MutableSharedFlow()
   val actionFlow: SharedFlow<ACTION> = _actionFlow

   //abstract fun getInitialState(): STATE

   protected fun emit(action: ACTION) {
      viewModelScope.launch { _actionFlow.emit(action) }
   }

   protected fun updateState(block: (STATE) -> STATE) {
      _stateFlow.update(block)
   }

   abstract fun process(event: EVENT)
}