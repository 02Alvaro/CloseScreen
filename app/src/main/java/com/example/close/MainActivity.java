package com.example.close;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName adminComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

        if (!devicePolicyManager.isAdminActive(adminComponent)) {
            // Solicitar permisos de administrador
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "This app needs to be Device Admin to turn off the screen.");
            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
        } else {
            // Apagar la pantalla
            lockScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (resultCode == RESULT_OK) {
                lockScreen();
            } else {
                finish(); // Cerrar la aplicación si no se otorgan los permisos
            }
        }
    }

    private void lockScreen() {
        // Apagar la pantalla
        devicePolicyManager.lockNow();
        finish(); // Cerrar la aplicación después de apagar la pantalla
    }
}
