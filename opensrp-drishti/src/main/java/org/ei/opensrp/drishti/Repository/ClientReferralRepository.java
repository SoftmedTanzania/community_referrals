package org.ei.opensrp.drishti.Repository;

import android.content.ContentValues;

import static org.ei.opensrp.commonregistry.CommonRepository.DETAILS_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.ID_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.Relational_ID;

/**
 * Created by Kency on 14/09/2017.
 */

public class ClientReferralRepository {

    public static final String fName = "first_name";
    public static final String mName = "middle_name";
    public static final String lName = "surname";
    public static final String CBHS = "community_based_hiv_service";
    public static final String CTCNumber = "ctc_number";
    public static final String ReferralDate = "referral_date";
    public static final String ReferralFacility = "facility_id";
    public static final String ReferralReason = "referral_reason";
    public static final String Service = "referral_service_id";
    public static final String Status = "referral_status";
    public static final String IS_VALID = "is_valid";



    public ContentValues createValuesFor(ClientReferralPersonObject clientReferralPersonObject) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, clientReferralPersonObject.getId());
        values.put(Relational_ID, clientReferralPersonObject.getRelationalId());
        values.put(fName, clientReferralPersonObject.getFirst_name());
        values.put(mName, clientReferralPersonObject.getMiddle_name());
        values.put(lName, clientReferralPersonObject.getSurname());
        values.put(CBHS, clientReferralPersonObject.getCommunity_based_hiv_service());
        values.put(CTCNumber, clientReferralPersonObject.getCtc_number());
        values.put(ReferralDate, clientReferralPersonObject.getReferral_date());
        values.put(ReferralFacility, clientReferralPersonObject.getFacility_id());
        values.put(ReferralReason, clientReferralPersonObject.getReferral_reason());
        values.put(Service, clientReferralPersonObject.getReferral_service_id());
        values.put(Status, clientReferralPersonObject.getReferral_status());
        values.put(IS_VALID, clientReferralPersonObject.getIs_valid());
        values.put(DETAILS_COLUMN, clientReferralPersonObject.getDetails());
        return values;
    }

}
