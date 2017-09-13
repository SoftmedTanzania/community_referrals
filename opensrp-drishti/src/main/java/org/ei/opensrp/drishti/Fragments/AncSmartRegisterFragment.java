package org.ei.opensrp.drishti.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.Context;
import org.ei.opensrp.adapter.SmartRegisterPaginatedAdapter;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.commonregistry.CommonPersonObjectController;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.commonregistry.ControllerFilterMap;
import org.ei.opensrp.cursoradapter.CursorCommonObjectFilterOption;
import org.ei.opensrp.cursoradapter.CursorCommonObjectSort;
import org.ei.opensrp.cursoradapter.CursorFilterOption;
import org.ei.opensrp.cursoradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.cursoradapter.SmartRegisterPaginatedCursorAdapter;
import org.ei.opensrp.cursoradapter.SmartRegisterQueryBuilder;
import org.ei.opensrp.drishti.Anc.AncServiceModeOption;
import org.ei.opensrp.drishti.AncDetailActivity;
import org.ei.opensrp.drishti.AncFollowUpFormActivity;
import org.ei.opensrp.drishti.AncSmartRegisterActivity;
import org.ei.opensrp.drishti.LoginActivity;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.pageradapter.AncSmartClientsProvider;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.util.StringUtil;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.ei.opensrp.view.contract.ECClient;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.view.controller.VillageController;
import org.ei.opensrp.view.customControls.CustomFontTextView;
import org.ei.opensrp.view.dialog.AllClientsFilter;
import org.ei.opensrp.view.dialog.DialogOption;
import org.ei.opensrp.view.dialog.DialogOptionMapper;
import org.ei.opensrp.view.dialog.DialogOptionModel;
import org.ei.opensrp.view.dialog.EditOption;
import org.ei.opensrp.view.dialog.FilterOption;
import org.ei.opensrp.view.dialog.LocationSelectorDialogFragment;
import org.ei.opensrp.view.dialog.ServiceModeOption;
import org.ei.opensrp.view.dialog.SortOption;
import org.json.JSONObject;
import org.opensrp.api.domain.Location;
import org.opensrp.api.util.EntityUtils;
import org.opensrp.api.util.LocationTree;
import org.opensrp.api.util.TreeNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by koros on 11/2/15.
 */
