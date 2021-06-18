package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard() {
    val focus = this.currentFocus
    focus?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(focus.windowToken, 0)
    }
}

fun Activity.getRootView(): View {
    return findViewById(android.R.id.content)
}

/*fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}*/

fun Activity.isKeyboardOpen(): Boolean {

    val rect = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(rect)

    val heightDiff: Int = this.getRootView().rootView.height - (rect.bottom - rect.top)
    return heightDiff > 100

/* val rect = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(rect)
    val screenHeight: Int = getRootView().height
    val keypadHeight: Int = screenHeight - rect.bottom

    return keypadHeight > screenHeight * 0.15*/

/*    val visibleBounds = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = this.convertDpToPx(50F).roundToInt()
    return heightDiff > marginOfError*/

}

fun Activity.isKeyboardClosed(): Boolean = !this.isKeyboardOpen()
