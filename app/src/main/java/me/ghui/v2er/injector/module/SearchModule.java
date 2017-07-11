package me.ghui.v2er.injector.module;

import dagger.Module;
import dagger.Provides;
import me.ghui.v2er.R;
import me.ghui.v2er.adapter.base.CommonLoadMoreAdapter;
import me.ghui.v2er.adapter.base.ViewHolder;
import me.ghui.v2er.module.home.SearchContract;
import me.ghui.v2er.module.home.SearchFragment;
import me.ghui.v2er.module.home.SearchPresenter;
import me.ghui.v2er.network.bean.BingSearchResultInfo;
import me.ghui.v2er.widget.LoadMoreRecyclerView;

/**
 * Created by ghui on 02/06/2017.
 */

@Module
public class SearchModule {
    private SearchFragment mFragment;

    public SearchModule(SearchFragment fragment) {
        mFragment = fragment;
    }

    @Provides
    public SearchContract.IPresenter providePresenter() {
        return new SearchPresenter(mFragment);
    }

    @Provides
    public LoadMoreRecyclerView.Adapter<BingSearchResultInfo.Item> provideAdapter() {
        return new CommonLoadMoreAdapter<BingSearchResultInfo.Item>(mFragment.getContext(), R.layout.item_bing_search) {
            @Override
            protected void convert(ViewHolder holder, BingSearchResultInfo.Item item, int position) {
                holder.setText(R.id.search_result_title_tv, item.getTitle());
                holder.setText(R.id.search_result_content_tv, item.getContent());
            }
        };
    }
}