public class AncSmartRegisterFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private static final String TAG = AncSmartRegisterFragment.class.getSimpleName();
    private final ClientActionHandler clientActionHandler = new ClientActionHandler();
    private SmartRegisterClientsProvider clientProvider = null;
    private CommonPersonObjectController controller;
    private VillageController villageController;
    private DialogOptionMapper dialogOptionMapper;
    private String locationDialogTAG = "locationDialogTAG";
    private JSONObject fieldOverides = new JSONObject();
    private String recordId;
    private String formName = "pregnant_mothers_registration";

    @Override
    protected SmartRegisterPaginatedAdapter adapter() {
        return new SmartRegisterPaginatedAdapter(clientsProvider());
    }

    @Override
    protected SecuredNativeSmartRegisterActivity.DefaultOptionsProvider getDefaultOptionsProvider() {
        return new SecuredNativeSmartRegisterActivity.DefaultOptionsProvider() {

            @Override
            public ServiceModeOption serviceMode() {
                return new AncServiceModeOption(clientsProvider());
            }

            @Override
            public FilterOption villageFilter() {
                return new AllClientsFilter();
            }

            @Override
            public SortOption sortOption() {
//                return new ElcoPSRFDueDateSort();
                return null;

            }

            @Override
            public String nameInShortFormForTitle() {
                return getResources().getString(R.string.mcare_ANC_register_title_in_short);
            }
        };
    }

    @Override
    protected SecuredNativeSmartRegisterActivity.NavBarOptionsProvider getNavBarOptionsProvider() {
        return new SecuredNativeSmartRegisterActivity.NavBarOptionsProvider() {

            @Override
            public DialogOption[] filterOptions() {
                ArrayList<DialogOption> dialogOptionslist = new ArrayList<DialogOption>();
                dialogOptionslist.add(new CursorCommonObjectFilterOption(getString(R.string.filter_by_all_label), ""));
                dialogOptionslist.add(new CursorCommonObjectFilterOption(getString(R.string.filter_by_anc1), filterStringForANCRV1()));
                dialogOptionslist.add(new CursorCommonObjectFilterOption(getString(R.string.filter_by_anc2), filterStringForANCRV2()));
                dialogOptionslist.add(new CursorCommonObjectFilterOption(getString(R.string.filter_by_anc3), filterStringForANCRV3()));
                dialogOptionslist.add(new CursorCommonObjectFilterOption(getString(R.string.filter_by_anc4), filterStringForANCRV4()));

                String locationjson = context().anmLocationController().get();
                LocationTree locationTree = EntityUtils.fromJson(locationjson, LocationTree.class);

                Map<String, TreeNode<String, Location>> locationMap =
                        locationTree.getLocationsHierarchy();
                addChildToList(dialogOptionslist, locationMap);
                DialogOption[] dialogOptions = new DialogOption[dialogOptionslist.size()];
                for (int i = 0; i < dialogOptionslist.size(); i++) {
                    dialogOptions[i] = dialogOptionslist.get(i);
                }

                return dialogOptions;
            }

            @Override
            public DialogOption[] serviceModeOptions() {
                return new DialogOption[]{};
            }

            @Override
            public DialogOption[] sortingOptions() {
                return new DialogOption[]{
//                        new ElcoPSRFDueDateSort(),
                        new CursorCommonObjectSort(getString(R.string.due_status), sortByAlertmethod()),
                        new CursorCommonObjectSort(Context.getInstance().applicationContext().getString(R.string.elco_alphabetical_sort), sortByFWWOMFNAME()),
                        new CursorCommonObjectSort(Context.getInstance().applicationContext().getString(R.string.hh_fwGobhhid_sort), sortByMOTHERS_ID()),
                        new CursorCommonObjectSort(Context.getInstance().applicationContext().getString(R.string.sortbyLmp), sortByLmp())

//                        new CommonObjectSort(true,false,true,"age")
                };
            }

            @Override
            public String searchHint() {
                return getString(R.string.str_ec_search_hint);
            }
        };
    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {

        return null;
    }


    @Override
    protected void onInitialization() {

//        context().formSubmissionRouter().getHandlerMap().put("psrf_form", new PSRFHandler());
    }

    @Override
    public void startRegistration() {
        Log.d(TAG, "starting registrations");
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag(locationDialogTAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        LocationSelectorDialogFragment
                .newInstance((AncSmartRegisterActivity) getActivity(), new EditDialogOptionModel(), context().anmLocationController().get(),
                        "pregnant_mothers_registration")
                .show(ft, locationDialogTAG);
    }

    @Override
    protected void onCreation() {
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        getDefaultOptionsProvider();
        if (isPausedOrRefreshList()) {
            initializeQueries();
        }
        updateSearchView();
        try {
            LoginActivity.setLanguage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        view.findViewById(R.id.btn_report_month).setVisibility(INVISIBLE);
        view.findViewById(R.id.service_mode_selection).setVisibility(INVISIBLE);

        ImageButton startregister = (ImageButton) view.findViewById(org.ei.opensrp.R.id.register_client);
        startregister.setVisibility(View.VISIBLE);
        clientsView.setVisibility(View.VISIBLE);
        clientsProgressView.setVisibility(View.INVISIBLE);
//        list.setBackgroundColor(Color.RED);
        initializeQueries();
        //processFormSubmission("");
    }

    private DialogOption[] getEditOptions() {
        return ((AncSmartRegisterActivity) getActivity()).getEditOptions();
    }

    private DialogOption[] getEditOptionsforanc(String ancvisittext, String ancvisitstatus) {
        return ((AncSmartRegisterActivity) getActivity()).getEditOptionsforanc(ancvisittext, ancvisitstatus);
    }

    public void updateSearchView() {
        getSearchView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(final CharSequence cs, int start, int before, int count) {

                if (cs.toString().equalsIgnoreCase("")) {
                    filters = "";
                } else {
                    //filters = "and MOTHERS_FIRST_NAME Like '%" + cs.toString() + "%' or GOBHHID Like '%" + cs.toString() + "%'  or JiVitAHHID Like '%" + cs.toString() + "%' ";
                    filters = cs.toString();
                }
                joinTable = "";
                mainCondition = "(Is_PNC is null or Is_PNC = '0') and MOTHERS_FIRST_NAME not null and MOTHERS_FIRST_NAME != \"\"   AND details  LIKE '%\"IS_VALID\":\"1\"%'";

                getSearchCancelView().setVisibility(isEmpty(cs) ? INVISIBLE : VISIBLE);
                CountExecute();
                filterandSortExecute();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void addChildToList(ArrayList<DialogOption> dialogOptionslist, Map<String, TreeNode<String, Location>> locationMap) {
        for (Map.Entry<String, TreeNode<String, Location>> entry : locationMap.entrySet()) {

            if (entry.getValue().getChildren() != null) {
                addChildToList(dialogOptionslist, entry.getValue().getChildren());

            } else {
                StringUtil.humanize(entry.getValue().getLabel());
                String name = StringUtil.humanize(entry.getValue().getLabel());
//                dialogOptionslist.add(new ElcoMauzaCommonObjectFilterOption(name, "location_name", name));

            }
        }
    }

    public String ancMainSelectWithJoins() {
        return "Select id as _id,relationalid,details,MOTHERS_FIRST_NAME,MOTHERS_LAST_NAME,MOTHERS_LAST_MENSTRUATION_DATE,MOTHERS_SORTVALUE,PNC_STATUS,MOTHERS_ID,Is_PNC,EXPECTED_DELIVERY_DATE \n" +
                "from wazazi_salama_mother\n";
    }

    public String ancMainCountWithJoins() {
        return "Select Count(*) \n" +
                "from wazazi_salama_mother\n";
    }

    public void initializeQueries() {
        AncSmartClientsProvider hhscp = new AncSmartClientsProvider(getActivity(),
                clientActionHandler, context().alertService());

        clientAdapter = new SmartRegisterPaginatedCursorAdapter(getActivity(), null, hhscp, new CommonRepository("wazazi_salama_mother", new String[]{"MOTHERS_FIRST_NAME", "MOTHERS_LAST_NAME", "MOTHERS_LAST_MENSTRUATION_DATE", "MOTHERS_SORTVALUE", "MOTHERS_ID", "Is_PNC", "EXPECTED_DELIVERY_DATE", "PNC_STATUS"}));
        clientsView.setAdapter(clientAdapter);

        setTablename("wazazi_salama_mother");
        SmartRegisterQueryBuilder countqueryBUilder = new SmartRegisterQueryBuilder(ancMainCountWithJoins());
        countSelect = countqueryBUilder.mainCondition("(wazazi_salama_mother.Is_PNC is null or wazazi_salama_mother.Is_PNC = '0') and wazazi_salama_mother.MOTHERS_FIRST_NAME not null and wazazi_salama_mother.MOTHERS_FIRST_NAME != \"\"   AND wazazi_salama_mother.details  LIKE '%\"IS_VALID\":\"1\"%'");
        mainCondition = "(Is_PNC is null or Is_PNC = '0') and MOTHERS_FIRST_NAME not null and MOTHERS_FIRST_NAME != \"\"   AND details  LIKE '%\"IS_VALID\":\"1\"%'";
        super.CountExecute();

        SmartRegisterQueryBuilder queryBUilder = new SmartRegisterQueryBuilder(ancMainSelectWithJoins());
        mainSelect = queryBUilder.mainCondition("(wazazi_salama_mother.Is_PNC is null or wazazi_salama_mother.Is_PNC = '0') and wazazi_salama_mother.MOTHERS_FIRST_NAME not null and wazazi_salama_mother.MOTHERS_FIRST_NAME != \"\"   AND wazazi_salama_mother.details  LIKE '%\"IS_VALID\":\"1\"%'");
        Sortqueries = sortBySortValue();

        currentlimit = 20;
        currentoffset = 0;

        super.filterandSortInInitializeQueries();

        updateSearchView();
        refresh();

    }

    private String sortBySortValue() {
        return " MOTHERS_SORTVALUE ASC";
    }

    private String sortByFWWOMFNAME() {
        return " MOTHERS_FIRST_NAME ASC";
    }


    private String sortByLmp() {
        return " MOTHERS_LAST_MENSTRUATION_DATE ASC";
    }

    private String filterStringForANCRV1() {
        return "ancrv_1";
    }

    private String filterStringForANCRV2() {
        return "ancrv_2";
    }

    private String filterStringForANCRV3() {
        return "ancrv_3";
    }

    private String filterStringForANCRV4() {
        return "ancrv_4";
    }

    private String sortByMOTHERS_ID() {
        return " MOTHERS_ID ASC";
    }

    private String sortByAlertmethod() {
        return " CASE WHEN Ante_Natal_Care_Reminder_Visit = 'urgent' and BirthNotificationPregnancyStatusFollowUp = 'urgent' THEN 1 "
                +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'upcoming' and BirthNotificationPregnancyStatusFollowUp = 'urgent' THEN  2\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'normal' and BirthNotificationPregnancyStatusFollowUp = 'urgent' THEN 3\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'expired' and BirthNotificationPregnancyStatusFollowUp = 'urgent' THEN 4\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit is null and BirthNotificationPregnancyStatusFollowUp = 'urgent' THEN 5\n" +

                "WHEN Ante_Natal_Care_Reminder_Visit = 'urgent' and BirthNotificationPregnancyStatusFollowUp = 'upcoming' THEN 6\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'upcoming' and BirthNotificationPregnancyStatusFollowUp = 'upcoming' THEN 7\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'normal' and BirthNotificationPregnancyStatusFollowUp = 'upcoming' THEN 8\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'expired' and BirthNotificationPregnancyStatusFollowUp = 'upcoming' THEN 9\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit is null and BirthNotificationPregnancyStatusFollowUp = 'upcoming' THEN 10\n" +

                "WHEN Ante_Natal_Care_Reminder_Visit = 'urgent' and BirthNotificationPregnancyStatusFollowUp = 'normal' THEN 11\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'upcoming' and BirthNotificationPregnancyStatusFollowUp = 'normal' THEN 12\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'normal' and BirthNotificationPregnancyStatusFollowUp = 'normal' THEN 13\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'expired' and BirthNotificationPregnancyStatusFollowUp = 'normal' THEN 14\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit is null and BirthNotificationPregnancyStatusFollowUp = 'normal' THEN 15\n" +

                "WHEN Ante_Natal_Care_Reminder_Visit = 'urgent' and BirthNotificationPregnancyStatusFollowUp = 'expired' THEN 16\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'upcoming' and BirthNotificationPregnancyStatusFollowUp = 'expired' THEN 17\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'normal' and BirthNotificationPregnancyStatusFollowUp = 'expired' THEN 18\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = 'expired' and BirthNotificationPregnancyStatusFollowUp = 'expired' THEN 19\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit is null and BirthNotificationPregnancyStatusFollowUp = 'expired' THEN 20\n" +

                "WHEN BirthNotificationPregnancyStatusFollowUp is null THEN 9999\n" +
                "WHEN Ante_Natal_Care_Reminder_Visit = \"\" THEN 99999\n" +
//                "WHEN BirthNotificationPregnancyStatusFollowUp is null THEN '18'\n" +
                "Else Ante_Natal_Care_Reminder_Visit END ASC";
    }

    /**
     * Override filter to capture fts filter by location
     *
     * @param filter
     */
    @Override
    public void onFilterSelection(FilterOption filter) {
        appliedVillageFilterView.setText(filter.name());
        filters = ((CursorFilterOption) filter).filter();
        mainCondition = "(Is_PNC is null or Is_PNC = '0') and MOTHERS_FIRST_NAME not null and MOTHERS_FIRST_NAME != \"\"   AND details  LIKE '%\"IS_VALID\":\"1\"%'";

        if (StringUtils.isNotBlank(filters) && filters.contains(" and details LIKE ")) {
            mainCondition += filters;
            filters = "";
        }
        CountExecute();
        filterandSortExecute();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    // override this on tha child classes to override specific fields
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

    public void processFormSubmission(String formSubmission) {
        Log.d(TAG, "submitted data = " + formSubmission);
//        JSONObject formSubmissionJson = null;
//        HashMap<String, String> dataHash = new HashMap<String, String>();
//        dataHash.put("mother_id", "1");
//        dataHash.put("reg_date", "2017-09-04");
//        dataHash.put("reg_place", "Sinza");
//
//        dataHash.put("MOTHERS_FIRST_NAME", "Jane");
//        dataHash.put("FWWOMLNAME", "Doe");
//        dataHash.put("FWHUSNAME", "John Doe");
//        dataHash.put("FWHOHBIRTHDATE", "07-09-1990");
//        dataHash.put("FWHOHGENDER", "2");
//        dataHash.put("FWWOMAGE", "27");
//
//
//
//        dataHash.put("case_num_pregnancies", "0");
//        dataHash.put("case_would_be_high_risk", "1");
//
//        dataHash.put("FWEDD", "2018-05-04");
//        dataHash.put("height", "190");
//        dataHash.put("weight_startofpregnancy", "80");
//
//        dataHash.put("user_type", "1");
//
//        Log.d(TAG, "formname = " + formName);
//        String modelString = readFileAssets("www/form/" + formName + "/model.xml").replaceAll("\"", "'").replaceAll("\n", "").replaceAll("\r", "").replaceAll("/", "/");
//
//        try {
//            formSubmissionJson = XML.toJSONObject(modelString);
//
//            for (Map.Entry<String, String> entry : dataHash.entrySet()) {
//                String key = entry.getKey();
//                String value = (String) entry.getValue();
//                populateJSONWithData(formSubmissionJson, key, value);
//            }
//
//            Log.d(TAG, "Results : " + formSubmissionJson.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        ((SecuredNativeSmartRegisterActivity) getActivity()).saveFormSubmission(formSubmissionJson.toString(), recordId, formName, getFormFieldsOverrides());
    }

    public String readFileAssets(String fileName) {
        String fileContents = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            fileContents = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        //Log.d("File", fileContents);
        return fileContents;
    }




    //TODO Implement this method to save the current unsubmitted form data for future uses
    public void saveCurrentFormData() {
        Log.d(TAG, "Save the currentform data");
    }

    private class ClientActionHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.d("coze","onclick listener clicked");
            switch (view.getId()) {
                case R.id.profile_info_layout:
                    AncDetailActivity.ancclient = (CommonPersonObjectClient) view.getTag();
                    Intent intent = new Intent(getActivity(), AncDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nbnf_due_date:
                    showFragmentDialog(new EditDialogOptionModelfornbnf(), view.getTag(R.id.clientobject));
                    break;
                case R.id.reminder:
                    CustomFontTextView ancreminderDueDate = (CustomFontTextView) view.findViewById(R.id.anc_reminder_due_date);
                    Intent intent2 = new Intent(getActivity(), AncFollowUpFormActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.reminder2:
                    Intent intent3 = new Intent(getActivity(), AncFollowUpFormActivity.class);
                    startActivity(intent3);
                    break;
            }
        }

        private void showProfileView(ECClient client) {
            navigationController.startEC(client.entityId());
        }
    }

    private class EditDialogOptionModelfornbnf implements DialogOptionModel {
        @Override
        public DialogOption[] getDialogOptions() {
            return getEditOptions();
        }

        @Override
        public void onDialogOptionSelection(DialogOption option, Object tag) {
            onEditSelection((EditOption) option, (SmartRegisterClient) tag);
        }
    }

    private class EditDialogOptionModelForANC implements DialogOptionModel {
        String ancvisittext;
        ;
        String Ancvisitstatus;

        public EditDialogOptionModelForANC(String text, String status) {
            ancvisittext = text;
            Ancvisitstatus = status;
        }

        @Override
        public DialogOption[] getDialogOptions() {
            return getEditOptionsforanc(ancvisittext, Ancvisitstatus);
        }

        @Override
        public void onDialogOptionSelection(DialogOption option, Object tag) {
            onEditSelection((EditOption) option, (SmartRegisterClient) tag);
        }
    }

    class ancControllerfiltermap extends ControllerFilterMap {

        @Override
        public boolean filtermapLogic(CommonPersonObject commonPersonObject) {
            boolean returnvalue = false;
            if (commonPersonObject.getDetails().get("IS_VALID") != null) {
                if (commonPersonObject.getDetails().get("IS_VALID").equalsIgnoreCase("1")) {
                    returnvalue = true;
                    if (commonPersonObject.getDetails().get("Is_PNC") != null) {
                        if (commonPersonObject.getDetails().get("Is_PNC").equalsIgnoreCase("1")) {
                            returnvalue = false;
                        }

                    }
                }
            }
            Log.v("the filter", "" + returnvalue);
            return returnvalue;
        }
    }

    private class EditDialogOptionModel implements DialogOptionModel {
        @Override
        public DialogOption[] getDialogOptions() {
            return getEditOptions();
        }

        @Override
        public void onDialogOptionSelection(DialogOption option, Object tag) {
            onEditSelection((EditOption) option, (SmartRegisterClient) tag);
        }
    }

}
