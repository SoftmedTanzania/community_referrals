package org.ei.opensrp.drishti.Repository;

import android.content.ContentValues;

import static org.ei.opensrp.commonregistry.CommonRepository.DETAILS_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.ID_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.Relational_ID;

/**
 * Created by Kency on 14/09/2017.
 */

public class CustomPncRepository {

    public static final String motherCaseId = "motherCaseId";
    public static final String childCaseId = "childCaseId";
    public static final String DELIVERY_COMPLICATION = "DELIVERY_COMPLICATION";
    public static final String DELIVERY_TYPE = "DELIVERY_TYPE";
    public static final String DELIVERY_DATE = "DELIVERY_DATE";
    public static final String ADMISSION_DATE = "ADMISSION_DATE";
    public static final String CREATEDBY = "CreatedBy";
    public static final String MODIFYBY = "ModifyBy";



    public ContentValues createValuesFor(PncPersonObject pncPersonObject) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, pncPersonObject.getId());
        values.put(Relational_ID, pncPersonObject.getRelationalId());
        values.put(motherCaseId, pncPersonObject.getMotherCaseId());
        values.put(childCaseId, pncPersonObject.getChildCaseId());
        values.put(DELIVERY_TYPE, pncPersonObject.getDELIVERY_TYPE());
        values.put(DELIVERY_COMPLICATION, pncPersonObject.getDELIVERY_COMPLICATION());
        values.put(DELIVERY_DATE, pncPersonObject.getDELIVERY_DATE());
        values.put(ADMISSION_DATE, pncPersonObject.getADMISSION_DATE());
        values.put(CREATEDBY, pncPersonObject.getCreatedBy());
        values.put(MODIFYBY, pncPersonObject.getModifyBy());
        values.put(DETAILS_COLUMN, pncPersonObject.getDetails());
        return values;
    }

}
