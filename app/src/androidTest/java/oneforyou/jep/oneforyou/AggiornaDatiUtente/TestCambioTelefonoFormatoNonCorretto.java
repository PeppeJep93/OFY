package oneforyou.jep.oneforyou.AggiornaDatiUtente;


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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaSplash;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
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
public class TestCambioTelefonoFormatoNonCorretto {

    @Rule
    public ActivityTestRule<PaginaSplash> mActivityTestRule = new ActivityTestRule<>(PaginaSplash.class);

    String mail = "";
    String tel = "A525-E.0898347?";

    @Test
    public void provaCambio() {

        try {

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction editText = onView(
                allOf(withId(R.id.inserisciuser),
                        childAtPosition(
                                allOf(withId(R.id.sfondodati),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                1),
                        isDisplayed()));
        editText.perform(replaceText("staff"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.inseriscipass),
                        childAtPosition(
                                allOf(withId(R.id.sfondodati),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                2),
                        isDisplayed()));
        editText2.perform(replaceText("Ciao1"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.premiaccesso), withText("ACCEDI"),
                        childAtPosition(
                                allOf(withId(R.id.sfondodati),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                3),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.button2b),
                        childAtPosition(
                                allOf(withId(R.id.button2a),
                                        childAtPosition(
                                                withId(R.id.button2),
                                                0)),
                                0),
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
                allOf(withId(R.id.modificadati), withText("MODIFICA DATI"),
                        childAtPosition(
                                allOf(withId(R.id.spaziopassword),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                11)),
                                3)));
        button3.perform(scrollTo(), click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.mailmodifica),
                        childAtPosition(
                                allOf(withId(R.id.spaziomail),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                9)),
                                2)));
        editText3.perform(scrollTo(), replaceText(mail), closeSoftKeyboard());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.telmodifica),
                        childAtPosition(
                                allOf(withId(R.id.spaziotelefono),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                10)),
                                2)));
        editText4.perform(scrollTo(), replaceText(tel), closeSoftKeyboard());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.confermamodifica), withText("CONFERMA MODIFICA"),
                        childAtPosition(
                                allOf(withId(R.id.spaziopassword),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                11)),
                                4)));
        button4.perform(scrollTo(), click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.telutente),
                        isDisplayed()));
        textView2.check(matches(withText(tel)));
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
