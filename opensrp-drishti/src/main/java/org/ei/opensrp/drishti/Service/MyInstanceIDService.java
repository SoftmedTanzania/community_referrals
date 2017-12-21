package org.ei.opensrp.drishti.Service;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by kency on 12/11/17.
 */

public class MyInstanceIDService extends InstanceIDListenerService {
    public void onTokenRefresh() {
        refreshAllTokens();
    }

    private void refreshAllTokens() {
        // assuming you have defined TokenList as
//        // some generalized store for your tokens
//        ArrayList<TokenList> tokenList = TokensList.get();
//        InstanceID iid = InstanceID.getInstance(this);
//        for(tokenItem : tokenList) {
//            tokenItem.token = iid.getToken(tokenItem.authorizedEntity,tokenItem.scope,tokenItem.options);
//         send this tokenItem.token to your server
//    };
    }
}
