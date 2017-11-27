package org.ei.opensrp.drishti.Repository;

import android.content.ContentValues;

import org.ei.opensrp.drishti.DataModels.FollowUp;

import static org.ei.opensrp.commonregistry.CommonRepository.DETAILS_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.ID_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.Relational_ID;

/**
 * Created by Kency on 14/09/2017.
 */

public class FollowUpRepository {

    public static final String Comment = "Comment";
    public static final String Date = "Date";
    public static final String Reason = "Reason";
    public static final String IS_VALID = "IsValid";



    public ContentValues createValuesFor(FollowUpPersonObject followUp) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, followUp.getId());
        values.put(Relational_ID, followUp.getRelationalId());
        values.put(Comment, followUp.getComment());
        values.put(Date, followUp.getDate());
        values.put(Reason, followUp.getReason());
        values.put(IS_VALID, followUp.getIsValid());
        values.put(DETAILS_COLUMN, followUp.getDetails());
        return values;
    }

}
