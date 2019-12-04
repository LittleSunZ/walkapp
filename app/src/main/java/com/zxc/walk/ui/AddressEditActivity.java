package com.zxc.walk.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.zxc.walk.R;
import com.zxc.walk.entity.Address;
import com.zxc.walk.entity.JsonBean;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.Shop;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.framework.utils.PhoneInfoTools;
import com.zxc.walk.framework.utils.ThreadManager;
import com.zxc.walk.ui.base.TitleBarActivity;
import com.zxc.walk.ui.widget.CustomToast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class AddressEditActivity extends TitleBarActivity {

    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.province_layout)
    LinearLayout provinceLayout;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String provinceSelected = "";
    private String citySelected = "";
    private String countySelected = "";
    private JsonBean selectedJsonBean;
    private int option1 = 0;
    private int option2 = 0;
    private int option3 = 0;
    /**
     * 地区
     */
    private String districtConcatStr;
    private boolean update;
    private Address address;

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_address_edit);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("收货地址");
        ThreadManager.getSinglePool().execute(new Runnable() {
            @Override
            public void run() {
                initJsonData();
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        provinceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPickerView();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    CustomToast.showToast(AddressEditActivity.this, "姓名不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                String phone = etPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    CustomToast.showToast(AddressEditActivity.this, "手机号不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (!PhoneInfoTools.isMobilePhone(phone)) {
                    CustomToast.showToast(AddressEditActivity.this, "手机号格式错误", Toast.LENGTH_SHORT);
                    return;
                }
                String addressStr = tvProvince.getText().toString() + etAddress.getText().toString().replace(" ", "");
                if (TextUtils.isEmpty(addressStr)) {
                    CustomToast.showToast(AddressEditActivity.this, "地址不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                showProgressDialog("");
                String isdefault = checkbox.isChecked() ? "1" : "0";
                Address address = new Address();
                address.setAddress(addressStr);
                address.setUserid(userInfo.getUserid());
                address.setName(name);
                address.setIsdefault(isdefault);
                address.setPhone(phone);

                Observable<Result<String>> reqUserInfo = walkApiReq.insertAddress(address);
                DataCallBack<String> dataCallBack = new DataCallBack<String>() {
                    @Override
                    protected void onSuccessData(String message) {
                        CustomToast.showToast(AddressEditActivity.this, "新增收货地址成功!", Toast.LENGTH_SHORT);
                        finish();
                    }

                    @Override
                    protected void onEnd() {
                        super.onEnd();
                        dismissProgressDialog();
                    }
                };
                HttpUtils.invoke(AddressEditActivity.this, reqUserInfo, dataCallBack);

            }
        });
    }

    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                provinceSelected = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                citySelected = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                countySelected = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                concatDistrict();
                tvProvince.setText(districtConcatStr);
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                provinceSelected = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                citySelected = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                countySelected = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                concatDistrict();
                tvProvince.setText(districtConcatStr);

            }
        })

                .setTitleText("")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(16)
                .setSelectOptions(option1, option2, option3)
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void concatDistrict() {
        districtConcatStr = provinceSelected + " " + citySelected + " " + countySelected;
        if (!TextUtils.isEmpty(provinceSelected)) {
            if (provinceSelected.startsWith("北京") || provinceSelected.startsWith("重庆") || provinceSelected.startsWith("天津") || provinceSelected.startsWith("上海")) {
                districtConcatStr = citySelected + " " + countySelected;
            }
        }
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = getJson(this, "district.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        if (!options1Items.isEmpty() && !TextUtils.isEmpty(provinceSelected)) {
            for (JsonBean bean : options1Items) {
                if (!TextUtils.isEmpty(bean.getName()) && bean.getName().startsWith(provinceSelected)) {
                    selectedJsonBean = bean;
                    break;
                }
            }
        }

        if (null != selectedJsonBean) {
            option1 = options1Items.indexOf(selectedJsonBean);
            List<JsonBean.CityBean> cityList = selectedJsonBean.getCityList();
            if (null != cityList && !cityList.isEmpty()) {
                for (int i = 0; i < cityList.size(); i++) {
                    JsonBean.CityBean cityBean = cityList.get(i);
                    if (!TextUtils.isEmpty(citySelected) && cityBean.getName().startsWith(citySelected)) {
                        option2 = i;
                        List<String> area = cityBean.getArea();
                        if (null != area && !area.isEmpty()) {
                            for (int k = 0; k < area.size(); k++) {
                                String countyArea = area.get(k);
                                if (!TextUtils.isEmpty(countySelected) && countyArea.startsWith(countySelected)) {
                                    option3 = k;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}
