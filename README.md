# Compose Easy Route 📍
Jetpack Compose navigation made declarative

## Defining destinations

## Navigation Host

## Navigation

## Nested graphs
By default, all your destinations will belong to root `NavigationGraph`. This `NavigationGraph` instance will be generated in an object called `NavGraphs`. So, you can access it via `NavGraphs.root` and you should pass it into `EasyRouteNavHost` call.

To define a nested graph, you need to create an annotation class annotated with `@NavGraph`. For example:
```kotlin
@NavGraph(route = "registration", startRoute = "name")
annotation class RegistrationNavGraph
```
The NavGraph annotation has two mandatory parameters:
- `route` - the route of the navigation graph
- `startRoute` - start route of the navigation graph
  Navigating to the graph via its route automatically navigates to the graph's start destination.

By default, all nested graphs are children of the root navigation graph. To define a nested graph as a child of another nested graph, use `parent` parameter of `NavGraph` annotatioon:
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
