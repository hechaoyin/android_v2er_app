package me.ghui.v2ex.module.topic;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.ghui.v2ex.R;
import me.ghui.v2ex.injector.component.DaggerTopicComponent;
import me.ghui.v2ex.injector.module.TopicModule;
import me.ghui.v2ex.module.base.BaseActivity;
import me.ghui.v2ex.network.bean.TopicInfo;
import me.ghui.v2ex.widget.LoadMoreRecyclerView;

import static me.ghui.v2ex.util.Utils.KEY;

/**
 * Created by ghui on 04/05/2017.
 */

public class TopicActivity extends BaseActivity<TopicContract.IPresenter> implements TopicContract.IView,
        LoadMoreRecyclerView.OnLoadMoreListener {
    public static final String TOPIC_ID_KEY = KEY("topic_id_key");

    @BindView(R.id.recyclerview_act_topic)
    LoadMoreRecyclerView mLoadMoreRecyclerView;
    @Inject
    TopicAdapter mAdapter;
    private String mTopicId;

    @Override
    protected int attachLayoutRes() {
        return R.layout.act_topic;
    }

    @Override
    protected void startInject() {
        DaggerTopicComponent.builder()
                .appComponent(getAppComponent())
                .topicModule(new TopicModule(this))
                .build().inject(this);
    }

    @Override
    protected void parseExtras(Intent intent) {
        mTopicId = intent.getStringExtra(TOPIC_ID_KEY);
    }

    @Override
    protected void init() {
        mLoadMoreRecyclerView.addDivider();
        mLoadMoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLoadMoreRecyclerView.setAdapter(mAdapter);
        mLoadMoreRecyclerView.setOnLoadMoreListener(this);
    }

    @Override
    protected PtrHandler attachPtrHandler() {
        return new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.loadData(mTopicId);
            }
        };
    }

    @Override
    public void onLoadMore(int willLoadPage) {
        mPresenter.loadData(mTopicId, willLoadPage);
    }

    @Override
    public void fillView(TopicInfo topicInfo, boolean isLoadMore) {
        if (topicInfo == null) {
            mAdapter.setData(null);
            return;
        }
        mAdapter.setData(topicInfo.getReplies(), isLoadMore);
        boolean hasMore = mAdapter.getItemCountWithoutFooter() < topicInfo.getPage();
        mLoadMoreRecyclerView.setHasMore(hasMore);
    }
}