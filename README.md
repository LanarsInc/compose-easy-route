# Compose Easy Route 📍
Jetpack Compose navigation made declarative

- [Download](#download)
- [Defining destinations](#defining-destinations)
- [Navigation host](#navigation-host)
- [Navigation](#navigation)
- [Navigation arguments](#navigation-arguments)
- [Nested graphs](#nested-graphs)
- [Deep links](#deep-links)
- [Android Studio not indexing generated files](#android-studio-not-indexing-generated-files)

## Download
```gradle
repositories {
    ...
    maven("https://jitpack.io")
}
```
```gradle
dependencies {
    implementation("com.github.LanarsInc.compose-easy-route:compose-easy-route:{version}")
    implementation("com.github.LanarsInc.compose-easy-route:compose-easy-route-core:{version}")
    ksp("com.github.LanarsInc.compose-easy-route:compose-easy-route-generator:{version}")
}
```

## Defining destinations
To mark a Composable as a destination, use the `@Destination` annotation:
```kotlin
@Destination(name = "foo-page")
@Composable
fun FooPage() {
    /* */
}
```
`@Destination` annotation takes one mandatory parameter:
- `name` - name of the destination route

## Navigation host
`EasyRouteNavHost` listens to navigation commands and triggers `NavController`. It uses Compose Navigation's `NavHost` under the hood.
`EasyRouteNavHost` has three mandatory parameters:
- `navigationManager` - used for sending navigation commands to `EasyRouteNavHost`
- `navGraph` - generated `NavigationGraph` object from `NavGraphs` object
- `startDirection` - initial direction
```kotlin
val navigationManager = rememberNavigationManager()
EasyRouteNavHost(
    navigationManager = navigationManager,
    navGraph = NavGraphs.root,
    startDirection = FirstPageDestination()
)
```

## Navigation
ComposeEasyRoute navigation API is similar to original Compose Navigation's.
#### Navigate to a destination
```kotlin
navigationManager.navigate(FooPageDestination())
```
Navigating with arguments:
```kotlin
navigationManager.navigate(BarPageDestination(id = 42, name = "Name"))
```
#### Pop back stack
```kotlin
navigationManager.popBackStack()
```
Pop up to specific destination:
```kotlin
navigationManager.popBackStack(FooPageDestination)
```
#### Pop up to before navigating
```kotlin
navigationManager.navigate(FooPageDestination()) {
    popUpTo(BarPageDestination) { inclusive = true }
}
```
Pop the whole back stack
```kotlin
navigationManager.navigate(FooPageDestination()) {
    popUntilRoot { inclusive = true }
}
```

## Navigation arguments
To declare navigation arguments you can simply add them to the Composable function:
```kotlin
@Destination(name = "foo-page")
@Composable
fun FooPage(
    id: Int? = null, // <- this will be an optional navigation argument
    name: String // <- this will be a mandatory navigation argument
)
```
### Supported types
- `Int`
- `Float`
- `Long`
- `Boolean`
- `String`
- `Serializable`
- `Parcelable`
- `Enums`
- `IntArray`
- `FloatArray`
- `LongArray`
- `BooleanArray`
- `Array<String>`
- `Array<Serializable>`
- `Array<Parcelable>`

**NOTE:** ComposeEasyRoute supports passing `Serializable` and `Parcelable` objects, however this approach is not recommended by Google. The official [documentation](https://developer.android.com/guide/navigation/navigation-pass-data#supported_argument_types) says:
> Passing complex data structures over arguments is considered an anti-pattern. Each destination should be responsible for loading UI data based on the minimum necessary information, such as item IDs. This simplifies process recreation and avoids potential data inconsistencies.

So you should avoid it as much as you can.

## Nested graphs
By default, all your destinations will belong to root `NavigationGraph`. This `NavigationGraph` instance will be generated in an object called `NavGraphs`. So, you can access it via `NavGraphs.root` and you should pass it into `EasyRouteNavHost` call.

To define a nested graph, you need to create an annotation class annotated with `@NavGraph`. For example:
```kotlin
@NavGraph(route = "registration")
annotation class RegistrationNavGraph(
    val start: Boolean = false
)
```
Note that `start` parameter is mandatory. It is used to mark destinations as start. Exactly one destination within specific navigation graph must be marked as `start`.

The `@NavGraph` annotation takes one mandatory parameter:
- `route` - the route of the navigation graph

To make destinations part of this navigation graph, you need to annotate them with it:
```kotlin
@RegistrationNavGraph(start = true)
@Destination(name = "foo-page")
@Composable
fun FooPage() {
    /* */
}
```

By default, all nested graphs are children of the root navigation graph. To define a nested graph as a child of another nested graph, use `parent` parameter of `@NavGraph` annotatioon:
```kotlin
@NavGraph(
    route = "confirmation",
    parent = RegistrationNavGraph::class
)
annotation class ConfirmationNavGraph
```

### Multiple NavHosts
Sometimes you want to create another NavHost that is independent from main NavHost (e.g. when implementing bottom navigation or bottom sheet). For that you will need to define a navigation graph, and mark it as `independent`, so ComposeEasyRoute will know that this navigation graph is separate from main navigation graph. Also, for independent graphs we don't need the `start` parameter, because `startDirection` will be passed right into `EasyRouteNavHost`.

**NOTE:** Independent graphs can't be defined as children of other graphs.
```kotlin
@NavGraph(
    route = "bottom-navigation",
    independent = true
)
annotation class BottomNavigationGraph
```
```kotlin
@BottomNavigationNavGraph
@Destination("books")
@Composable
fun BooksScreen() {
  /* */
}
```
```kotlin
val navigationManager = remember { NavigationManager() }
EasyRouteNavHost(
  navigationManager = navigationManager,
  navGraph = NavGraphs.bottomNavigation,
  startDirection = BooksScreenDestination()
)
```

### Scoping ViewModel to navigation graph
Sometimes its useful to scope ViewModel to a specific navigation graph. ComposeEasyRoute provides a way to do that by obtaining parent backstack entry in `Composable` through parameter of type `NavBackStackEntry`, annotated with `@ParentBackStackEntry`, and passing it to `viewModel()` function.
```kotlin
@RegistrationNavGraph
@Destination(name = "foo-page")
@Composable
fun FooPage(
    @ParentBackStackEntry parentBackStackEntry: NavBackStackEntry
) {
    val viewModel = viewModel<SharedViewModel>(parentBackStackEntry)
    // if using Hilt
    val hiltViewModel = hiltViewModel<SharedViewModel>(parentBackStackEntry)
    /* */
}
```

## Deep links
You can define deep links to a destination like this:
```kotlin
@Destination(
    name = "foo-page",
    deepLinks = [
        EasyRouteDeepLink(uriPattern = "https://www.example.com/foo/{id}")
    ]
)
@Composable
fun FooPage(id: Int) {
    /* */
}
```
`EasyRouteDeepLink` also has `action` and `mimeType` parameters. Read more about deep linking in [official documentation](https://developer.android.com/jetpack/compose/navigation#deeplinks).

## Android Studio not indexing generated files
See KSP related [issue](https://github.com/google/ksp/issues/37).

Fix: put this code inside `android` block.
```gradle
applicationVariants.all {
    kotlin.sourceSets {
        getByName(name) {
            kotlin.srcDir("build/generated/ksp/$name/kotlin")
        }
    }
}
```

