package com.seoul.publicbooksearcher.presentation.view.component;

import android.widget.RelativeLayout;

public class ProgressBarView {

    private RelativeLayout progressBar;

    public ProgressBarView(RelativeLayout progressBar) {
        this.progressBar = progressBar;
    }

    public void visible() {
        progressBar.setVisibility(RelativeLayout.VISIBLE);
    }

    public void gone() {
        progressBar.setVisibility(RelativeLayout.GONE);
    }
}
