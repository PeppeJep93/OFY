package oneforyou.jep.oneforyou.RegalaCreditiUtente;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaSplash;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestUtenteOK {

    @Rule
    public ActivityTestRule<PaginaSplash> mActivityTestRule = new ActivityTestRule<>(PaginaSplash.class);

    String saldoinserito = "200";

    @Test
    public void regalaCose() {

        try {

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction editText = onView(
                allOf(withId(R.id.inserisciuser),
                        isDisplayed()));
        editText.perform(replaceText("staff"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.inseriscipass),
                        isDisplayed()));
        editText2.perform(replaceText("Ciao1"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.premiaccesso), withText("ACCEDI"),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.button2b),
                        isDisplayed()));
        button2.perform(click());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.listviewGestioneUtenti),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)))
                .atPosition(2);
        linearLayout.perform(scrollTo(), click());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.menocinquanta)));
        button3.perform(scrollTo(), click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.piucinquanta)));
        button4.perform(scrollTo(), click());

        ViewInteraction saldoutente = onView(
                allOf(withId(R.id.saldoutente),
                        isDisplayed()));
        saldoutente.perform(replaceText(saldoinserito), closeSoftKeyboard());

        ViewInteraction button5 = onView(
                allOf(withId(R.id.aggiornasaldo)));
        button5.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
