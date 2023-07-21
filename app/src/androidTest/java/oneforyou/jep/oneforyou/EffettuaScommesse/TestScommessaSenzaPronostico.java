package oneforyou.jep.oneforyou.EffettuaScommesse;


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
public class TestScommessaSenzaPronostico {

    @Rule
    public ActivityTestRule<PaginaSplash> mActivityTestRule = new ActivityTestRule<>(PaginaSplash.class);

    @Test
    public void effettuaScommesse() {

        /*se iniziamo il testing partendo dalla pagina
        Splash, è fondamentale questo try-catch
        che ritarda la simulazione di 3 secondi,
        cioè il tempo previsto per la pagina di benvenuto
        di passare alla pagina di accesso
         */
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction username = onView(
                allOf(withId(R.id.inserisciuser),
                        isDisplayed()));
        /*
        ...ci scrive dentro una stringa
         */
        username.perform(replaceText("Gianni"), closeSoftKeyboard());



         /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction password = onView(
                allOf(withId(R.id.inseriscipass),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        password.perform(replaceText("Ciao1"), closeSoftKeyboard());



        /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction accedi = onView(
                allOf(withId(R.id.premiaccesso), withText("ACCEDI"),
                        isDisplayed()));
        /*
        ...simula un click su di esso
         */
        accedi.perform(click());



        ViewInteraction button2 = onView(
                allOf(withId(R.id.butpa),
                        childAtPosition(
                                allOf(withId(R.id.gioca0),
                                        childAtPosition(
                                                withId(R.id.gioca),
                                                0)),
                                0),
                        isDisplayed()));
        button2.perform(click());

        DataInteraction relativeLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.listviewPartite),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                1)))
                .atPosition(0);
        relativeLayout.perform(scrollTo(), click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.piuuno), withText("+1"),
                        childAtPosition(
                                allOf(withId(R.id.agguno),
                                        childAtPosition(
                                                withId(R.id.aggiungere),
                                                0)),
                                0),
                        isDisplayed()));
        button4.perform(click());

        ViewInteraction button6 = onView(
                allOf(withId(R.id.scommetti), withText("VINCI 10 funnies!"),
                        childAtPosition(
                                allOf(withId(R.id.tastierino),
                                        childAtPosition(
                                                withId(R.id.quote),
                                                1)),
                                3)));
        button6.perform(scrollTo(), click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.txtAttive), withText("attive"),
                        childAtPosition(
                                allOf(withId(R.id.infoAttive),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                3)),
                                0),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));
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
