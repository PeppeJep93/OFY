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
public class TestAggiornaOk {

    @Rule
    public ActivityTestRule<PaginaSplash> mActivityTestRule = new ActivityTestRule<>(PaginaSplash.class);

    @Test
    public void agg() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
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

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.button1a),
                        childAtPosition(
                                allOf(withId(R.id.button1),
                                        childAtPosition(
                                                withId(R.id.tableRow0),
                                                0)),
                                0),
                        isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.button1b),
                        childAtPosition(
                                allOf(withId(R.id.button1a),
                                        childAtPosition(
                                                withId(R.id.button1),
                                                0)),
                                0),
                        isDisplayed()));
        button2.perform(click());

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



        ViewInteraction editText4 = onView(
                allOf(withId(R.id.quotacasa),
                        isDisplayed()));
        editText4.perform(replaceText(""));




        ViewInteraction editText6 = onView(
                allOf(withId(R.id.quotaospite),
                        isDisplayed()));
        editText6.perform(replaceText(""),closeSoftKeyboard());





        ViewInteraction editText8 = onView(
                allOf(withId(R.id.quotapareggio),
                        isDisplayed()));
        editText8.perform(replaceText(""),closeSoftKeyboard());



        ViewInteraction button9 = onView(
                allOf(withId(R.id.datapartita),
                        childAtPosition(
                                allOf(withId(R.id.orario),
                                        childAtPosition(
                                                withId(R.id.info),
                                                5)),
                                0),
                        isDisplayed()));
        button9.perform(click());

        ViewInteraction button3 = onView(
                allOf(withId(android.R.id.button1),
                        isDisplayed()));
        button3.perform(click());

        ViewInteraction editText10 = onView(
                allOf(withId(R.id.orapartita),
                        childAtPosition(
                                allOf(withId(R.id.orario),
                                        childAtPosition(
                                                withId(R.id.info),
                                                5)),
                                1),
                        isDisplayed()));
        editText10.perform(replaceText(""),closeSoftKeyboard());



        ViewInteraction editText12 = onView(
                allOf(withId(R.id.minutipartita),
                        childAtPosition(
                                allOf(withId(R.id.orario),
                                        childAtPosition(
                                                withId(R.id.info),
                                                5)),
                                2),
                        isDisplayed()));
        editText12.perform(replaceText(""),closeSoftKeyboard());



        ViewInteraction editText14 = onView(
                allOf(withId(R.id.squadracasa),
                        isDisplayed()));
        editText14.perform(replaceText(""),closeSoftKeyboard());



        ViewInteraction editText16 = onView(
                allOf(withId(R.id.squadraospite),
                        isDisplayed()));
        editText16.perform(replaceText("i"),closeSoftKeyboard());




        ViewInteraction spinner = onView(
                allOf(withId(R.id.colorecasa),
                        isDisplayed()));
        spinner.perform(click());

        DataInteraction checkedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        checkedTextView.perform(click());



        ViewInteraction spinner2 = onView(
                allOf(withId(R.id.coloreospite),
                        isDisplayed()));
        spinner2.perform(click());

        DataInteraction checkedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(7);
        checkedTextView2.perform(click());



        ViewInteraction button4 = onView(
                allOf(withId(R.id.bottoneOK), withText("AGGIORNA INFORMAZIONI"),
                        isDisplayed()));
        button4.perform(click());

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
