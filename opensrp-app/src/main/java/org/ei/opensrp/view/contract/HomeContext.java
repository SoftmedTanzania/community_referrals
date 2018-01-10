package org.ei.opensrp.view.contract;

import android.database.Cursor;
import android.util.Log;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ANM;

import java.util.List;

public class HomeContext {

    private static final String TAG = HomeContext.class.getSimpleName();
    private long sucessReferralCount;
    private long unsucessReferralCount;

    private CommonRepository commonRepository;
    private Cursor cursor;
    public Context context;
    public HomeContext(ANM anm) {
        this.sucessReferralCount = anm.getSuccessfulCount();
        this.unsucessReferralCount = anm.getUnsucessfulCount();
    }


    public long getSucessReferralCount() {

//            commonRepository = context.commonrepository("client_referral");
//            cursor = commonRepository.RawCustomQueryForAdapter("select * FROM client_referral where referral_status ='1'");
//
//            List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
//            Log.d(TAG,"success"+commonPersonObjectList.size());
            Log.d(TAG,"success"+sucessReferralCount);
            return sucessReferralCount;
    }

    public long getUnsucessReferralCount() {
//        commonRepository= context.commonrepository("client_referral");
//        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM client_referral where referral_status ='0'");
//        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
//        this.sucessReferralCount=commonPersonObjectList.size();
        Log.d(TAG,"unsuccess"+unsucessReferralCount);
        return unsucessReferralCount;
    }
}
