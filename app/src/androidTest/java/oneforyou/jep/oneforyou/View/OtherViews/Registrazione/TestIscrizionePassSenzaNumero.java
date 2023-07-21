package oneforyou.jep.oneforyou.View.OtherViews.Registrazione;
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
public class TestIscrizionePassSenzaNumero {



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
        ViewInteraction registrati = onView(
                allOf(withId(R.id.registrati),
                        isDisplayed()));
        /*
        ...ci scrive dentro una stringa
         */
        registrati.perform(click());



         /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction user = onView(
                allOf(withId(R.id.dgtuser),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        user.perform(replaceText("Carlo"), closeSoftKeyboard());



        /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction occhio = onView(
                allOf(withId(R.id.occhio),
                        isDisplayed()));
         /*
        ...e simula un click su di esso
         */
        occhio.perform(click());



         /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction password1 = onView(
                allOf(withId(R.id.dgtpass1),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        password1.perform(replaceText("Favola"), closeSoftKeyboard());



         /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction password2 = onView(
                allOf(withId(R.id.dgtpass2),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        password2.perform(replaceText("Favola"), closeSoftKeyboard());
/*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction email = onView(
                allOf(withId(R.id.dgtemail),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        email.perform(replaceText("carloalessandri@live.it"), closeSoftKeyboard());



         /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction telefono = onView(
                allOf(withId(R.id.dgttelefono),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        telefono.perform(replaceText("3281718203"), closeSoftKeyboard());



         /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction nome = onView(
                allOf(withId(R.id.dgtnome),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        nome.perform(replaceText("Carlo"), closeSoftKeyboard());



         /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction cognome = onView(
                allOf(withId(R.id.dgtcognome),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        cognome.perform(replaceText("Alessandri"), closeSoftKeyboard());



//          /*
//        cerca una view con quello specifico ID e...
//         */
//        ViewInteraction donna = onView(
//                allOf(withId(R.id.femmina),
//                        isDisplayed()));
//         /*
//        ...ci scrive dentro una stringa
//         */
//        donna.perform(click());



        /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction uomo = onView(
                allOf(withId(R.id.maschio),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        uomo.perform(click());



          /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction luogonascita = onView(
                allOf(withId(R.id.dgtluogo),
                        isDisplayed()));
         /*
        ...ci scrive dentro una stringa
         */
        luogonascita.perform(replaceText("Palermo"), closeSoftKeyboard());



        /*
        qui verrà aperto il fragment di datepicker,
        che selezionerà in automatico come
        data di nascita il 4 Febbraio 2000
         */
        ViewInteraction datanascita = onView(
                allOf(withId(R.id.datanascita), withText("DATA NASCITA")));
        datanascita.perform(scrollTo(), click());

        ViewInteraction imageButton = onView(
                allOf(withClassName(is("android.widget.ImageButton")), withContentDescription("Mese precedente"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.DayPickerView")),
                                        childAtPosition(
                                                withClassName(is("com.android.internal.widget.DialogViewAnimator")),
                                                0)),
                                1)));
        imageButton.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withClassName(is("android.widget.TextView")), withText("2002"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        textView.perform(click());
        DataInteraction textView2 = onData(anything())
                .inAdapterView(allOf(withClassName(is("android.widget.YearPickerView")),
                        childAtPosition(
                                withClassName(is("com.android.internal.widget.DialogViewAnimator")),
                                1)))
                .atPosition(100);
        textView2.perform(scrollTo(), click());

        ViewInteraction imageButton2 = onView(
                allOf(withClassName(is("android.widget.ImageButton")), withContentDescription("Mese precedente"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.DayPickerView")),
                                        childAtPosition(
                                                withClassName(is("com.android.internal.widget.DialogViewAnimator")),
                                                0)),
                                1)));
        imageButton2.perform(scrollTo(), click());

        ViewInteraction button3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        button3.perform(scrollTo(), click());



        /*
        cerca una view con quello specifico ID e...
         */
        ViewInteraction button2 = onView(
                allOf(withId(R.id.post), withText("ISCRIVITI"),
                        childAtPosition(
                                allOf(withId(R.id.sfondodati),
                                        childAtPosition(
                                                withId(R.id.scrolling),
                                                0)),
                                22)));
        /*
        ...simula un click su di esso
         */
        button2.perform(scrollTo(), click());



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
                anyOf(withId(R.id.premiaccesso))
        ).check(matches(isDisplayed()));
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
