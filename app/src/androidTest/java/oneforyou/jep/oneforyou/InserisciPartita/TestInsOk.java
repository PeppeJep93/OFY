package oneforyou.jep.oneforyou.InserisciPartita;


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
public class TestInsOk {

    @Rule
    public ActivityTestRule<PaginaSplash> mActivityTestRule = new ActivityTestRule<>(PaginaSplash.class);

    @Test
    public void testIns() {

        try {
            Thread.sleep(4000);
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
        editText.perform(click());



        ViewInteraction user = onView(
                allOf(withId(R.id.inserisciuser),
                        isDisplayed()));
        user.perform(replaceText("staff"), closeSoftKeyboard());


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        ViewInteraction pass = onView(
                allOf(withId(R.id.inseriscipass),
                        isDisplayed()));
        pass.perform(replaceText("Ciao2"), closeSoftKeyboard());



        ViewInteraction entra = onView(
                allOf(withId(R.id.premiaccesso), withText("ACCEDI"),
                        isDisplayed()));
        entra.perform(click());


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.button1),
                        childAtPosition(
                                allOf(withId(R.id.tableRow0),
                                        childAtPosition(
                                                withId(R.id.tableLayout1),
                                                0)),
                                0),
                        isDisplayed()));
        relativeLayout.perform(click());


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button2 = onView(
                allOf(withId(R.id.button3b),
                        childAtPosition(
                                allOf(withId(R.id.button3a),
                                        childAtPosition(
                                                withId(R.id.button3),
                                                0)),
                                0),
                        isDisplayed()));
        button2.perform(click());


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        ViewInteraction editText4 = onView(
                allOf(withId(R.id.squadracasa),
                        childAtPosition(
                                allOf(withId(R.id.info),
                                        childAtPosition(
                                                withId(R.id.prova),
                                                1)),
                                0),
                        isDisplayed()));
        editText4.perform(click());



        ViewInteraction squadraC = onView(
                allOf(withId(R.id.squadracasa),
                        isDisplayed()));
        squadraC.perform(replaceText("Bilbao"), closeSoftKeyboard());



        ViewInteraction squadraO = onView(
                allOf(withId(R.id.squadraospite),
                        isDisplayed()));
        squadraO.perform(replaceText("Napoli"), closeSoftKeyboard());




        ViewInteraction coloreC = onView(
                allOf(withId(R.id.colorecasa),
                        childAtPosition(
                                allOf(withId(R.id.info),
                                        childAtPosition(
                                                withId(R.id.prova),
                                                1)),
                                2),
                        isDisplayed()));
        coloreC.perform(click());

        DataInteraction checkedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(4);
        checkedTextView.perform(click());




        ViewInteraction coloreO = onView(
                allOf(withId(R.id.coloreospite),
                        childAtPosition(
                                allOf(withId(R.id.info),
                                        childAtPosition(
                                                withId(R.id.prova),
                                                1)),
                                3),
                        isDisplayed()));
        coloreO.perform(click());



        DataInteraction checkedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        checkedTextView2.perform(scrollTo(),click());




        ViewInteraction quoC = onView(
                allOf(withId(R.id.quotacasa),
                        isDisplayed()));
        quoC.perform(replaceText("2"), closeSoftKeyboard());



        ViewInteraction quoP = onView(
                allOf(withId(R.id.quotapareggio),
                        isDisplayed()));
        quoP.perform(replaceText("5"), closeSoftKeyboard());



        ViewInteraction quoO = onView(
                allOf(withId(R.id.quotaospite),
                        isDisplayed()));
        quoO.perform(replaceText("1"), closeSoftKeyboard());



        ViewInteraction data = onView(
                allOf(withId(R.id.datapartita), withText("7/2/2020"),
                        childAtPosition(
                                allOf(withId(R.id.orario),
                                        childAtPosition(
                                                withId(R.id.info),
                                                5)),
                                0),
                        isDisplayed()));
        data.perform(click());

        ViewInteraction okData = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        okData.perform(scrollTo(), click());



        ViewInteraction ora = onView(
                allOf(withId(R.id.orapartita),
                        isDisplayed()));
        ora.perform(replaceText("20"), closeSoftKeyboard());



        ViewInteraction minuti = onView(
                allOf(withId(R.id.minutipartita),
                        isDisplayed()));
        minuti.perform(replaceText("45"), closeSoftKeyboard());





        ViewInteraction editText12 = onView(
                allOf(withId(R.id.consiglio),
                        isDisplayed()));
        editText12.perform(replaceText("2"));





        ViewInteraction aggiungiamo = onView(
                allOf(withId(R.id.bottoneOK), withText("AGGIUNGI PARTITA"),
                        childAtPosition(
                                allOf(withId(R.id.info),
                                        childAtPosition(
                                                withId(R.id.prova),
                                                1)),
                                6),
                        isDisplayed()));
        aggiungiamo.perform(click());



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
