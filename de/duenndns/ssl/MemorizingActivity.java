package de.duenndns.ssl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemorizingActivity extends Activity implements OnCancelListener, OnClickListener {
    private static final Logger LOGGER;
    int decisionId;
    AlertDialog dialog;

    static {
        LOGGER = Logger.getLogger(MemorizingActivity.class.getName());
    }

    public void onCreate(Bundle savedInstanceState) {
        LOGGER.log(Level.FINE, "onCreate");
        super.onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        Intent i = getIntent();
        this.decisionId = i.getIntExtra("de.duenndns.ssl.DECISION.decisionId", 0);
        int titleId = i.getIntExtra("de.duenndns.ssl.DECISION.titleId", C1252R.string.mtm_accept_cert);
        String cert = i.getStringExtra("de.duenndns.ssl.DECISION.cert");
        LOGGER.log(Level.FINE, "onResume with " + i.getExtras() + " decId=" + this.decisionId + " data: " + i.getData());
        this.dialog = new Builder(this).setTitle(titleId).setMessage(cert).setPositiveButton(C1252R.string.mtm_decision_always, this).setNeutralButton(C1252R.string.mtm_decision_once, this).setNegativeButton(C1252R.string.mtm_decision_abort, this).setOnCancelListener(this).create();
        this.dialog.show();
    }

    protected void onPause() {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        super.onPause();
    }

    private void sendDecision(int decision) {
        LOGGER.log(Level.FINE, "Sending decision: " + decision);
        MemorizingTrustManager.interactResult(this.decisionId, decision);
        finish();
    }

    public void onClick(DialogInterface dialog, int btnId) {
        int decision;
        dialog.dismiss();
        switch (btnId) {
            case -3:
                decision = 2;
                break;
            case MqttServiceConstants.NON_MQTT_EXCEPTION /*-1*/:
                decision = 3;
                break;
            default:
                decision = 1;
                break;
        }
        sendDecision(decision);
    }

    public void onCancel(DialogInterface dialog) {
        sendDecision(1);
    }
}
