package org.ei.opensrp.drishti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import org.ei.opensrp.domain.ClientReferral;
import org.ei.opensrp.drishti.Fragments.ClientDetailFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ClientsDetailsActivity extends AppCompatActivity {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private TextView name,contacts,sponsor,refered,referedReason,referedDate,chwId,note;
    private ClientReferral clientReferral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ClientDetailFragment.CLIENT_REFERRAL,
                    getIntent().getStringExtra(ClientDetailFragment.CLIENT_REFERRAL));
            ClientDetailFragment fragment = new ClientDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        clientReferral = (ClientReferral) bundle.getSerializable("client_referral");


        name = (TextView) findViewById(R.id.name);
        contacts = (TextView) findViewById(R.id.contacts);
        sponsor = (TextView) findViewById(R.id.sponsor);
        refered = (TextView) findViewById(R.id.refered);
        refered = (TextView) findViewById(R.id.refered_date);
        referedDate = (TextView) findViewById(R.id.refered_reason);
        chwId = (TextView) findViewById(R.id.chw_id);
        note = (TextView) findViewById(R.id.note);


        name . setText(clientReferral.getFirst_name()+" "+clientReferral.getMiddle_name()+", "+ clientReferral.getSurname());
        contacts.setText(clientReferral.getPhone_number());
        refered.setText(clientReferral.getFacility_id());
        referedReason.setText(clientReferral.getReferral_reason());
        referedDate.setText(dateFormat.format(clientReferral.getReferral_date()));
        chwId.setText(clientReferral.getService_provider_uiid());
        note.setText(clientReferral.getStatus());
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
//            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
