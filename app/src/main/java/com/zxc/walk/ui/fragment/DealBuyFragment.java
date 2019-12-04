package com.zxc.walk.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.zxc.walk.R;
import com.zxc.walk.entity.DealTable;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.Statistical;
import com.zxc.walk.entity.Transaction;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.adapter.BuyAdapter;
import com.zxc.walk.ui.base.BaseFragment;
import com.zxc.walk.ui.widget.CustomToast;
import com.zxc.walk.ui.widget.EasyLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

public class DealBuyFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.lineView)
    LineChart chart;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_zuidi)
    TextView tvZuidi;
    @BindView(R.id.tv_now)
    TextView tvNow;
    @BindView(R.id.tv_deal)
    TextView tvDeal;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_zhangdie)
    TextView tvZhangdie;
    @BindView(R.id.tv_zuigao)
    TextView tvZuigao;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Typeface mTf;

    private int pageNum = 1;
    private int pageSize = 10;
    BuyAdapter buyAdapter;
    private String phone;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(String event) {
        if (event.equals("pubblish_success")) {
            swipeRefreshLayout.setRefreshing(true);
            pageNum = 1;
            reqTaskList();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_buy;
    }

    @Override
    protected void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                pageNum = 1;
                reqTaskList();
                reqData();
            }
        });
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = etText.getText().toString();
                swipeRefreshLayout.setRefreshing(true);
                phone = text;
                pageNum = 1;
                reqTaskList();
            }
        });
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        initRecyclerView();

    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        pageNum = 1;
        reqTaskList();
        reqData();
    }

    private void reqData() {
        Observable<Result<Statistical>> reqUserInfo = walkApiReq.statistical(0);
        DataCallBack<Statistical> dataCallBack = new DataCallBack<Statistical>() {
            @Override
            protected void onSuccessData(Statistical message) {
                DecimalFormat df = new DecimalFormat("0.00");
                tvZuidi.setText("￥" + df.format(message.getLowData()));
                tvZuigao.setText("￥" + df.format(message.getHighData()));
                tvNow.setText("￥" + df.format(message.getNowData()));
                tvDeal.setText("" + message.getTotalData());
                tvBuy.setText("" + message.getReadyTotalData());
                tvZhangdie.setText(df.format(message.getRaiseData()) + "%");
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);

        Observable<Result<List<DealTable>>> table = walkApiReq.getDealTable(0);
        DataCallBack<List<DealTable>> tableCallBack = new DataCallBack<List<DealTable>>() {
            @Override
            protected void onSuccessData(List<DealTable> message) {
                Collections.reverse(message);
                initChart(message);
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, table, tableCallBack);
    }

    private void reqTaskList() {
        Observable<Result<ListResult<List<Transaction>>>> reqUserInfo = walkApiReq.getTransactionList(pageNum, pageSize, "0",phone);
        DataCallBack<ListResult<List<Transaction>>> dataCallBack = new DataCallBack<ListResult<List<Transaction>>>() {
            @Override
            protected void onSuccessData(ListResult<List<Transaction>> message) {
                if (pageNum == 1) {
                    buyAdapter.setNewData(message.getData());
                } else {
                    buyAdapter.addData(message.getData());
                    buyAdapter.loadMoreComplete();
                }
                if (pageNum * pageSize >= message.getTotal()) {
                    buyAdapter.loadMoreEnd();
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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        buyAdapter = new BuyAdapter(getActivity());
        buyAdapter.bindToRecyclerView(recyclerView);
        buyAdapter.setLoadMoreView(new EasyLoadMoreView());
        buyAdapter.setOnLoadMoreListener(this, recyclerView);
        buyAdapter.setOnDealClickListener(new BuyAdapter.OnDealClickListener() {
            @Override
            public void onSafe(Transaction transaction) {
                buy(transaction);
            }
        });
    }

    public void buy(Transaction transaction) {
        transaction.setDealuserid(userInfo.getUserid());
        Observable<Result<Transaction>> reqUserInfo = walkApiReq.updateTransaction("1", transaction);
        DataCallBack<Transaction> dataCallBack = new DataCallBack<Transaction>() {
            @Override
            protected void onSuccessData(Transaction message) {
                CustomToast.showToast(getActivity(), "出售成功!", Toast.LENGTH_SHORT);
                swipeRefreshLayout.setRefreshing(true);
                pageNum = 1;
                reqTaskList();
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(reqUserInfo, dataCallBack);
    }


    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        reqTaskList();
    }

    private void initChart(List<DealTable> message) {
        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String tradeDate = message.get((int) value % message.size()).getDate();
                SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
                try {
                    tradeDate = simpleDateFormat.format(sim.parse(tradeDate).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return tradeDate;
            }
        });
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.WHITE);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        chart.setScaleEnabled(false);
        chart.getDescription().setEnabled(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);
        // set data
        chart.setData(generateDataLine(message));

        chart.animateX(750);
    }

    public LineData generateDataLine(List<DealTable> message) {
        ArrayList<Entry> values1 = new ArrayList<>();
        for (int i = 0; i < message.size(); i++) {
            values1.add(new Entry(i, (float) message.get(i).getData()));
        }
        LineDataSet d1 = new LineDataSet(values1, "");
        d1.setColor(ContextCompat.getColor(getActivity(), R.color.text_main));
        d1.setLineWidth(2f);
        d1.setCircleRadius(3f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setCircleColor(ContextCompat.getColor(getActivity(), R.color.text_main));
        d1.setDrawValues(false);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);

        return new LineData(sets);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
