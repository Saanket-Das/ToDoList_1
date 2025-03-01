package com.yalantis.beamazingtoday.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.callback.BatCallback;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;
import com.yalantis.beamazingtoday.util.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

public class ExampleActivity extends AppCompatActivity implements BatListener, OnItemClickListener, OnOutsideClickedListener {

    private BatRecyclerView mRecyclerView;
    private BatAdapter mAdapter;
    private List<BatModel> mGoals;
    private BatItemAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        TextView titleText = findViewById(R.id.text_title);
        if (titleText != null) {
            titleText.setTypeface(TypefaceUtil.getAvenirTypeface(this));
        }

        mRecyclerView = findViewById(R.id.bat_recycler_view);
        mAnimator = new BatItemAnimator();

        // Initialize goals first
        mGoals = new ArrayList<>();
        mGoals.add(new Goal("first"));
        mGoals.add(new Goal("second"));
        mGoals.add(new Goal("third"));
        mGoals.add(new Goal("fourth"));
        mGoals.add(new Goal("fifth"));
        mGoals.add(new Goal("sixth"));
        mGoals.add(new Goal("seventh"));
        mGoals.add(new Goal("eighth"));
        mGoals.add(new Goal("ninth"));
        mGoals.add(new Goal("tenth"));

        // Initialize Adapter
        mAdapter = new BatAdapter(mGoals, this, mAnimator)
                .setOnItemClickListener(this)
                .setOnOutsideClickListener(this);

        mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.getView().setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BatCallback(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView.getView());
        mRecyclerView.getView().setItemAnimator(mAnimator);
        mRecyclerView.setAddItemListener(this);

        View rootView = findViewById(R.id.root);
        if (rootView != null) {
            rootView.setOnClickListener(v -> mRecyclerView.revertAnimation());
        }
    }

    @Override
    public void add(String string) {
        mGoals.add(0, new Goal(string));
        mAdapter.notify(AnimationType.ADD, 0);
    }

    @Override
    public void delete(int position) {
        if (position >= 0 && position < mGoals.size()) {
            mGoals.remove(position);
            mAdapter.notify(AnimationType.REMOVE, position);
        }
    }

    @Override
    public void move(int from, int to) {
        if (from >= 0 && to >= 0 && from < mGoals.size() && to < mGoals.size()) {
            mAnimator.setPosition(to);
            BatModel model = mGoals.get(from);
            mGoals.remove(from);
            mGoals.add(to, model);
            mAdapter.notify(AnimationType.MOVE, from, to);

            if (from == 0 || to == 0) {
                mRecyclerView.getView().scrollToPosition(Math.min(from, to));
            }
        }
    }

    @Override
    public void onClick(BatModel item, int position) {
        if (item != null) {
            Toast.makeText(this, item.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOutsideClicked() {
        mRecyclerView.revertAnimation();
    }
}
