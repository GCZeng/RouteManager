package org.zgc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.apache.commons.lang3.StringUtils;
import org.zgc.R;
import org.zgc.activity.base.BaseActivity;
import org.zgc.adapter.RouteMacListAdapter;
import org.zgc.util.RouteUtil;
import org.zgc.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rv_list;
    private SwipeRefreshLayout srl_list;
    private FloatingActionButton fab_add;
    private ArrayList<String> list = null;

    public static String MAC_LIST = "MAC_LIST";

    private RouteMacListAdapter mRouteMacListAdapter = null;

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);

        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        srl_list = (SwipeRefreshLayout) findViewById(R.id.srl_list);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);

        fab_add.setOnClickListener(this);
        fab_add.setOnLongClickListener(view -> {
            Observable.create(subscriber -> {
                subscriber.onNext(RouteUtil.getInstance().resetMac());
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        ToastUtil.showLong(R.string.reset_success);
                        getMacList();
                    });
            return false;
        });

        initList();
    }

    private void initList() {
        //刷新 // 不能在onCreate中设置，这个表示当前是刷新状态，如果一进来就是刷新状态，SwipeRefreshLayout会屏蔽掉下拉事件
        //swipeRefreshLayout.setRefreshing(true);

        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        srl_list.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        srl_list.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        srl_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMacList();
            }
        });

        //设置布局管理器
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getMacList();

    }

    @Override
    public void initData() {

    }

    private void getMacList() {
        //获取数据
        Observable.create(subscriber -> {
            subscriber.onNext(RouteUtil.getInstance().getMacList());
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> srl_list.setRefreshing(true))
                .subscribe(result -> {
                    String macList = result.toString();
                    if (StringUtils.isNotEmpty(macList)) {
                        if (list == null) {
                            list = new ArrayList<>();
                        } else {
                            list.clear();
                        }
                        list.addAll(Arrays.asList(macList.split(" ")));
                    }
                    if (mRouteMacListAdapter == null) {
                        mRouteMacListAdapter = new RouteMacListAdapter(list);
                    } else {
                        mRouteMacListAdapter.notifyDataSetChanged();
                    }
                    rv_list.setAdapter(mRouteMacListAdapter);
                }, Throwable::printStackTrace, () -> srl_list.setRefreshing(false));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add:
                Intent intent = new Intent(this, MACAddACtivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(MAC_LIST, list);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
