package com.example.mynotes.presentation.ui.dispatcher

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationDispatcher @Inject constructor() : AppNavigator, NavigationHandler {
    override val navigationBuffer = MutableSharedFlow<NavigationArgs>()

    private suspend fun navigator(args: NavigationArgs) {
        navigationBuffer.emit(args)
    }

    override suspend fun replaceTo(screen: AppScreen) = navigator {
        replace(screen)
    }

    override suspend fun back() = navigator {
        pop()
    }

    override suspend fun navigateTo(screen: AppScreen) = navigator {
        push(screen)
    }

    override suspend fun navigateTo(screens: List<AppScreen>) = navigator {
        push(screens)
    }

    override suspend fun replace(screen: AppScreen) = navigator {
        replace(screen)

    }

    override suspend fun replaceAll(screen: AppScreen) = navigator {
        replaceAll(screen)
    }

    override suspend fun <T : AppScreen> backUntil(clazz: Class<T>) = navigator {
        popUntil { it::class == clazz }
    }
}