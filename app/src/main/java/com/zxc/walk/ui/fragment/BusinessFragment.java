package com.zxc.walk.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zxc.walk.R;
import com.zxc.walk.entity.BannerModel;
import com.zxc.walk.entity.Company;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.ShopListActivity;
import com.zxc.walk.ui.adapter.BusinessAdapter;
import com.zxc.walk.ui.base.BaseFragment;
import com.zxc.walk.ui.widget.EasyLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

public class BusinessFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<BannerModel> images;

    private int pageNum = 1;
    private int pageSize = 10;
    BusinessAdapter safeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_business;
    }

    @Override
    protected void setListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void init() {
        initRecyclerView();
        setBanner();
        swipeRefreshLayout.setRefreshing(true);
        reqTaskList();
    }


    private void setBanner() {
        images = new ArrayList<>();
        images.add(new BannerModel(R.drawable.banner1, 0));
        images.add(new BannerModel(R.drawable.banner2, 1));
        images.add(new BannerModel(R.drawable.banner3, 2));
        images.add(new BannerModel(R.drawable.banner4, 3));
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

    private void reqTaskList() {
        Observable<Result<ListResult<List<Company>>>> reqUserInfo = walkApiReq.getCompanyList(pageNum, pageSize);
        DataCallBack<ListResult<List<Company>>> dataCallBack = new DataCallBack<ListResult<List<Company>>>() {
            @Override
            protected void onSuccessData(ListResult<List<Company>> message) {
                if (pageNum == 1) {
                    safeAdapter.setNewData(message.getData());
                } else {
                    safeAdapter.addData(message.getData());
                    safeAdapter.loadMoreComplete();
                }
                if (pageNum * pageSize >= message.getTotal()) {
                    safeAdapter.loadMoreEnd();
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
                if (pageNum == 1) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);

    }

    private void initRecyclerView() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        safeAdapter = new BusinessAdapter();
        safeAdapter.bindToRecyclerView(recyclerView);
        safeAdapter.setLoadMoreView(new EasyLoadMoreView());
        safeAdapter.setOnLoadMoreListener(this, recyclerView);
        safeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Company company = (Company) adapter.getData().get(position);
                Intent intent = new Intent(getActivity(), ShopListActivity.class);
                intent.putExtra("companyid", company.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        reqTaskList();
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        reqTaskList();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            final BannerModel model = (BannerModel) path;
            imageView.setBackgroundResource(model.getImage());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }
}
