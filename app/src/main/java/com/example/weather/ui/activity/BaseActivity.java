package com.example.weather.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by thanhle on 3/22/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getMainLayout());
        ButterKnife.bind(this);
        initToolbar();
        bindEventHandlers();
        initComponents();
    }

    protected abstract int getMainLayout();

    protected abstract void bindEventHandlers();

    protected abstract void initToolbar();

    protected abstract void initComponents();

}
