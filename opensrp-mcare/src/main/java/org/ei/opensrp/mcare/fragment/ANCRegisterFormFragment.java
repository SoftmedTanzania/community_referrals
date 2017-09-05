package org.ei.opensrp.mcare.fragment;

import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;

import org.ei.opensrp.mcare.R;
import org.ei.opensrp.mcare.adapters.ANCRegisterPagerAdapter;
import org.ei.opensrp.mcare.datamodels.PregnantMom;

public class ANCRegisterFormFragment extends android.support.v4.app.Fragment {

    private ViewPager viewPager;
    Animation animationFabShow, animationFabHide, animationFabHideSlow;
    FloatingActionButton fabDone;

    private ANCRegisterPagerAdapter pagerAdapter;
    private TabLayout tabs;

    private PregnantMom pregnantMom;
    private Gson gson = new Gson();

    private static final String TAG = ANCRegisterFormFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.activity_ancregister_form, container, false);

        animationFabShow = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_fab_show_fast);
        animationFabHide = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_fab_hide_fast);
        animationFabHideSlow = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_fab_hide);


        fabDone = (FloatingActionButton) v.findViewById(R.id.fabDone);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        pagerAdapter = new ANCRegisterPagerAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == pagerAdapter.getCount() - 1) {
                    // show only on the last fragment
                    fabDone.startAnimation(animationFabShow);
                    fabDone.setEnabled(true);

                } else if (fabDone.isEnabled()) {
                    fabDone.startAnimation(animationFabHide);
                    fabDone.setEnabled(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabs = (TabLayout) v.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // tabs icons
        tabs.getTabAt(0).setIcon(R.drawable.ic_account_circle);
        tabs.getTabAt(1).setIcon(R.drawable.ic_message_bulleted);

        final int colorWhite = ContextCompat.getColor(getActivity().getApplicationContext(), android.R.color.white);
        final int colorPrimaryLight = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_light);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(colorPrimaryLight, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        if (fabDone.isEnabled() &&
                tabs.getSelectedTabPosition() != pagerAdapter.getCount() - 1) {
            // hide fab on starting activity
            fabDone.startAnimation(animationFabHideSlow);
            fabDone.setEnabled(false);
        }


        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo collect data from forms
                if (((ANCRegister1stFragment) pagerAdapter.getItem(0)).isFormSubmissionOk()) {
                    // collect mother details from the 1st page
                    pregnantMom = ((ANCRegister1stFragment) pagerAdapter.getItem(0)).getPregnantMom();

                    //todo check for checkboxes on the 2nd page then submit form

                    SparseBooleanArray indicatorsMap = ((ANCRegister2ndFragment) pagerAdapter.getItem(1))
                            .getIndicatorsMap();

                    pregnantMom.setAbove20WeeksPregnant(indicatorsMap.get(R.id.checkboxAgeBelow20));
                    pregnantMom.setHas10YrsPassedSinceLastPreg(indicatorsMap.get(R.id.checkbox10YrsLastPreg));
                    pregnantMom.setHasBabyDeath(indicatorsMap.get(R.id.checkboxBabyDeath));
                    pregnantMom.setHas2orMoreBBA(indicatorsMap.get(R.id.checkbox2orMoreBBA));
                    pregnantMom.setHasHeartProblem(indicatorsMap.get(R.id.checkboxHeartProb));
                    pregnantMom.setHasDiabetes(indicatorsMap.get(R.id.checkboxDiabetes));
                    pregnantMom.setHasTB(indicatorsMap.get(R.id.checkboxTB));

                    // convert to json
                    String gsonMom = gson.toJson(pregnantMom);
                    Log.d(TAG, "mom = " + gsonMom);

                    // todo start form submission

                }

            }
        });

        return v;
    }

}
