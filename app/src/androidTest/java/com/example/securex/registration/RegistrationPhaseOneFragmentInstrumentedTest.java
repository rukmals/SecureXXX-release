package com.example.securex.registration;


import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.securex.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RegistrationPhaseOneFragmentInstrumentedTest {

    private static final String TEST_EMAIL = "shashimal@gmail.com";
    private static final String TEST_USERNAME = "shashimal4";

    MainActivity mainActivity;
    RegistrationPhaseTwoFragment registrationPhaseTwoFragment;
    TestNavHostController navHostController;

    @Rule
    public ActivityTestRule<MainActivity> activityActivityScenario = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void PhaseOntoTwo(){
        navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.nav_graph);

        FragmentScenario<RegistrationPhaseOneFragment> registrationPhaseOneFragmentFragmentScenario = FragmentScenario.launchInContainer(RegistrationPhaseOneFragment.class);

        registrationPhaseOneFragmentFragmentScenario.onFragment(new FragmentScenario.FragmentAction<RegistrationPhaseOneFragment>() {
            @Override
            public void perform(@NonNull RegistrationPhaseOneFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(),navHostController);


            }
        });
        onView(withId(R.id.username)).perform(typeText(TEST_USERNAME),closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(TEST_EMAIL),closeSoftKeyboard());
        onView(withId(R.id.phase1next)).perform(click());
        List<NavBackStackEntry> backStack = navHostController.getBackStack();
        NavBackStackEntry currentDestination = backStack.get(backStack.size() - 1);
        assertEquals(currentDestination.getDestination().getId(),R.id.registrationPhaseTwoFragment);


    }


    @Test
    public void SelectColorAndSIzeTest(){

    }


}