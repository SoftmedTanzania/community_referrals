package com.softmed.uzazisalama.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
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

import com.softmed.uzazisalama.AncSmartRegisterActivity;
import com.softmed.uzazisalama.DataModels.PregnantMom;
import org.ei.opensrp.drishti.R;
import com.softmed.uzazisalama.Repository.MotherPersonObject;
import com.softmed.uzazisalama.pageradapter.ANCRegisterPagerAdapter;
import com.softmed.uzazisalama.util.DatesHelper;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.util.Calendar;

public class AncRegisterFormFragment extends android.support.v4.app.Fragment {

    private ViewPager viewPager;
    Animation animationFabShow, animationFabHide, animationFabHideSlow;
    FloatingActionButton fabDone;

    private ANCRegisterPagerAdapter pagerAdapter;
    private TabLayout tabs;

    private PregnantMom pregnantMom;
    private static MotherPersonObject motherData;
    private Gson gson = new Gson();
    private Calendar calendar = Calendar.getInstance();

    private JSONObject fieldOverides = new JSONObject();
    private static String recordId;
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
                    AncRegister1stFragment detailsFragment = (AncRegister1stFragment) pagerAdapter.getItem(0);

                  final  AncRegister2ndFragment indicatorsFragment = (AncRegister2ndFragment) pagerAdapter.getItem(1);
                  final  int[] riskIndicators = detailsFragment.getRiskIndicatorsFromDetails();
                    // update risk indicators
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            indicatorsFragment.updateRiskIndicators(
                                    riskIndicators[0],
                                    riskIndicators[1],
                                    riskIndicators[2],
                                    riskIndicators[3]);
                        }
                    }, 100);

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
                AncRegister1stFragment firstFragment = (AncRegister1stFragment) pagerAdapter.getItem(0);
                if (firstFragment.isFormSubmissionOk()) {
                    // collect mother details from the 1st page
                    pregnantMom = firstFragment.getPregnantMom();

                    //todo check for checkboxes on the 2nd page then submit form

                    SparseBooleanArray indicatorsMap = ((AncRegister2ndFragment) pagerAdapter.getItem(1))
                            .getIndicatorsMap();

//                    pregnantMom.setAbove20WeeksPregnant(indicatorsMap.get(R.id.checkboxAgeBelow20));
                    pregnantMom.setHas10YrsPassedSinceLastPreg(indicatorsMap.get(R.id.checkbox10YrsLastPreg));
                    pregnantMom.setHadStillBirth(indicatorsMap.get(R.id.checkboxBabyDeath));
//                    pregnantMom.setHas2orMoreBBA(indicatorsMap.get(R.id.checkbox2orMoreBBA));
                    pregnantMom.setHasHeartProblem(indicatorsMap.get(R.id.checkboxHeartProb));
                    pregnantMom.setHasDiabetes(indicatorsMap.get(R.id.checkboxDiabetes));
                    pregnantMom.setHasTB(indicatorsMap.get(R.id.checkboxTB));
//                    pregnantMom.setFourOrMorePreg(indicatorsMap.get(R.id.checkbox4orMorePregnancies));
                    pregnantMom.setFirstPregAbove35Yrs(indicatorsMap.get(R.id.checkbox1stPregAbove35Yrs));
//                    pregnantMom.setHeightBelow150(indicatorsMap.get(R.id.checkboxHeightBelow150));
                    pregnantMom.setCsDelivery(indicatorsMap.get(R.id.checkboxCSDelivery));
                    pregnantMom.setKilemaChaNyonga(indicatorsMap.get(R.id.checkboxKilemaChaNyonga));
                    pregnantMom.setBleedingOnDelivery(indicatorsMap.get(R.id.checkboxBleedingOnDelivery));
                    pregnantMom.setKondoKukwama(indicatorsMap.get(R.id.checkboxKondoKukwama));

                    // default values
                    pregnantMom.setDateLastVisited(0);
                    pregnantMom.setLastSmsToken("0");
                    pregnantMom.setChwComment("no comment");
                    pregnantMom.setReg_type("1");
                    pregnantMom.setIs_pnc("false");
                    pregnantMom.setIs_valid("true");

                    long lnmp = pregnantMom.getDateLNMP();
                    long firstVisit = DatesHelper.calculate1stVisitFromLNMP(lnmp);
                    long secondVisit = DatesHelper.calculate2ndVisitFromLNMP(lnmp);
                    long thirdVisit = DatesHelper.calculate3rdVisitFromLNMP(lnmp);
                    long fourthVisit = DatesHelper.calculate4thVisitFromLNMP(lnmp);
                    long today = calendar.getTimeInMillis();

                    pregnantMom.setAncAppointment1(false);
                    pregnantMom.setAncAppointment2(false);
                    pregnantMom.setAncAppointment3(false);
                    pregnantMom.setAncAppointment4(false);

                    if (today > fourthVisit)
                        // fourth appointment checked
                        pregnantMom.setAncAppointment4(true);
                    else if (today > thirdVisit)
                        // third appointment checked
                        pregnantMom.setAncAppointment3(true);
                    else if (today > secondVisit)
                        // second appointment checked
                        pregnantMom.setAncAppointment2(true);
                    else if (today > firstVisit)
                        // first appointment checked
                        pregnantMom.setAncAppointment1(true);

                    if (pregnantMom.isOnRisk()) {
                        // calculate early visit date
                        long earlyVisit = DatesHelper.calculateEarlyVisitFromLNMP(lnmp);
                        if (today > earlyVisit)
                            // early appointment checked
                            pregnantMom.setAncAppointmentEarly(true);
                    }

                    // convert to json
                    String gsonMom = gson.toJson(pregnantMom);
                    Log.d(TAG, "mom = " + gsonMom);

                    // todo start form submission
                    if(firstFragment.getRegistrationType().equals("2")){

                        Log.d(TAG,"am in kumaliiza registration");
                        String motherId = motherData.getId();
                        motherData.setDetails(new Gson().toJson(pregnantMom));
                        Log.d(TAG,"mother id "+ motherId);
                        motherData = new MotherPersonObject(motherId, null, pregnantMom);
                        Log.d(TAG,"motherdata to be updated"+ new Gson().toJson(motherData));
                        ((AncSmartRegisterActivity) getActivity()).updateFormSubmission(motherData, motherId);
                        ((AncSmartRegisterActivity) getActivity()).switchToBaseFragment(null);
                    }else {
                        Log.d(TAG,"am in for the first registration registration");
                        ((SecuredNativeSmartRegisterActivity) getActivity()).saveFormSubmission(gsonMom, recordId, formName, getFormFieldsOverrides());
                        ((AncSmartRegisterActivity) getActivity()).switchToBaseFragment(null);

                    }


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

    public String getRecordId(){
        return motherData.getId();
    }

    public void setMotherDetails(MotherPersonObject mother) {

        this.motherData = mother;

        Log.d("TAG","motherDATA ="+ new Gson().toJson(motherData));
        AncRegister1stFragment firstFragment = (AncRegister1stFragment) pagerAdapter.getItem(0);
        firstFragment.setMotherDetails(motherData);

    }
    public void setEmptyDetails() {
        AncRegister1stFragment firstFragment = (AncRegister1stFragment) pagerAdapter.getItem(0);
        firstFragment.setEmptyValues();
    }

    public String getRegistrationType(){

        AncRegister1stFragment firstFragment = (AncRegister1stFragment) pagerAdapter.getItem(0);
        return firstFragment.getRegistrationType();
    }

    public void reloadValues(){
        Log.d(TAG,"reloading values");
        pagerAdapter = new ANCRegisterPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }
}
