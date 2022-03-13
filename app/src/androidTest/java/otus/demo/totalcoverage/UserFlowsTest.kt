package otus.demo.totalcoverage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import otus.demo.totalcoverage.expenseslist.ExpensesViewHolder

@RunWith(AndroidJUnit4::class)
class UserFlowsTest {

    @get:Rule
    val activityTestRule = activityScenarioRule<ContainerActivity>()

    @Test
    fun shouldShowFabAndEmptyText() {
        val scenarioRule = activityTestRule.scenario

        onView(withId(R.id.empty_text))
            .check(
                matches(
                    allOf(
                        isDisplayed(),
                        withText("No expenses :)")
                    )
                )
            )

        onView(withId(R.id.add_expense_fab)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldAddItem() {
        onView(withId(R.id.add_expense_fab))
            .perform(click())

        onView(withId(R.id.title_edittext)).perform(
            ViewActions.typeText("Food")
        )
        onView(withId(R.id.amount_edittext)).perform(
            ViewActions.typeText("2000")
        )
        onView(withId(R.id.comment_edittext)).perform(
            ViewActions.typeText("It was good")
        )

        onView(withId(R.id.submit_button)).perform(click())

        onView(withId(R.id.expenses_recycler))
            .check(matches(atPosition(0, hasDescendant(withText("Food")))))
    }

    @Test
    fun shouldShowToastOnDoubleClick() {
        onView(withId(R.id.add_expense_fab))
            .perform(click())

        onView(withId(R.id.title_edittext)).perform(
            ViewActions.typeText("Food")
        )
        onView(withId(R.id.amount_edittext)).perform(
            ViewActions.typeText("2000")
        )
        onView(withId(R.id.comment_edittext)).perform(
            ViewActions.typeText("It was good")
        )

        onView(withId(R.id.submit_button)).perform(click())

        onView(withId(R.id.expenses_recycler))
            .perform(
                actionOnItemAtPosition<ExpensesViewHolder>(
                    0,
                    doubleClick()
                )
            )

        // check that toast was shown
    }
}

fun doubleClick() = DoubleClickAction()

class DoubleClickAction : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return allOf(isDisplayed(), isClickable())
    }

    override fun getDescription(): String {
        return "double clicked"
    }

    override fun perform(uiController: UiController?, view: View) {
        view.performClick()
        view.performClick()
    }
}

fun atPosition(position: Int, matcher: Matcher<View>) = RecyclerAtPositionMatcher(position, matcher)

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