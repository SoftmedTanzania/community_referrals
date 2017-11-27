package org.ei.opensrp.drishti.Repository;

import android.content.ContentValues;

import static org.ei.opensrp.commonregistry.CommonRepository.DETAILS_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.ID_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.Relational_ID;

/**
 * Created by Kency on 14/09/2017.
 */

public class FacilityRepository {

    public static final String name = "Name";



    public ContentValues createValuesFor(FacilityObject facility) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, facility.getId());
        values.put(Relational_ID, facility.getRelationalId());
        values.put(name, facility.getName());
        values.put(DETAILS_COLUMN, facility.getDetails());
        return values;
    }

}
