import com.arkivanov.essenty.parcelable.Parcelable

sealed class Configuration: Parcelable{
    object App: Configuration()
    object App2: Configuration()
}
