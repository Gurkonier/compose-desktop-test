import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.ParcelableContainer
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import ui.App
import ui.App2

@Composable
private fun rememberLifecycle(): Lifecycle {
    val lifecycle = remember { LifecycleRegistry() }

    DisposableEffect(Unit) {
        lifecycle.resume()
        onDispose { lifecycle.destroy() }
    }

    return lifecycle
}

private const val KEY_STATE = "STATE"

@Composable
private fun rememberStateKeeper(): StateKeeper {
    val saveableStateRegistry: SaveableStateRegistry? = LocalSaveableStateRegistry.current

    val dispatcher =
        remember {
            StateKeeperDispatcher(saveableStateRegistry?.consumeRestored(KEY_STATE) as ParcelableContainer?)
        }

    if (saveableStateRegistry != null) {
        DisposableEffect(Unit) {
            val entry = saveableStateRegistry.registerProvider(KEY_STATE, dispatcher::save)
            onDispose { entry.unregister() }
        }
    }

    return dispatcher
}

@Composable
private fun rememberComponentContext(): ComponentContext {
    val lifecycle = rememberLifecycle()
    val stateKeeper = rememberStateKeeper()

    return remember {
        DefaultComponentContext(
            lifecycle = lifecycle,
            stateKeeper = stateKeeper
        )
    }
}

@Composable
fun <C : Parcelable> rememberRouter(
    initialConfiguration: () -> C,
    configurationClass: kotlin.reflect.KClass<out C>
): Router<C, Any> {
    val context = rememberComponentContext()

    return remember {
        context.router(
            initialConfiguration = initialConfiguration,
            configurationClass = configurationClass
        ) { configuration, _ -> configuration }
    }
}

@Composable
inline fun <reified C : Parcelable> rememberRouter(
    noinline initialConfiguration: () -> C
): Router<C, Any> =
    rememberRouter(
        initialConfiguration = initialConfiguration,
        configurationClass = C::class
    )

fun main() = application {
    val router =
        rememberRouter<Configuration>(
            initialConfiguration = { Configuration.App }
        )
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        Children(routerState = router.state) { screen ->
            when (val configuration = screen.configuration) {
                is Configuration.App -> App(onForward = { router.push(Configuration.App2) })
                is Configuration.App2 -> App2(onBack = router::pop)
            }
        }
    }
}
