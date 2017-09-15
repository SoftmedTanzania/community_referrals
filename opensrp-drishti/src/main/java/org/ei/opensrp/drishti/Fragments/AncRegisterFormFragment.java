package org.ei.opensrp.drishti.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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

import org.ei.opensrp.drishti.DataModels.PregnantMom;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.pageradapter.ANCRegisterPagerAdapter;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

public class AncRegisterFormFragment extends android.support.v4.app.Fragment {

    private ViewPager viewPager;
    Animation animationFabShow, animationFabHide, animationFabHideSlow;
    FloatingActionButton fabDone;

    private ANCRegisterPagerAdapter pagerAdapter;
    private TabLayout tabs;

    private PregnantMom pregnantMom;
    private Gson gson = new Gson();

    private JSONObject fieldOverides = new JSONObject();
    private String recordId;
    private String formName = "pregnant_mothers_registration";

    private static final String TAG = AncRegisterFormFragment.class.getSimpleName();

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

        viewPager.setOffscreenPageLimit(10);

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
                if (((AncRegister1stFragment) pagerAdapter.getItem(0)).isFormSubmissionOk()) {
                    // collect mother details from the 1st page
                    pregnantMom = ((AncRegister1stFragment) pagerAdapter.getItem(0)).getPregnantMom();

                    //todo check for checkboxes on the 2nd page then submit form

                    SparseBooleanArray indicatorsMap = ((AncRegister2ndFragment) pagerAdapter.getItem(1))
                            .getIndicatorsMap();

                    pregnantMom.setAbove20WeeksPregnant(indicatorsMap.get(R.id.checkboxAgeBelow20));
                    pregnantMom.setHas10YrsPassedSinceLastPreg(indicatorsMap.get(R.id.checkbox10YrsLastPreg));
                    pregnantMom.setHadStillBirth(indicatorsMap.get(R.id.checkboxBabyDeath));
                    pregnantMom.setHas2orMoreBBA(indicatorsMap.get(R.id.checkbox2orMoreBBA));
                    pregnantMom.setHasHeartProblem(indicatorsMap.get(R.id.checkboxHeartProb));
                    pregnantMom.setHasDiabetes(indicatorsMap.get(R.id.checkboxDiabetes));
                    pregnantMom.setHasTB(indicatorsMap.get(R.id.checkboxTB));
                    pregnantMom.setFourOrMorePreg(indicatorsMap.get(R.id.checkbox4orMorePregnancies));
                    pregnantMom.setFirstPregAbove35Yrs(indicatorsMap.get(R.id.checkbox1stPregAbove35Yrs));
                    pregnantMom.setHeightBelow150(indicatorsMap.get(R.id.checkboxHeightBelow150));
                    pregnantMom.setCsDelivery(indicatorsMap.get(R.id.checkboxCSDelivery));
                    pregnantMom.setKilemaChaNyonga(indicatorsMap.get(R.id.checkboxKilemaChaNyonga));
                    pregnantMom.setBleedingOnDelivery(indicatorsMap.get(R.id.checkboxBleedingOnDelivery));
                    pregnantMom.setKondoKukwama(indicatorsMap.get(R.id.checkboxKondoKukwama));

                    // convert to json
                    String gsonMom = gson.toJson(pregnantMom);
                    Log.d(TAG, "mom = " + gsonMom);

                    // todo start form submission

                    ((SecuredNativeSmartRegisterActivity) getActivity()).saveFormSubmission(gsonMom, recordId, formName, getFormFieldsOverrides());
                    getActivity().finish();
                }

            }
        });

        return v;
    }

    //TODO Implement this method to initialize a form data
    public void setFormData(String data) {
        Log.d(TAG, "Setting form data");
//        ((SecuredNativeSmartRegisterActivity) getActivity()).saveFormSubmission(data, recordId, formName, getFormFieldsOverrides());
    }

    public void savePartialFormData(String partialData) {
        ((SecuredNativeSmartRegisterActivity) getActivity()).savePartialFormData(partialData, recordId, formName, getFormFieldsOverrides());
    }

    public JSONObject getFormFieldsOverrides() {
        return fieldOverides;
    }

    public JSONObject getFieldOverides() {
        return fieldOverides;
    }

    public void setFieldOverides(String overrides) {
        try {
            //get the field overrides map
            if (overrides != null) {
                JSONObject json = new JSONObject(overrides);
                String overridesStr = json.getString("fieldOverrides");
                this.fieldOverides = new JSONObject(overridesStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
