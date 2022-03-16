package me.ghui.v2er.module.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import me.ghui.v2er.R;

public class NewsHomeFragment extends Fragment implements MainActivity.ChangeTabTypeDelegate {

    private TabLayout mTabLayout;
    private ViewPager2 mNewsPager;
    private NewsPagerAdapter newsPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_news_home, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        mTabLayout = view.findViewById(R.id.tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            private long lastTime;

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (System.currentTimeMillis() - lastTime < 500) {
                    scrollToTop();
                }
                lastTime = System.currentTimeMillis();
            }
        });

        mNewsPager = view.findViewById(R.id.news_pager);
        mNewsPager.setOffscreenPageLimit(14);
        newsPagerAdapter = new NewsPagerAdapter(this);
        mNewsPager.setAdapter(newsPagerAdapter);

        new TabLayoutMediator(mTabLayout, mNewsPager, (TabLayoutMediator.TabConfigurationStrategy) (tab, position) -> {
            final TabInfo tabInfo = newsPagerAdapter.tabInfoList.get(position);
            tab.setText(tabInfo.title);
        }).attach();
    }

    @Override
    public void changeTabType(TabInfo tabInfo) {
        if (newsPagerAdapter == null) {
            return;
        }
        for (int i = 0; i < newsPagerAdapter.tabInfoList.size(); i++) {
            if (newsPagerAdapter.tabInfoList.get(i).title.equals(tabInfo.title)) {
                mNewsPager.setCurrentItem(i);
                break;
            }
        }

    }

    public boolean scrollToTop() { // 滑到顶部
        final int currentItem = mNewsPager.getCurrentItem();

        final View childAt = mNewsPager.getChildAt(0);
        if (childAt instanceof RecyclerView) {
            final RecyclerView.ViewHolder viewHolder = ((RecyclerView) childAt).findViewHolderForAdapterPosition(currentItem);
            if (viewHolder != null) {
                final View rootView = viewHolder.itemView;
                RecyclerView recyclerView = rootView.findViewById(R.id.base_recyclerview);
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(0);
                    return true;
                }
            }
        }

        return false;
    }

    private static class NewsPagerAdapter extends FragmentStateAdapter {
        final List<TabInfo> tabInfoList = TabInfo.getDefault();

        public NewsPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            final NewsFragment newsFragment = NewsFragment.newInstance();
            newsFragment.setCurrentTab(tabInfoList.get(position));
            return newsFragment;
        }

        @Override
        public int getItemCount() {
            return tabInfoList.size();
        }
    }
}
