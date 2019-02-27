package com.softmed.htmr_chw.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.softmed.htmr_chw.R;

import org.ei.opensrp.domain.Client;

import java.util.Date;

public class ClientDetails extends AppCompatActivity {
    private static final String TAG = ClientDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        final Client client = (Client) bundle.getSerializable("client");

        TextView clientName = (TextView) findViewById(R.id.client_name);
        MaterialEditText gender = (MaterialEditText) findViewById(R.id.gender);
        MaterialEditText regDob = (MaterialEditText) findViewById(R.id.reg_dob);
        MaterialEditText village = (MaterialEditText) findViewById(R.id.editTextKijiji);
        MaterialEditText villageLeader = (MaterialEditText) findViewById(R.id.editTextVillageLeader);
        MaterialEditText cbhs = (MaterialEditText) findViewById(R.id.cbhs);
        MaterialEditText ctcNumber = (MaterialEditText) findViewById(R.id.ctc_number);
        MaterialEditText phone = (MaterialEditText) findViewById(R.id.edittextPhone);
        MaterialEditText helperName = (MaterialEditText) findViewById(R.id.helper_name);
        MaterialEditText helperPhoneNumber = (MaterialEditText) findViewById(R.id.helper_phone_number);

        Typeface robotoBold = Typeface.createFromAsset(getAssets(), "roboto_bold.ttf");

        ((TextView) findViewById(R.id.client_name)).setTypeface(robotoBold);
        clientName.setText(client.getFirst_name() + " " + client.getMiddle_name() + " " + client.getSurname());

        Date date = new Date();
        date.setTime(client.getDate_of_birth());
        gender.setText(client.getGender());
        village.setText(client.getVillage());
        villageLeader.setText(client.getVillage_leader());
        cbhs.setText(client.getCommunity_based_hiv_service());
        ctcNumber.setText(client.getCtc_number());
        phone.setText(client.getPhone_number());
        regDob.setText(date.toString());
        helperName.setText(client.getHelper_name());
        helperPhoneNumber.setText(client.getPhone_number());

        Button fab = (Button) findViewById(R.id.referal_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ClientDetails.this, ReferralFormRegisterActivity.class);
                intent.putExtra("clientName", client.getFirst_name()+" "+client.getMiddle_name()+" "+client.getSurname());
                intent.putExtra("clientId", client.getClient_id());
                startActivityForResult(intent, 90);
            }
        });


    }

}
