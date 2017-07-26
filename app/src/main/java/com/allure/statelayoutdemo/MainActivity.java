package com.allure.statelayoutdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.allure.statelayout.StateLayout;


/**
 * Created by Allure on 2017/7/24.
 */

public class MainActivity extends AppCompatActivity {
    private StateLayout stateLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        stateLayout = (StateLayout) findViewById(R.id.state_layout);
//        stateLayout.setLoadingView(View.inflate(this, R.layout.state_loading, null));
//        stateLayout.setContentView(View.inflate(this, R.layout.state_content, null));
//        stateLayout.setErrorView(View.inflate(this, R.layout.state_error, null));
//        stateLayout.setEmptyView(View.inflate(this, R.layout.state_empty, null));
        stateLayout.showLoading();

        //视图切换动画
        stateLayout.setStateChangeListener(new StateLayout.SimpleStateChangeListener(){
            @Override
            public void onStateChange(int oldState, int newState) {
                super.onStateChange(oldState, newState);
            }

            @Override
            public void animationState(View exitView, View enterView) {
                super.animationState(exitView, enterView);
            }
        });
    }


    public void showError(View view) {
        stateLayout.showError();
    }

    public void showLoading(View view) {
        stateLayout.showLoading();
    }

    public void showEmpty(View view) {
        stateLayout.showEmpty();
    }

    public void showContent(View view) {
        stateLayout.showContent();
    }


}
