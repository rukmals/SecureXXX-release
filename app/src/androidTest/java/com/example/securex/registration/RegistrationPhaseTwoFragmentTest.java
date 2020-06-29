package com.example.securex.registration;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import com.example.securex.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class RegistrationPhaseTwoFragmentTest {

    MainActivity mainActivity;
    RegistrationPhaseTwoFragment registrationPhaseTwoFragment;


    @Rule
    public ActivityTestRule<MainActivity> activityActivityScenario = new ActivityTestRule<>(MainActivity.class);

    private Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void Before(){
        activityActivityScenario.getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.nav_graph);
        navHostController.setCurrentDestination(R.id.registrationPhaseTwoFragment);

        FragmentScenario<RegistrationPhaseTwoFragment> registrationPhaseOneFragmentFragmentScenario = FragmentScenario.launchInContainer(RegistrationPhaseTwoFragment.class);

        registrationPhaseOneFragmentFragmentScenario.onFragment(new FragmentScenario.FragmentAction<RegistrationPhaseTwoFragment>() {
            @Override
            public void perform(@NonNull RegistrationPhaseTwoFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(),navHostController);


            }
        });
    }


    @Test
    public void SelectColorAndSIzeTest(){
        onView(withId(R.id.phase1next)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("4"))).perform(click());
//        onView(withId(R.id.siezspinner)).check(matches(withSpinnerText(containsString("4"))));
    }
}