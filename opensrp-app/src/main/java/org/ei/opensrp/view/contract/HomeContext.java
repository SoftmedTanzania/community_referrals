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
    private long successReferralCount;
    private long unsuccessReferralCount;

    private CommonRepository commonRepository;
    private Cursor cursor;
    public Context context;
    public HomeContext(ANM anm) {
        this.successReferralCount = anm.getSuccessfulCount();
        this.unsuccessReferralCount = anm.getUnsucessfulCount();
    }


    public long getSucessReferralCount() {


            return successReferralCount;
    }

    public long getUnsucessReferralCount() {

        return unsuccessReferralCount;
    }
}
