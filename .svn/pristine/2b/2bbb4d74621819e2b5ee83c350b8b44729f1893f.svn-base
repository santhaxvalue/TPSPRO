package com.panelManagement.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.panelManagement.utils.PanelConstants.FCM_REG_ID;

/**
 * Created by Centura User1 on 24-01-2017.
 */

public class FCMInstanceService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        FCM_REG_ID = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCMKey", FCM_REG_ID);
        // TODO: Implement this method to send any registration to your app's servers.
    }
}
