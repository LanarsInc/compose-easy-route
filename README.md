# Compose Easy Route 📍
Jetpack Compose navigation made declarative

## Defining destinations
To mark a Composable as a destination, use the `@Destination` annotation:
```kotlin
@Destination(name = "foo-page")
@Composable
fun FooPage() {
    /*  */
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

## Nested graphs
By default, all your destinations will belong to root `NavigationGraph`. This `NavigationGraph` instance will be generated in an object called `NavGraphs`. So, you can access it via `NavGraphs.root` and you should pass it into `EasyRouteNavHost` call.

To define a nested graph, you need to create an annotation class annotated with `@NavGraph`. For example:
```kotlin
@NavGraph(route = "registration", startRoute = "name")
annotation class RegistrationNavGraph
```
The `@NavGraph` annotation takes two mandatory parameters:
- `route` - the route of the navigation graph
- `startRoute` - start route of the navigation graph
  Navigating to the graph via its route automatically navigates to the graph's start destination.

To make destinations part of this navigation graph, you need to annotate them with it:
```kotlin
@RegistrationNavGraph
@Destination(name = "foo-page")
@Composable
fun FooPage() {
    /*  */
}
```

By default, all nested graphs are children of the root navigation graph. To define a nested graph as a child of another nested graph, use `parent` parameter of `@NavGraph` annotatioon:
```kotlin
@NavGraph(
    route = "confirmation",
    startRoute = "start",
    parent = RegistrationNavGraph::class
)
annotation class ConfirmationNavGraph
```

### Scoping ViewModel to navigation graph
Sometimes its useful to scope ViewModel to a specific navigation graph. ComposeEasyRoute provides a way to do that by obtaining parent backstack entry in `Composable` through parameter of type `NavBackStackEntry`, annotated with `@ParentBackStackEntry`, and passing it to `viewModel()` function.
```kotlin
@LoginNavGraph
@Destination(name = "foo-page")
@Composable
fun FooPage(
    @ParentBackStackEntry parentBackStackEntry: NavBackStackEntry
) {
    val viewModel = viewModel<SharedViewModel>(parentBackStackEntry)
    // if using Hilt
    val hiltViewModel = hiltViewModel<SharedViewModel>(parentBackStackEntry)
    
    /*  */
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
    /*  */
}
```
`EasyRouteDeepLink` also has `action` and `mimeType` parameters. Read more about deep linking in [official documentation](https://developer.android.com/jetpack/compose/navigation#deeplinks).
