package oneforyou.jep.oneforyou.AggiornaPartite;


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
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestAggiornaQuotaPareggioCarSpec {

    @Rule
    public ActivityTestRule<PaginaSplash> mActivityTestRule = new ActivityTestRule<>(PaginaSplash.class);

    @Test
    public void agg() {

        try {

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        ViewInteraction user = onView(
                allOf(withId(R.id.inserisciuser),
                        isDisplayed()));
        user.perform(replaceText("staff"), closeSoftKeyboard());




        ViewInteraction pass = onView(
                allOf(withId(R.id.inseriscipass),
                        isDisplayed()));
        pass.perform(replaceText("Ciao1"), closeSoftKeyboard());



        ViewInteraction entra = onView(
                allOf(withId(R.id.premiaccesso), withText("ACCEDI"),
                        isDisplayed()));
        entra.perform(click());



        ViewInteraction partite = onView(
                allOf(withId(R.id.button1a),
                        isDisplayed()));
        partite.perform(click());




        ViewInteraction aggiornaP = onView(
                allOf(withId(R.id.button1b),
                        isDisplayed()));
        aggiornaP.perform(click());




        ViewInteraction textView = onView(
                allOf(withId(R.id.textRelativeSinistro), withText("AGGIORNA"),
                        childAtPosition(
                                allOf(withId(R.id.primoRelative),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        textView.perform(click());




        ViewInteraction squadraC = onView(
                allOf(withId(R.id.squadracasa),
                        isDisplayed()));
        squadraC.perform(replaceText("Milan"),closeSoftKeyboard());



        ViewInteraction squadraO = onView(
                allOf(withId(R.id.squadraospite),
                        isDisplayed()));
        squadraO.perform(replaceText("Inter"),closeSoftKeyboard());



        ViewInteraction coloreC = onView(
                allOf(withId(R.id.colorecasa),
                        isDisplayed()));
        coloreC.perform(click());

        DataInteraction checkedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        checkedTextView.perform(click());




        ViewInteraction coloreO = onView(
                allOf(withId(R.id.coloreospite),
                        isDisplayed()));
        coloreO.perform(click());

        DataInteraction checkedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(7);
        checkedTextView2.perform(click());




        ViewInteraction quoC = onView(
                allOf(withId(R.id.quotacasa),
                        isDisplayed()));
        quoC.perform(replaceText("5"));




        ViewInteraction quoO = onView(
                allOf(withId(R.id.quotaospite),
                        isDisplayed()));
        quoO.perform(replaceText("6"),closeSoftKeyboard());



        ViewInteraction quoP = onView(
                allOf(withId(R.id.quotapareggio),
                        isDisplayed()));
        quoP.perform(replaceText("&%^"),closeSoftKeyboard());



        ViewInteraction oraP = onView(
                allOf(withId(R.id.orapartita),
                        isDisplayed()));
        oraP.perform(replaceText("15"),closeSoftKeyboard());




        ViewInteraction minutiP = onView(
                allOf(withId(R.id.minutipartita),
                        isDisplayed()));
        minutiP.perform(replaceText("00"),closeSoftKeyboard());






        ViewInteraction dataP = onView(
                allOf(withId(R.id.datapartita),
                        childAtPosition(
                                allOf(withId(R.id.orario),
                                        childAtPosition(
                                                withId(R.id.info),
                                                5)),
                                0),
                        isDisplayed()));
        dataP.perform(click());

        ViewInteraction okP = onView(
                allOf(withId(android.R.id.button1),
                        isDisplayed()));
        okP.perform(click());




        ViewInteraction Aggiorniamo = onView(
                allOf(withId(R.id.bottoneOK), withText("AGGIORNA INFORMAZIONI"),
                        isDisplayed()));
        Aggiorniamo.perform(click());




        try {

            Thread.sleep(3000);
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
