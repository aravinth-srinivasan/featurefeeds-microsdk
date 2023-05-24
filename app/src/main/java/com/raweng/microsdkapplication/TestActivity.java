package com.raweng.microsdkapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.raweng.dfe.DFEManager;
import com.raweng.dfe.modules.callbacks.DFEResultCallback;
import com.raweng.dfe.modules.policy.ErrorModel;
import com.raweng.dfe.modules.policy.RequestType;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*DFEManager.getInst().getQueryManager().getFeeds(
                null,
                null,
                null,
                RequestType.Network, new DFEResultCallback() {
                    @Override
                    public void onComplete(List<?> list, ErrorModel errorModel) {
                        Log.e("TAG", "onComplete: "+list.size());
                        Log.e("TAG", "error: "+errorModel.getErrorMessage());
                    }
                });*/
    }
}
