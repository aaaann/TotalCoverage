package otus.demo.totalcoverage

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher

open class ViewObject(protected val viewInteraction: ViewInteraction) {

    companion object {

        fun objectWithId(@IdRes id: Int, block: ViewObject.() -> Unit): ViewObject {
            val interaction = onView(withId(id))
            return ViewObject(interaction).apply(block)
        }
    }

    fun click() {
        viewInteraction.perform(ViewActions.click())
    }

    fun isDisplayed() {
        viewInteraction.check(matches(ViewMatchers.isDisplayed()))
    }
}

class TextObject(viewInteraction: ViewInteraction) : ViewObject(viewInteraction) {
    companion object {

        fun textObjectWithId(@IdRes id: Int, block: TextObject.() -> Unit): TextObject {
            val interaction = onView(withId(id))
            return TextObject(interaction).apply(block)
        }

        fun objectWithText(text: String, block: TextObject.() -> Unit): TextObject {
            val interaction = onView(withText(text))
            return TextObject(interaction).apply(block)
        }
    }

    fun isTextTheSame(text: String) {
        viewInteraction.check(matches(withText(text)))
    }
}

class EditTextObject(viewInteraction: ViewInteraction) : ViewObject(viewInteraction) {

    companion object {
        fun editTextObjectWithId(@IdRes id: Int, block: EditTextObject.() -> Unit): EditTextObject {
            val interaction = onView(withId(id))
            return EditTextObject(interaction).apply(block)
        }
    }

    fun typeText(text: String) {
        viewInteraction.perform(ViewActions.typeText(text))
    }
}

class RecyclerObject(viewInteraction: ViewInteraction) : ViewObject(viewInteraction) {
    companion object {
        fun recyclerWithId(@IdRes id: Int, block: RecyclerObject.() -> Unit): RecyclerObject {
            val interaction = onView(withId(id))
            return RecyclerObject(interaction).apply(block)
        }
    }

    fun textAtPosition(position: Int, text: String) {
        viewInteraction.check(
            matches(
                RecyclerAtPositionMatcher(
                    position,
                    ViewMatchers.hasDescendant(withText(text))
                )
            )
        )
    }

    fun clickAtPosition(position: Int) {
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                ViewActions.click()
            )
        )
    }
}

class RecyclerAtPositionMatcher(
    private val position: Int,
    private val matcher: Matcher<View>
) : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
    override fun describeTo(description: Description?) {
        println("Not matches")
    }

    override fun matchesSafely(item: RecyclerView): Boolean {
        return matcher.matches(item.findViewHolderForAdapterPosition(position)?.itemView)
    }
}