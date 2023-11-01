package com.zhangdi.myfunctiontest.fingerprint;

import android.app.Activity;


import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

public class FingerUtil {
    private CancellationSignal signal;
    private FingerprintManagerCompat fingerprintManager;

    public FingerUtil(Activity activity) {
        signal = new CancellationSignal();
        fingerprintManager = FingerprintManagerCompat.from(activity);
    }

    public void startFingerListen(FingerprintManagerCompat.AuthenticationCallback callback) {
        fingerprintManager.authenticate(null, 0, signal, callback, null);
    }

    public void stopsFingerListen() {
        if (signal != null) {
            signal.cancel();
        }
        signal = null;
    }
}
