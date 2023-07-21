package oneforyou.jep.oneforyou.View.OtherViews.Login;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaSplash;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AnyOf.anyOf;



@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestAccessoUserCortaPassPiena {



    @Rule
    public ActivityTestRule<PaginaSplash> mActivityTestRule = new ActivityTestRule<>(PaginaSplash.class);



    @Test
    public void testAccesso() {



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
        username.perform(replaceText("Rosa"), closeSoftKeyboard());



         /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction password = onView(
                allOf(withId(R.id.inseriscipass),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        password.perform(replaceText("Pizza80"), closeSoftKeyboard());



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


        /*qui decido di aspettare un po' di tempo
        per permettere all'app di mostrare un toast
        con messaggio l'eventuale errore riscontrato
         */
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        /*
        gli diamo un riferimento ad una view
        presente nella pagina successiva e
        verifichiamo che esso esista:
        se esiste, allora l'operazione è
        andata a buon fine, altrimenti no
         */
        onView(
                anyOf(withId(R.id.username))
        ).check(matches(isDisplayed()));
    }





    /*private static Matcher<View> childAtPosition(
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
    }*/
}