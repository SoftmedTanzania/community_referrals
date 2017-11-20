package org.ei.opensrp.drishti.Repository;

import android.content.ContentValues;

import com.google.gson.Gson;

import static org.ei.opensrp.commonregistry.CommonRepository.DETAILS_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.ID_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.Relational_ID;

/**
 * Created by Kency on 14/09/2017.
 */

public class ClientReferralRepository {

    public static final String ClientName = "ClientName";
    public static final String CBHS = "CBHS";
    public static final String ReferralDate = "ReferralDate";
    public static final String ReferralFacility = "ReferralFacility";
    public static final String ReferralReason = "ReferralReason";
    public static final String Service = "Service";
    public static final String IS_VALID = "IS_VALID";



    public ContentValues createValuesFor(ClientReferralPersonObject clientReferralPersonObject) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, clientReferralPersonObject.getId());
        values.put(Relational_ID, clientReferralPersonObject.getRelationalId());
        values.put(ClientName, clientReferralPersonObject.getClientName());
        values.put(CBHS, clientReferralPersonObject.getCBHS());
        values.put(ReferralDate, clientReferralPersonObject.getReferralDate());
        values.put(ReferralFacility, clientReferralPersonObject.getReferralFacility());
        values.put(ReferralReason, clientReferralPersonObject.getReferralreason());
        values.put(Service, clientReferralPersonObject.getReferralService());
        values.put(IS_VALID, clientReferralPersonObject.getIS_VALID());
        values.put(DETAILS_COLUMN, clientReferralPersonObject.getDetails());
        return values;
    }

}
