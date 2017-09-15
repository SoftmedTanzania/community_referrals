package org.ei.opensrp.drishti.chw;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ei.opensrp.Context;
import org.ei.opensrp.adapter.SmartRegisterPaginatedAdapter;
import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.commonregistry.CommonPersonObjectController;
import org.ei.opensrp.cursoradapter.CursorCommonObjectFilterOption;
import org.ei.opensrp.cursoradapter.CursorCommonObjectSort;
import org.ei.opensrp.drishti.Anc.AncServiceModeOption;
import org.ei.opensrp.drishti.AncDetailActivity;
import org.ei.opensrp.drishti.AncFollowUpFormActivity;
import org.ei.opensrp.drishti.AncSmartRegisterActivity;
import org.ei.opensrp.drishti.Fragments.AncSmartRegisterFragment;
import org.ei.opensrp.drishti.LoginActivity;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.DataModels.ChwFollowUpMother;
import org.ei.opensrp.drishti.DataModels.PreRegisteredMother;
import org.ei.opensrp.drishti.pageradapter.CHWPagerAdapter;
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

import java.util.ArrayList;
import java.util.Map;

import static android.view.View.INVISIBLE;

public class CHWSmartRegisterActivity extends SecuredNativeSmartRegisterActivity {
    private static final String TAG = CHWSmartRegisterActivity.class.getSimpleName();
    private final ClientActionHandler clientActionHandler = new ClientActionHandler();
    private SmartRegisterClientsProvider clientProvider = null;
    private CommonPersonObjectController controller;
    private VillageController villageController;
    private DialogOptionMapper dialogOptionMapper;
    private String locationDialogTAG = "locationDialogTAG";
    private JSONObject fieldOverides = new JSONObject();
    private String recordId;
    private String formName = "pregnant_mothers_registration";
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chwregister);
        CHWPagerAdapter adapter = new CHWPagerAdapter(getSupportFragmentManager());

        ViewPager feeds = (ViewPager) findViewById(R.id.viewPager);
        feeds.setAdapter(adapter);

        feeds.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(feeds);

//        ((TextView) findViewById(R.id.txt_title_label)).setText("Community HW");
        // tabs icons
        tabs.getTabAt(0).setIcon(R.drawable.ic_account_circle);
        tabs.getTabAt(1).setIcon(R.drawable.ic_message_bulleted);

        final int colorWhite = ContextCompat.getColor(getApplicationContext(), android.R.color.white);
        final int colorPrimaryLight = ContextCompat.getColor(getApplicationContext(), R.color.primary_light);

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
    }

    private class ClientActionHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.d("coze", "onclick listener clicked");
            switch (view.getId()) {
                case R.id.profile_info_layout:
                    AncDetailActivity.ancclient = (CommonPersonObjectClient) view.getTag();
                    Intent intent = new Intent(CHWSmartRegisterActivity.this, AncDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nbnf_due_date:
                    showFragmentDialog(new CHWSmartRegisterActivity.EditDialogOptionModelfornbnf(), view.getTag(R.id.clientobject));
                    break;
                case R.id.reminder:
                    CustomFontTextView ancreminderDueDate = (CustomFontTextView) view.findViewById(R.id.anc_reminder_due_date);
                    Intent intent2 = new Intent(CHWSmartRegisterActivity.this, AncFollowUpFormActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.reminder2:
                    Intent intent3 = new Intent(CHWSmartRegisterActivity.this, AncFollowUpFormActivity.class);
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
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        Fragment prev = this.getFragmentManager().findFragmentByTag(locationDialogTAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        LocationSelectorDialogFragment
                .newInstance(this, new EditDialogOptionModel(), context().anmLocationController().get(),
                        "pregnant_mothers_registration")
                .show(ft, locationDialogTAG);
    }

    @Override
    protected void onCreation() {
    }

    @Override
    protected void onResumption() {

    }



    private DialogOption[] getEditOptions() {
        return this.getEditOptions();
    }

    private DialogOption[] getEditOptionsforanc(String ancvisittext, String ancvisitstatus) {
        return this.getEditOptionsforanc(ancvisittext, ancvisitstatus);
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


    public void showPreRegistrationDetailsDialog(PreRegisteredMother mother) {

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwregistration_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CHWSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        ImageView cancel = (ImageView) dialogView.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        textName.setText(mother.getName());
    }

    public void showPreRegistrationVisitDialog(final PreRegisteredMother mother){

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwregistration_visit_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CHWSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        Button button_yes = (Button) dialogView.findViewById(R.id.button_yes);
        Button button_no = (Button) dialogView.findViewById(R.id.button_no);

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CHWSmartRegisterActivity.this, "Asante kwa kumtembelea tena "+ mother.getName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        textName.setText(mother.getName());
    }

    public void showFollowUpDetailsDialog(ChwFollowUpMother mother) {

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwfollow_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CHWSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        ImageView cancel = (ImageView) dialogView.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        textName.setText(mother.getName());
    }

    public void showFollowUpFormDialog(final ChwFollowUpMother mother){

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwfollow_visit_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CHWSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.visit) ;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if(selectedId == R.id.visit_yes){
                    RelativeLayout info = (RelativeLayout) dialogView.findViewById(R.id.information);
                    info.setVisibility(View.VISIBLE);
                }
                if(selectedId == R.id.visit_no){
                    RelativeLayout info = (RelativeLayout) dialogView.findViewById(R.id.information);
                    info.setVisibility(View.GONE);
                }
            }
        });


        Button cancel = (Button) dialogView.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button save = (Button) dialogView.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CHWSmartRegisterActivity.this, "Asante kwa kumtembelea tena "+ mother.getName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        textName.setText(mother.getName());
    }

    public void confirmDelete() {
        final View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_confirm_delete, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CHWSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();


        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: delete mother


                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }


}
