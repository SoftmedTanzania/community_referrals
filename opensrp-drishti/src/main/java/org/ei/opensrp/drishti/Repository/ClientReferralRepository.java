package org.ei.opensrp.drishti.Repository;

import android.content.ContentValues;

import static org.ei.opensrp.commonregistry.CommonRepository.DETAILS_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.ID_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.Relational_ID;

/**
 * Created by Kency on 14/09/2017.
 */

public class ClientReferralRepository {

    public static final String fName = "fName";
    public static final String mName = "mName";
    public static final String lName = "lName";
    public static final String CBHS = "CBHS";
    public static final String CTCNumber = "CTCNumber";
    public static final String ReferralDate = "ReferralDate";
    public static final String ReferralFacility = "FacilityId";
    public static final String ReferralReason = "ReferralReason";
    public static final String Service = "Service";
    public static final String Status = "Status";
    public static final String IS_VALID = "IS_VALID";



    public ContentValues createValuesFor(ClientReferralPersonObject clientReferralPersonObject) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, clientReferralPersonObject.getId());
        values.put(Relational_ID, clientReferralPersonObject.getRelationalId());
        values.put(fName, clientReferralPersonObject.getfName());
        values.put(mName, clientReferralPersonObject.getmName());
        values.put(lName, clientReferralPersonObject.getlName());
        values.put(CBHS, clientReferralPersonObject.getCBHS());
        values.put(CTCNumber, clientReferralPersonObject.getCTCNumber());
        values.put(ReferralDate, clientReferralPersonObject.getReferralDate());
        values.put(ReferralFacility, clientReferralPersonObject.getFacilityId());
        values.put(ReferralReason, clientReferralPersonObject.getReferralreason());
        values.put(Service, clientReferralPersonObject.getReferralService());
        values.put(Status, clientReferralPersonObject.getStatus());
        values.put(IS_VALID, clientReferralPersonObject.getIS_VALID());
        values.put(DETAILS_COLUMN, clientReferralPersonObject.getDetails());
        return values;
    }

}
