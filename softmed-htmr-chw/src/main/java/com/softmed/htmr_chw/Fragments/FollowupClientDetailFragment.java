package com.softmed.htmr_chw.Fragments;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.softmed.htmr_chw.Domain.ClientReferral;
import com.softmed.htmr_chw.R;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.cursoradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.domain.Client;
import org.ei.opensrp.domain.Followup;
import org.ei.opensrp.domain.Referral;
import org.ei.opensrp.domain.ReferralFeedback;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.repository.FollowupRepository;
import org.ei.opensrp.repository.ReferralFeedbackRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.softmed.htmr_chw.util.Utils.generateRandomUUIDString;
import static org.ei.opensrp.AllConstants.ENGLISH_LOCALE;

public class FollowupClientDetailFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private static final String CLIENT_FOLLOWUP = "item_id";
    private static final String TAG = FollowupClientDetailFragment.class.getSimpleName();
    private ClientReferral clientReferral;
    private CommonRepository commonRepository;
    private Cursor cursor;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private Gson gson = new Gson();
    private TextView name, age, veo, ward, mapCue, village, refererName, gender, phoneNumber, feedback, otherInformation, referedReason, helperName, referedDate, helperPhoneNumber;
    private MaterialSpinner spinnerReason;
    private Typeface robotoRegular, sansBold;
    private int reasonSelection = -1;
    private List<String> referralFeedbacksNames = new ArrayList<>();
    private Button save;
    private String preferredLocale;
    private MaterialEditText cbhsNumber;
    private boolean saveCBHS=false;

    public FollowupClientDetailFragment() {
    }

    public static FollowupClientDetailFragment newInstance(ClientReferral clientReferral) {
        FollowupClientDetailFragment fragment = new FollowupClientDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLIENT_FOLLOWUP, clientReferral);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientReferral = (ClientReferral) getArguments().getSerializable(CLIENT_FOLLOWUP);
        }
    }

    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.followup_client_details, container, false);

        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(org.ei.opensrp.Context.getInstance().applicationContext()));
        preferredLocale = allSharedPreferences.fetchLanguagePreference();
        setLanguage();

        Log.d(TAG, "CLient Referral ID = " + clientReferral.getReferral_id());

        setupviews(rootView);
        setDetails(clientReferral);

        return rootView;
    }

    @Override
    protected SecuredNativeSmartRegisterActivity.DefaultOptionsProvider getDefaultOptionsProvider() {
        return null;
    }

    @Override
    protected SecuredNativeSmartRegisterActivity.NavBarOptionsProvider getNavBarOptionsProvider() {
        return null;
    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }

    @Override
    protected void onInitialization() {

    }

    @Override
    protected void startRegistration() {

    }

    public String getFacilityName(String id) {

        commonRepository = context().commonrepository("facility");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility where id ='" + id + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        if (commonPersonObjectList.size() > 0) {
            return commonPersonObjectList.get(0).getColumnmaps().get("name");
        } else {
            return "";
        }
    }

    public List<ReferralFeedback> getReferralFeedbacks() {
        ReferralFeedbackRepository feedbackRepository = context().referralFeedbackRepository();


        List<ReferralFeedback> referralFeedbacks = feedbackRepository.findFeedbackByReferralType("1");
        referralFeedbacksNames.clear();
        for (ReferralFeedback referralFeedback : referralFeedbacks) {

            if (preferredLocale.equals(ENGLISH_LOCALE))
                referralFeedbacksNames.add(referralFeedback.getDesc());
            else {
                referralFeedbacksNames.add(referralFeedback.getDescSw());
            }


        }
        return referralFeedbacks;
    }

    private void setDetails(final ClientReferral clientReferral) {

        String reg_date = dateFormat.format(clientReferral.getDate_of_birth());
        Log.d(TAG, "Date of Birth : " + clientReferral.getDate_of_birth());
        String ageS = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date d = dateFormat.parse(reg_date);
            Calendar cal = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            cal.setTime(d);

            int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
            Integer ageInt = new Integer(age);
            ageS = ageInt.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((clientReferral.getGender()).equalsIgnoreCase(getResources().getString(R.string.female))) {
            gender.setText(getResources().getString(R.string.female));
        } else {
            gender.setText(getResources().getString(R.string.male));
        }
        age.setText(ageS + " years");
        name.setText(clientReferral.getFirst_name() + " " + clientReferral.getMiddle_name() + ", " + clientReferral.getSurname());
        phoneNumber.setText(clientReferral.getPhone_number());
        referedReason.setText(clientReferral.getReferral_reason());
        referedDate.setText(dateFormat.format(clientReferral.getReferral_date()));


        try {
            village.setText(clientReferral.getVillage());
            ward.setText(clientReferral.getWard());
            mapCue.setText(clientReferral.getKijitongoji());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            helperName.setText(clientReferral.getHelper_name());
            helperPhoneNumber.setText(clientReferral.getHelper_phone_number());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            feedback.setText(clientReferral.getReferral_feedback());
        } catch (Exception e) {
            e.printStackTrace();
        }


        getReferralFeedbacks();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, referralFeedbacksNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReason.setAdapter(adapter);

        spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerReason.setFloatingLabelText(getString(R.string.followup_qn_reasons_for_not_visiting_clinic));
                    reasonSelection = i;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerReason.getSelectedItemPosition() <= 0) {
                    Toast.makeText(getActivity(), getString(R.string.toast_message_select_reasons_for_missing_appointment), Toast.LENGTH_SHORT).show();

                } else if (clientReferral.getReferral_id() == null) {
                    Toast.makeText(getActivity(), "Referral ID is null", Toast.LENGTH_SHORT).show();
                } else {

                    if(saveCBHS && !cbhsNumber.getText().toString().equals("")){
                        Client client = context().clientRepository().find(clientReferral.getClient_id());
                        client.setCommunity_based_hiv_service(cbhsNumber.getText().toString());
                        context().clientRepository().update(client);
                    }

                    Referral referral = context().referralRepository().find(clientReferral.getReferral_id());
                    referral.setReferral_status("1");

                    //updating referral status
                    context().referralRepository().update(referral);

                    //Saving referral followup
                    Followup followup = new Followup();

                    String uuid = generateRandomUUIDString();
                    followup.setId(uuid);
                    followup.setRelationalid(uuid);
                    followup.setClient_id(Long.parseLong(clientReferral.getClient_id()));
                    followup.setReferral_id(Long.parseLong(clientReferral.getReferral_id()));
                    followup.setReferral_feedback_id(reasonSelection);

                    if (!feedback.getText().toString().equals(""))
                        followup.setOther_notes(feedback.getText().toString());
                    else
                        followup.setOther_notes("N/A");

                    Log.d(TAG, "client Referral ID  = " + clientReferral.getReferral_id());

                    FollowupRepository followupRepository = context().followupRepository();
                    followupRepository.add(followup);

                    Followup savedFollowup = followupRepository.find(followup.getId());


                    List<FormField> formFields = new ArrayList<>();


                    formFields.add(new FormField("id", savedFollowup.getId(), FollowupRepository.TABLE_NAME + "." + "id"));
                    formFields.add(new FormField("relationalid", savedFollowup.getId(), FollowupRepository.TABLE_NAME + "." + "relationalid"));

                    Map<String, String> details = new Gson().<Map<String, String>>fromJson(savedFollowup.getDetails(), new TypeToken<Map<String, String>>() {
                    }.getType());

                    for (String key : details.keySet()) {
                        Log.d(TAG, "key = " + key);
                        FormField f = null;
                        f = new FormField(key, details.get(key), FollowupRepository.TABLE_NAME + "." + key);
                        formFields.add(f);
                    }


                    Log.d(TAG, "form field = " + new Gson().toJson(formFields));

                    FormData formData = new FormData("followup", "/model/instance/followup_form/", formFields, null);
                    FormInstance formInstance = new FormInstance(formData, "1");
                    FormSubmission submission = new FormSubmission(generateRandomUUIDString(), uuid, "followup_form", new Gson().toJson(formInstance), "4", SyncStatus.PENDING, "4");
                    context().formDataRepository().saveFormSubmission(submission);

                    Log.d(TAG, "submission content = " + new Gson().toJson(submission));

                    Toast.makeText(getActivity(), getString(R.string.followup_thankyou_note_part_one) + clientReferral.getFirst_name() + " " + clientReferral.getSurname(), Toast.LENGTH_SHORT).show();

                    ((FollowupReferralsFragment)getActivity().getSupportFragmentManager().findFragmentByTag("followup_fragment")).populateData();

                    ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().remove(FollowupClientDetailFragment.this)
                            .commit();


                }

            }
        });

    }

    private void setupviews(View rootView) {
        TextView heading = rootView.findViewById(R.id.heading);
        TextView referralDateTitle = rootView.findViewById(R.id.referral_date_title);
        TextView clientAgeTitle = rootView.findViewById(R.id.client_age_title);
        TextView veoNameTitle = rootView.findViewById(R.id.veo_name_title);
        TextView clientWardTitle = rootView.findViewById(R.id.client_ward_title);
        TextView clientVillageTitle = rootView.findViewById(R.id.client_village_title);
        TextView refererNameTitle = rootView.findViewById(R.id.referer_name_title);
        TextView client_kitongoji_title = rootView.findViewById(R.id.client_kitongoji_title);
        TextView refererTitle = rootView.findViewById(R.id.referer_title);
        TextView referralReasonTitle = rootView.findViewById(R.id.sababu_ya_rufaa_title);
        TextView clinicalInformationTitle = rootView.findViewById(R.id.clinical_information_title);
        TextView feedbackTitle = rootView.findViewById(R.id.feedback_title);
        TextView helperPhoneNumberTitle = rootView.findViewById(R.id.helper_phone_number_title);
        TextView helperNameTitle = rootView.findViewById(R.id.helper_name_title);
        TextView service_offered_title = rootView.findViewById(R.id.service_offered_title);
        TextView service_advice_title = rootView.findViewById(R.id.service_advice_title);
        LinearLayout cbhs = rootView.findViewById(R.id.cbhs);

        Log.d(TAG,"CBHS Number = "+clientReferral.getCommunity_based_hiv_service());

        Client client = context().clientRepository().find(clientReferral.getClient_id());

        if(!client.getCommunity_based_hiv_service().equals(""))
            cbhs.setVisibility(View.GONE);

        TextView cbhsTitle = rootView.findViewById(R.id.cbhs_title);
        TextView prefix = rootView.findViewById(R.id.prefix);
        Switch cbhsSwitch = rootView.findViewById(R.id.cbhs_switch);
        final LinearLayout cbhsLayout = rootView.findViewById(R.id.cbhs_layout);
        cbhsNumber = rootView.findViewById(R.id.cbhs_number);

        prefix.setText(context().allSharedPreferences().fetchCBHS() + "/");

        cbhsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveCBHS = b;
                if(b){
                    cbhsLayout.setVisibility(View.VISIBLE);
                }else{
                    cbhsLayout.setVisibility(View.GONE);
                }
            }
        });





        save = (Button) rootView.findViewById(R.id.save_button);
        name = rootView.findViewById(R.id.client_name);
        phoneNumber = (TextView) rootView.findViewById(R.id.phone_number);
        village = (TextView) rootView.findViewById(R.id.client_village_value);
        age = (TextView) rootView.findViewById(R.id.client_age_value);
        veo = (TextView) rootView.findViewById(R.id.veo_name_value);
        ward = (TextView) rootView.findViewById(R.id.client_ward_value);
        refererName = (TextView) rootView.findViewById(R.id.referer_name_value);
        mapCue = (TextView) rootView.findViewById(R.id.client_kitongoji_value);
        helperName = (TextView) rootView.findViewById(R.id.helper_name_value);
        helperPhoneNumber = (TextView) rootView.findViewById(R.id.helper_phone_number_value);
        gender = (TextView) rootView.findViewById(R.id.gender);
        otherInformation = (TextView) rootView.findViewById(R.id.other_clinical_inforamtion_value);
        referedDate = (TextView) rootView.findViewById(R.id.referral_date);
        referedReason = (TextView) rootView.findViewById(R.id.followUp_reason);
        feedback = (TextView) rootView.findViewById(R.id.client_condition);
        spinnerReason = (MaterialSpinner) rootView.findViewById(R.id.spinnerClientAvailable);

        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "roboto_regular.ttf");
        sansBold = Typeface.createFromAsset(getActivity().getAssets(), "google_sans_bold.ttf");

        heading.setTypeface(sansBold);
        cbhsTitle.setTypeface(sansBold);
        refererTitle.setTypeface(sansBold);
        helperPhoneNumberTitle.setTypeface(sansBold);
        helperNameTitle.setTypeface(sansBold);
        clientVillageTitle.setTypeface(sansBold);
        feedbackTitle.setTypeface(sansBold);
        referralReasonTitle.setTypeface(sansBold);
        clinicalInformationTitle.setTypeface(sansBold);
        referralDateTitle.setTypeface(sansBold);
        client_kitongoji_title.setTypeface(sansBold);
        refererNameTitle.setTypeface(sansBold);
        clientAgeTitle.setTypeface(sansBold);
        veoNameTitle.setTypeface(sansBold);
        clientWardTitle.setTypeface(sansBold);
        name.setTypeface(sansBold);
        referedDate.setTypeface(sansBold);

        prefix.setTypeface(robotoRegular);
        service_offered_title.setTypeface(robotoRegular);
        service_advice_title.setTypeface(robotoRegular);
        age.setTypeface(robotoRegular);
        veo.setTypeface(robotoRegular);
        ward.setTypeface(robotoRegular);
        village.setTypeface(robotoRegular);
        refererName.setTypeface(robotoRegular);
        mapCue.setTypeface(robotoRegular);
        referedReason.setTypeface(robotoRegular);
        otherInformation.setTypeface(robotoRegular);
    }

    private void setLanguage() {
        Log.d(TAG, "set Locale : " + preferredLocale);

        Resources res = org.ei.opensrp.Context.getInstance().applicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(preferredLocale);
        res.updateConfiguration(conf, dm);

    }
}
