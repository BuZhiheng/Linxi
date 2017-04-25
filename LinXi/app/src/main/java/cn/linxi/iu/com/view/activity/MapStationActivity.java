package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.ToastUtil;
/**
 * Created by buzhiheng on 2016/11/28.
 */
public class MapStationActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap baiduMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            SDKInitializer.initialize(getApplicationContext());
            setContentView(R.layout.activity_map_station);
        } catch (Exception e){
            ToastUtil.show("异常");
            finish();
        }
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("位置");
        mapView = (MapView) findViewById(R.id.map_station);
        baiduMap = mapView.getMap();
        LatLng point = new LatLng(getIntent().getFloatExtra(CommonCode.INTENT_LAT,0), getIntent().getFloatExtra(CommonCode.INTENT_LNG,0));
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_station_loc);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        baiduMap.addOverlay(option);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(16)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);
    }
    public void onClick(View v){
        finish();
    }
}