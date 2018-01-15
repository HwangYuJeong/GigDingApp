package org.androidtown.gigdingapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import org.androidtown.gigdingapp.R;

/**
 * Created by Kim on 2018-01-15.
 */

public class AddressPopupActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.address_popup);

    }
}
