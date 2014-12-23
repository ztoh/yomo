package com.kescoode.android.yomo.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import org.robobinding.binder.Binders;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PresentationModel presentationModel = new PresentationModel();
//        View rootView = Binders.inflateAndBindWithoutPreInitializingViews(this, R.layout.activity_main, presentationModel);
        View rootView = Binders.inflateAndBind(this, R.layout.activity_main, presentationModel);
        setContentView(rootView);
    }

}
