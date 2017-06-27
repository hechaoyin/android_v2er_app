package me.ghui.v2er.module.node;

import me.ghui.v2er.general.PreConditions;
import me.ghui.v2er.network.APIService;
import me.ghui.v2er.network.GeneralConsumer;
import me.ghui.v2er.network.bean.NodeInfo;
import me.ghui.v2er.network.bean.NodeTopicInfo;
import me.ghui.v2er.network.bean.SimpleInfo;
import me.ghui.v2er.util.RefererUtils;

/**
 * Created by ghui on 27/05/2017.
 */

public class NodeTopicPresenter implements NodeTopicContract.IPresenter {

    private NodeTopicContract.IView mView;

    public NodeTopicPresenter(NodeTopicContract.IView view) {
        mView = view;
    }

    @Override
    public void start() {
        APIService.get().nodeInfo(mView.nodeName())
                .compose(mView.rx())
                .subscribe(new GeneralConsumer<NodeInfo>(mView) {
                    @Override
                    public void onConsume(NodeInfo nodeInfo) {
                        if (nodeInfo.isValid()) {
                            mView.fillHeaderView(nodeInfo);
                        } else {
                            mView.toast("加载节点信息失败");
                        }
                    }
                });
        loadData(mView.initPage());
    }

    @Override
    public void loadData(int page) {
        APIService.get().nodesInfo(mView.nodeName(), page)
                .compose(mView.rx())
                .subscribe(new GeneralConsumer<NodeTopicInfo>() {
                    @Override
                    public void onConsume(NodeTopicInfo nodesInfo) {
                        mView.fillListView(nodesInfo, page > 1 && mView.initPage() == 1);
                    }
                });
    }

    @Override
    public void starNode(String url) {
        if (PreConditions.notLoginAndProcessToLogin(mView.getContext())) return;
        APIService.get().starNode(RefererUtils.tinyReferer(), url)
                .compose(mView.rx())
                .subscribe(new GeneralConsumer<SimpleInfo>() {
                    @Override
                    public void onConsume(SimpleInfo simpleInfo) {
                        boolean forStar = url.contains("/favorite/");
                        if (forStar) {
                            mView.afterStarNode();
                        } else {
                            mView.afterUnStarNode();
                        }
                    }
                });
    }

}
