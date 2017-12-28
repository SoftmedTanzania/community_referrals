package org.ei.opensrp.view.contract;

import org.ei.opensrp.domain.ANM;

public class HomeContext {
    private long sucessReferralCount;
    private long unsucessReferralCount;

    public HomeContext(ANM anm) {
        this.sucessReferralCount = anm.ancCount();
        this.unsucessReferralCount = anm.pncCount();
    }


    public long getSucessReferralCount() {
        return sucessReferralCount;
    }

    public long getUnsucessReferralCount() {
        return unsucessReferralCount;
    }
}
