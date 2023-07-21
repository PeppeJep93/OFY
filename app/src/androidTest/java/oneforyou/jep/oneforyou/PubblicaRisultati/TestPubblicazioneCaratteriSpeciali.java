package oneforyou.jep.oneforyou.PubblicaRisultati;


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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestPubblicazioneCaratteriSpeciali {

    @Rule
    public ActivityTestRule<PaginaSplash> mActivityTestRule = new ActivityTestRule<>(PaginaSplash.class);

    @Test
    public void pubblica() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction user = onView(
                allOf(withId(R.id.inserisciuser),
                        isDisplayed()));


        user.perform(replaceText("staff"), closeSoftKeyboard());




        ViewInteraction password = onView(
                allOf(withId(R.id.inseriscipass),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        password.perform(replaceText("Ciao1"), closeSoftKeyboard());



        ViewInteraction accedi = onView(
                allOf(withId(R.id.premiaccesso), withText("ACCEDI"),
                        isDisplayed()));
        /*
        ...simula un click su di esso
         */
        accedi.perform(click());





        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        ViewInteraction partite = onView(
                allOf(withId(R.id.button1),
                        isDisplayed()));
        partite.perform(click());




        ViewInteraction pubblicaRis = onView(
                allOf(withId(R.id.button2b),
                        isDisplayed()));
        pubblicaRis.perform(click());




        ViewInteraction casa = onView(
                allOf(withId(R.id.GolCasa), withText("1"),
                        isDisplayed()));
        casa.perform(click());



        ViewInteraction modificaCasa = onView(
                allOf(withId(R.id.GolCasa), withText("1"),
                        isDisplayed()));
        modificaCasa.perform(replaceText("#?"));



        ViewInteraction ospite = onView(
                allOf(withId(R.id.Golospite), withText("0"),
                        isDisplayed()));
        ospite.perform(click());



        ViewInteraction modificaOspite = onView(
                allOf(withId(R.id.Golospite), withText("0"),
                        isDisplayed()));
        modificaOspite.perform(replaceText(".!"));



        ViewInteraction tastoPubblica = onView(
                allOf(withId(R.id.imageSpunta),
                        isDisplayed()));
        tastoPubblica.perform(click());

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
