package com.vazdautsan.pocketinbox.core.common.mvi

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MviViewModelTest {

    private class CounterViewModel : MviViewModel<Int, String, String>(initialState = 0) {
        override fun onIntent(intent: String) {
            when (intent) {
                "inc" -> updateState { this + 1 }
                "report" -> sendEffect("count=$currentState")
            }
        }
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun intentsReduceState() = runTest {
        val viewModel = CounterViewModel()

        viewModel.state.test {
            assertEquals(0, awaitItem())
            viewModel.onIntent("inc")
            assertEquals(1, awaitItem())
        }
    }

    @Test
    fun effectSentBeforeCollectionIsDelivered() = runTest {
        val viewModel = CounterViewModel()
        viewModel.onIntent("inc")
        viewModel.onIntent("report")

        viewModel.effects.test {
            assertEquals("count=1", awaitItem())
        }
    }
}
