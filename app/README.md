# :app module

Responsible for creating the nav graph and orchestrating the navigation between the features.

### BottomBar / NavRail

The bottom bar is displayed when the windows's width is compact. Otherwise, the nav rail is
displayed.

### Navigation

Behavior of the navigation bars:

- Each navigation bar item has its own back stack.
- When clicking on a navigation bar item, the old navigation bar item's back stack is saved,
  removed, and the new navigation bar item's back stack is restored.
- Only the start destination ([vnListRoute]) is kept in the backstack at all time.
- When pressing back:
    - the current navigation bar item's back stack is popped
    - if the current navigation bar item's back stack is empty, it returns to the graph's start
      destination (even if the navigation bar item corresponding to the graph's start destination
      had other screens)
    - in this case, to restore the back stack of the navigation bar item corresponding to the
      graph's start destination, click again on the navigation bar item.
- When reselecting a navigation bar item:
    - the navigation bar item's back stack is cleared and it returns to the navigation bar item's
      start destination
    - if we already were at the navigation bar item's start destination, the screen is refreshed (
      removed and readded)
- The currently selected navigation bar item:
    - is always the most recent top level destination in the back stack
    - or the graph's start destination if none exists
- To navigate to a top level destination screen without switching the currently selected navigation
  bar item, please navigate using an alternative route to the screen. This way, depending on the
  route we use to navigate, we can always choose which navigation bar item is selected.