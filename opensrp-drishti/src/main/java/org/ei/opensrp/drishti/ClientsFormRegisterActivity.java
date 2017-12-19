package org.ei.opensrp.drishti;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by issy on 11/17/17.
 */

public class ClientsFormRegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public Button saveButton, referButton;
    private MaterialSpinner servicesSpinner, healthFacilitySpinner;
    public TextView clientName, ctcNumber, referalReasons, villageLeaderValue, referrerName;
    private EditText servicesOfferedEt, otherInformationEt;
    private CheckBox hivStatus;

    public TextView clientNames, wardText, villageText, hamletText, patientGender;

    public Dialog referalDialogue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        setupviews();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referalDialogue = new Dialog(this);
        referalDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);


    }




    private void setupviews(){


    }



}
