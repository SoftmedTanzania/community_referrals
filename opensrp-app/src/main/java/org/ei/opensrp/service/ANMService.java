package org.ei.opensrp.service;

import org.ei.opensrp.Context;
import org.ei.opensrp.domain.ANM;
import org.ei.opensrp.repository.AllBeneficiaries;
import org.ei.opensrp.repository.AllEligibleCouples;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.repository.ClientReferralRepository;

public class ANMService {
    private AllSharedPreferences allSharedPreferences;
    private AllBeneficiaries allBeneficiaries;
    private AllEligibleCouples allEligibleCouples;
    public Context context;

    public ANMService(AllSharedPreferences allSharedPreferences, AllBeneficiaries allBeneficiaries, AllEligibleCouples allEligibleCouples) {
        this.allSharedPreferences = allSharedPreferences;
        this.allBeneficiaries = allBeneficiaries;
        this.allEligibleCouples = allEligibleCouples;
    }

    public ANM fetchDetails() {
        return new ANM(allSharedPreferences.fetchRegisteredANM(), allEligibleCouples.count(), allEligibleCouples.fpCount(),new ClientReferralRepository().succesfulcount(),new ClientReferralRepository().unsuccesfulcount());
    }
}
