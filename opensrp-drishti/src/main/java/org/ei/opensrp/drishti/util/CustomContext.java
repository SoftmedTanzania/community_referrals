package org.ei.opensrp.drishti.util;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonFtsObject;
import org.ei.opensrp.drishti.Repository.CustomMotherRepository;

import java.util.HashMap;

/**
 * Created by ali on 9/14/17.
 */

public class CustomContext extends Context {

    private HashMap<String, CustomMotherRepository> customRepoMap;

    public CustomMotherRepository getCustomMotherRepo(String tableName) {
        // get commonFitsObject from super
        CommonFtsObject commonFtsObject = commonFtsObject();

        if (customRepoMap == null)
            customRepoMap = new HashMap<>();


        if (customRepoMap.get(tableName) == null) {
            // repo doesn't exist
            int index = 0;
            for (int i = 0; i < bindtypes.size(); i++) {
                if (bindtypes.get(i).getBindtypename().equalsIgnoreCase(tableName)) {
                    index = i;
                }
            }

            String bindTypeName = bindtypes.get(index).getBindtypename();
            String[] columnNames = bindtypes.get(index).getColumnNames();

            if (commonFtsObject != null && commonFtsObject.containsTable(tableName)) {
                // table already exists in repo
//                customRepoMap.put(bindTypeName,
//                        new CustomMotherRepository(commonFtsObject, bindTypeName, columnNames));
            } else {
                // table doesn't exist in repo
//                customRepoMap.put(bindTypeName,
//                        new CustomMotherRepository(bindTypeName, columnNames));
            }
        }

        return customRepoMap.get(tableName);
    }
}
