package com.mao.travelapp.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mao.travelapp.R;

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initToolbar();
        setToolbaroOpposition();
        setToolbarMenu();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tb_base);
    }

    protected void hideToolbar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    public Toolbar getBaseToolbar() {
        if (toolbar != null) {
            return toolbar;
        } else
            return (Toolbar) findViewById(R.id.tb_base);
    }

    protected abstract int getLayoutId();

    /**
     * set toolbar's title、subtitle、logo、menu or navigations.If not set Toolbar just return void.
     */
    protected abstract void setToolbaroOpposition();

    protected void setToolbarMenu() {
        int MENU_ID = getToolbarMenuLayout();
        if (MENU_ID != 0) {
            toolbar.inflateMenu(MENU_ID);
            setBaseToolbarMenuItemClickListener();
        }
    }

    /**
     * The LayoutId of menu which you want to set.If did not set,just return 0.
     *
     * @return menu_layout_id.
     */
    protected abstract int getToolbarMenuLayout();

    protected abstract void setBaseToolbarMenuItemClickListener();

    protected void setMenuIcon(Drawable drawable) {
        toolbar.setOverflowIcon(drawable);
    }
}
