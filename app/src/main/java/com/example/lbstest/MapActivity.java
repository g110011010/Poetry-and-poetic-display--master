package com.example.lbstest;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.lbstest.Controller.TTSController.TTSEngineController;
import com.example.lbstest.Controller.VenueDisplayController;
import com.example.lbstest.Controller.importLiBaiController;
import com.example.lbstest.DateBase.LI_BAI;
import com.example.lbstest.Entity.MapSearchFilterItem;
import com.example.lbstest.Adapter.MapSearchFilterItemAdapter;
import com.example.lbstest.AnimatorKits.AnimatorComeOrOut;
import com.example.lbstest.Controller.ForMapSpanManagerController;
import com.example.lbstest.Entity.Poet;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.example.lbstest.R.id.id_marker_poem_content;
import static com.example.lbstest.R.id.id_marker_poem_title;


/**
 * 此类用于处理地图显示及其事件响应。
 * @author BeiYun
 */
public class MapActivity extends AppCompatActivity implements View.OnClickListener,BaiduMap.OnMapLoadedCallback,WheelPicker.OnItemSelectedListener {
    //若要实现地理位置的编码此类需要实现接口：OnGetGeoCoderResultListener。
    //Log输出信息中所需标识符。
    private static final String TAG = "MapActivity";
    public static final String POET_NAME = "poet_name";
    public static final String POET_IMAGE_ID = "poet_image_id";
    //此列表用于记录所有的被读入的古诗等展示实体。
    private List<poetry> poems;
    //若要实现地理位置的编码则需要GeoCoder引用。
//    GeoCoder mSearch = null;
    public LocationClient mLocationClient;
    //显示地图的视图所对应的引用。
    private MapView mapView;
    //地图控制器所需引用。
    private BaiduMap baiduMap;
    //此引用为地图的状态更新提供帮助。
    private MapStatus ms;
    //此引用为地图的信息弹出窗口提供帮助。
    private InfoWindow mInfoWindow;
    //此引用负责接收搜索部分中展示列表中的表项。
    private ListView mListView;
    //此引用负责接收欢迎界面的实例。
////    private FloatingActionButton mFActionButton;
    //此引用负责接收点聚合管理对象的实例，其中点聚合对象为poetry。
    private ClusterManager<poetry> mClusterManager;
    //此引用为方向监听接口的实例接收端。
    private OrientationListener mOrientationListener;
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    //地图的古诗展示基础界面的展开部分。
    private LinearLayout markerInfo;
    //此引用用于接收古诗展示的题目面板。
    private TextView titleTextView;
    //此引用用于接收古诗展示的内容面板
    private TextView contentTextView;
    //以下为多重按钮的实例对应接收引用
    private FilterMenuLayout mFilterMenulayout;
    //地图的古诗展示的收缩界面。
    private LinearLayout markerInfo2;
    private FilterMenu.OnMenuChangeListener listener;
    //在依据时间进行展示时的滚轮面板。
    private LinearLayout venueDisplayLinearLayout;
    //在依据时间进行展示时的滚轮组件。
    private WheelPicker venueDisplayWheelPicker;
    //此引用用于获取位于搜索模块第二层包裹的LinearLayout实例（最外层为RelativeLayout）
    private LinearLayout mapSearch;
    //此引用用于获取地图界面的搜索组件实例。
    private SearchView mSearchView;
    private List<LI_BAI> li_bais;
    //列表实例将用于为MapSearchFilterItemAdapter适配器实例提供所需数据。
    private List<MapSearchFilterItem> itemList=new ArrayList<>();
    //此引用用于为搜索展示列表提供适配器。
    private MapSearchFilterItemAdapter mMapSearchFilterItemAdapter;
    //此引用为地图展示界面底部信息展示的组件存储部分，用于提高性能。
    private ViewHolder viewHolder = null;
    //此引用用于接收地图适度缩放即移动的地图跨度管理对象。
    private ForMapSpanManagerController mForMapSpanManager;
    //此引用用于接收展开时的底部按钮布局部分。
    private RelativeLayout buttonRelativeLayout;
    //此引用用于接收非展开情况下的除底部按钮的其它部分。
    private RelativeLayout RelativeLayout2;
    //此引用用于接收非展开状态下底部按钮布局部分。
    private RelativeLayout buttonRelativeLayout2;
    private LatLng entityLatLng;

    //此引用用于接收TTS控制模块。
    private TTSEngineController mTTSEngineController;

    //用于记录当前用户所处的纬度。
    private double mCurrentLantitude;
    //用于记录当前用户所处的经度。
    private double mCurrentLongitude;
    //用于记录当前用户手机地图测量经度。
    private float mCurrentAccracy;
    //当前方向或上一次所记录的方向与正北方向所成的夹角。
    private int mXDirection;
    //用于记录用户上一在地图中点击的古诗展示实例。
    private poetry lastPoetry;
    //判断是否为第一次地图加载,如果为第一次地图加载则移动到用户当前位置，否则不移动仅做其余运算。
    private boolean isFirstLocate = true;
    //判断当前是否为时间轴下诗人展示状态。
    private boolean isVenueDisplay = false;

    //以下为用于实现加载界面所需的实例接收引用。
//    private TextView mapWelcomeAndLoadingTime;
//    private RelativeLayout mapWelcomAndLoadingRelativeLayout;
//    private int countDownNumber=5;
//    private Animation animationForMapWelcomeAndLoading;
    private GoogleApiClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //此句为解决在输入框出现时对地图模块产生的压缩效果。
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //对用户当前位置确定所申请的实例
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        //对百度地图所有操作均需要初始化此接口。
        SDKInitializer.initialize(getApplicationContext());
        //设置当前活动的布局文件。
        setContentView(R.layout.activity_map);


        //对地图界面以及地图进行初始化与基础设置，此函数应该最优先被调用。
        initAndSetWiewAndMap();
        //对数据库进行初始化。
        initDataBase();
        //实现对基础资源以及数据资源的申请以及填充，应该位于数据库初始化后调用。
        applyAndSetBasicsResource();
        //初始化语音合成模块。
        initVoiceTTS();
        //初始化方向监听器。
        initOritationListener();
        //初始化地图点击监听器。
        initMapClickEvent();


        //对软件所需特殊权利进行申请。
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    /**
     * 对地图界面以及地图进行初始化与基础设置，此函数应该最优先被调用。
     */
    private void initAndSetWiewAndMap(){
        //判断是否有标题栏如果有隐藏。
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //获得地图的初始视图布局。
        mapView = (MapView) findViewById(R.id.bmapView);
        //设置缩放按钮为不出现。
        mapView.showZoomControls(false);
        mapView.removeViewAt(1);
        //借助mapView获取对百度地图操作的baiduMap实例。
        baiduMap = mapView.getMap();
        //设置用户当前位置可获取为true。
        baiduMap.setMyLocationEnabled(true);
        //当地图初次加载完成的时候在此类中设置的参数将会被调用。
        baiduMap.setOnMapLoadedCallback(this);
    }

    /**
     * 对数据库进行初始化,首先判断是否为软件第一次运行，若为第一次运行则读取数据库否则不进行任何操作。
     */
    private void initDataBase(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(isFirstRun) {
            editor.putBoolean("isFirstRun", false);
            editor.commit();
            importLiBaiController.importInto(this);
        }
    }

    /**
     * 对FilterMenu的选择图标进行选择。
     * @param layout 接收FilterMenuLayout类型的参数。
     * @return 返回FilterMenu菜单类型。
     */
    private FilterMenu attachMenu(FilterMenuLayout layout){
        return new FilterMenu.Builder(this)
                .addItem(R.drawable.icon_map_layers_white)
                .addItem(R.drawable.icon_map_pencil_white)
                .addItem(R.drawable.icon_map_preso_white)
                .attach(layout)
                .withListener(listener)
                .build();
    }

    /**
     * 此函数用于将某一RelativeLayout布局设置到其父布局的底部。
     * @param relativeLayout 某一RelativeLayout布局
     * @param acceptParam   relativeLayout所对应的ViewGroup.LayoutParams 参数。
     */
    private void initRelativeLayoutAlignParentBottom(RelativeLayout relativeLayout,ViewGroup.LayoutParams acceptParam){
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(acceptParam);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayout.setLayoutParams(params);
    }

    /**
     * 实现对基础资源以及数据资源的申请以及填充，应该位于数据库初始化后调用。
     */
    private void applyAndSetBasicsResource(){
        //欢迎界面的初始化部分。
        //        mapWelcomeAndLoadingTime=(TextView)findViewById(R.id.id_map_welcomeAndLoading_time) ;
//        mapWelcomeAndLoadingTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mapWelcomAndLoadingRelativeLayout.setVisibility(View.GONE);
//            }
//        });
//        showWelcomeAndLoadingSurface();

        //以下为对基础布局的加载与设置。
        applyAndSetLayout();
        //对点聚合管理类进行实例化。
        initClusterManager();
        //对百度地图编码的实例获取以及监听器的设置。
//        mSearch = GeoCoder.newInstance();
//        mSearch.setOnGetGeoCodeResultListener(this);
        //初始化FilterMenu监听器。(保留按键接口，考虑被替换)
        initFilterMenuOnMenuChangeListener();
        //初始化搜索框模块;
        initSearchView();
        //初始化搜索展示列表所对应表项的List,需在poetry存储列表初始化完成后进行。
        initListView();

    }

    /**
     * 初始化语音合成播放模块。
     */
    private void initVoiceTTS(){
        mTTSEngineController=new TTSEngineController(MapActivity.this,(FloatingActionButton) markerInfo.findViewById(R.id.id_marker_voicePlay));
        mTTSEngineController.initPlayFloatingActionButtonListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                titleTextView=(TextView) markerInfo.findViewById(id_marker_poem_title);
                contentTextView=(TextView)markerInfo.findViewById(id_marker_poem_content);
                String waitingForTTS="内容均未收录";
                String title=titleTextView.getText().toString();
                String content=contentTextView.getText().toString();
                if (("".equals(title))&&("".equals("content"))) {
                }else{
                    waitingForTTS=title+","+"李白"+","+content;
                }
                int result = mTTSEngineController.speak(waitingForTTS);
                if (result < 0) {
                    Toast.makeText(MapActivity.this,"语音播放组件故障",Toast.LENGTH_SHORT).show();;
                }
            }
        });
    }

    /**
     * 初始化语音播放相关布局设置。
     */
    private void initVoiceTTSrelevant(){
        (markerInfo2.findViewById(R.id.id_marker_voicePlay)).setVisibility(View.GONE);
    }

    /**
     * 初始化方向传感器。
     */
    private void initOritationListener() {
        mOrientationListener = new OrientationListener(
                getApplicationContext());
        mOrientationListener
                .setOnOrientationListener(new OrientationListener.OnOrientationListener() {
                    @Override
                    public void onOrientationChanged(float x) {
                        mXDirection = (int) x;
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(mCurrentAccracy)
                                .direction(mXDirection)
                                .latitude(mCurrentLantitude)
                                .longitude(mCurrentLongitude).build();
                        baiduMap.setMyLocationData(locData);
                        // 设置自定义图标
                        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                                .fromResource(R.drawable.navi_self);
                        MyLocationConfiguration config = new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker);
                        baiduMap.setMyLocationConfiguration(config);
                    }
                });
    }

    /**
     * 初始化地图点击监听器,重写onMapClick方法，在地图被点击后对各窗口的焦点获取状态和隐藏显示状态进行设置或者修改。
     */
    private void initMapClickEvent() {
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                mSearchView.clearFocus();
                exitInfo(View.GONE,View.VISIBLE);
                baiduMap.hideInfoWindow();
            }
        });
    }

    /**
     * 以下为对基础布局的加载与设置,主要对各个布局的引用进行初始化。
     */
    private void applyAndSetLayout(){
        //获取数据库内的数据。
        li_bais= DataSupport.findAll(LI_BAI.class);
        markerInfo = (LinearLayout) findViewById(R.id.id_main_marker_info);
        markerInfo2= (LinearLayout) findViewById(R.id.id_main_marker_info_button);
        markerInfo.setVisibility(View.GONE);
        mFilterMenulayout = (FilterMenuLayout) markerInfo.findViewById(R.id.id_map_filter_menu);
        buttonRelativeLayout=(RelativeLayout)markerInfo.findViewById(R.id.id_map_button_relative);
        RelativeLayout2 =(RelativeLayout)markerInfo2.findViewById(R.id.id_maker_inf_bottom_relativeLayout);
        markerInfo2.setVisibility(View.VISIBLE);
        RelativeLayout2.setVisibility(View.GONE);
        buttonRelativeLayout2=(RelativeLayout)markerInfo2.findViewById(R.id.id_map_button_relative);
        mapSearch= (LinearLayout)this.findViewById(R.id.id_main_Search);
        mListView=(ListView) mapSearch.findViewById(R.id.id_map_search_list) ;
        mapSearch=(LinearLayout) findViewById(R.id.id_map_search_Linear) ;
        initRelativeLayoutAlignParentBottom(buttonRelativeLayout2,buttonRelativeLayout2.getLayoutParams());
        attachMenu(mFilterMenulayout);
        localApplyAndSetLayout();
    }

    /**
     * 以下为局部性基础布局或组件的加载与设置。
     */
    private void localApplyAndSetLayout(){
        //对地图界面的返回按键进行初始化与设置。
        initBackFloatingActionButton();
        //对依时间轴进行诗人地点展示进行初始化和设置。
        initVenueDisplayFloatingActionButton();
        //对语音相关组件进行布局设置。
        initVoiceTTSrelevant();
    }

    /**
     * 对依时间轴进行诗人地点展示进行初始化和设置.
     */
    private void initVenueDisplayFloatingActionButton(){
        initVenueDisplayFloatingActionButtonListener((FloatingActionButton)buttonRelativeLayout.findViewById(R.id.id_marker_VenueDisplay));
        initVenueDisplayFloatingActionButtonListener((FloatingActionButton)buttonRelativeLayout2.findViewById(R.id.id_marker_VenueDisplay));
        initVenueDisplay();
    }

    /**
     * 初始化VenueDisplayFloatingActionButton所对应的监听响应，主要是针对依时间轴进行诗人地点展示的界面设置。
     * @param button 需要进行返回监听器设置的FloatingActionButton组件
     */
    private void initVenueDisplayFloatingActionButtonListener(FloatingActionButton button){
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isVenueDisplay){
                    exitInfo(View.GONE,View.VISIBLE);
                    venueDisplayWheelPicker.setVisibility(View.GONE);
                    initMarkers(li_bais);
                    isVenueDisplay=false;
                }else{
                    exitInfo(View.GONE,View.VISIBLE);
                    venueDisplayWheelPicker.setVisibility(View.VISIBLE);
                    isVenueDisplay=true;
                }
            }
        });
    }

    /**
     *对依时间展示地点滚轮面板进行初始化。
     */
    private void initVenueDisplay(){
        venueDisplayLinearLayout=(LinearLayout)this.findViewById(R.id.id_main_venueDisplay);
        venueDisplayWheelPicker=(WheelPicker)venueDisplayLinearLayout.findViewById(R.id.id_venue_wheel);
        venueDisplayWheelPicker.setVisibility(View.GONE);
        venueDisplayWheelPicker.setVisibleItemCount(5);
        venueDisplayWheelPicker.setData(VenueDisplayController.getAllYear(li_bais));
        initVenueDisplayListener();
    }

    /**
     * 对依时间展示地点滚轮面板组件进行监听器设置。
     */
    private void initVenueDisplayListener() {
        venueDisplayWheelPicker.setOnItemSelectedListener(this);
    }
    /**
     * 对地图界面的返回按键进行初始化与设置。
     */
    private void initBackFloatingActionButton(){
        initBackFloatingActionButtonListener((FloatingActionButton)buttonRelativeLayout.findViewById(R.id.id_marker_back));
        initBackFloatingActionButtonListener((FloatingActionButton)buttonRelativeLayout2.findViewById(R.id.id_marker_back));
    }

    /**
     * 对返回性质的所有BackFloatingActionButton设置监听器，
     * @param button 需要进行返回监听器设置的FloatingActionButton组件。
     */
    private void initBackFloatingActionButtonListener(FloatingActionButton button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 对点聚合管理类进行实例化,包含对缩放调整类的初始化和对监听器的设置。
     */
    private void initClusterManager(){
        mClusterManager = new ClusterManager<poetry>(this, baiduMap);
        //对地图缩放已经屏幕适应进行初始化。
        mForMapSpanManager = new ForMapSpanManagerController(baiduMap);
        //初始化点聚合中应该存储的点信息。
        initMarkers(li_bais);
        //设置与百度地图相关连的监听器，以ClusterManager类作为对应监听器。
        baiduMap.setOnMapStatusChangeListener(mClusterManager);
        baiduMap.setOnMarkerClickListener(mClusterManager);
        //初始化点聚合管理类的监听器。
        initClusterManagerListener();
    }

    /**
     * 用于对ClusterManager对象内的数据初始化
     * @param liBai 接收的信息将被转化后存储到ClusterManager对象中。
     */
    private void initMarkers(List<LI_BAI> liBai){
        poems = new ArrayList<poetry>();
        for(LI_BAI temp:liBai){
            entityLatLng =new LatLng(temp.getLatitude(),temp.getLongitude());
            if(temp.getWorksType().equals("")){
                continue;
            }else if(!temp.getPoetryTitle().equals("")){
                poems.add(new poetry(entityLatLng ,temp.getPoetryTitle(), "对应内容尚未收录"));
            }
        }
        mClusterManager.clearItems();
        mClusterManager.addItems(poems);
        mClusterManager.cluster();
    }

    /**
     * 初始化点聚合管理类的监听器。
     */
    private void initClusterManagerListener(){
        //当点聚合后的状态点被点击后将被调用。
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<poetry>() {
            @Override
            public boolean onClusterClick(Cluster<poetry> cluster) {
//                mSearchView.clearFocus();
//                List<LatLng> mapzoom=new ArrayList<>();
//                mapzoom.add(new LatLng(31.906965, 114.401394));
//                mapzoom.add(new LatLng(36.906965, 116.401394));
//
//                mForMapSpanManager.setScreenList(mapzoom);
//                mForMapSpanManager.addToMap();
//                mForMapSpanManager.zoomToSpan();
//
////                OverlayOptions oArc = new ArcOptions().color(0xAA00FF00).width(4)
////                        .points(new LatLng(31.906965, 114.401394), new LatLng(32.906965,115.401394), new LatLng(36.906965, 116.401394));//三点绘制一个弧形
////                baiduMap.addOverlay(oArc);

                VenueDisplayController.VenueDisplay(baiduMap,mClusterManager,725);
                exitInfo(View.GONE,View.VISIBLE);
                Toast.makeText(MapActivity.this,
                        "张永康在此处作了-" + cluster.getSize() + "-首诗", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //点聚合前的状态点被点击后将被调用。
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<poetry>() {
            @Override
            public boolean onClusterItemClick(final poetry poem) {
                if (lastPoetry == poem) {
                    if(mSearchView.hasFocus()){
                        lastPoetry = poem;
                        mSearchView.clearFocus();
                        Button button = new Button(getApplicationContext());
                        InfoWindow.OnInfoWindowClickListener listener ;
                        button.setText(poem.getName());
                        button.setBackgroundResource(R.drawable.tips);
                        button.setWidth(300);
                        listener = new InfoWindow.OnInfoWindowClickListener() {
                            public void onInfoWindowClick() {
                                exitInfo(View.GONE,View.VISIBLE);
//                        mFActionButton.setVisibility(View.GONE);
                                baiduMap.hideInfoWindow();
                            }
                        };
                        LatLng ll = poem.getPosition();
                        ms = new MapStatus.Builder().target(ll).build();
                        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                        baiduMap.showInfoWindow(mInfoWindow);
                        exitInfo(View.VISIBLE,View.GONE);
                        popupInfo(markerInfo, poem);
                        return false;
                    }else {

                    }
                    return false;
                } else {
                    lastPoetry = poem;
                    mSearchView.clearFocus();
                    Button button = new Button(getApplicationContext());
                    InfoWindow.OnInfoWindowClickListener listener = null;
                    button.setText(poem.getName());
                    button.setBackgroundResource(R.drawable.tips);
                    button.setWidth(300);
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            exitInfo(View.GONE,View.VISIBLE);
                            baiduMap.hideInfoWindow();
                        }
                    };
                    LatLng ll = poem.getPosition();
                    ms = new MapStatus.Builder().target(ll).build();
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    baiduMap.showInfoWindow(mInfoWindow);
                    exitInfo(View.VISIBLE,View.GONE);
                    popupInfo(markerInfo, poem);
                    return false;
                }
            }
        });
    }

    /**
     * 初始化搜索展示列表所对应表项的List,需在poetry存储列表初始化完成后进行。
     */
    private void initListView(){
        //初始化搜索展示列表所对应表项的基础信息。
        initSearchItem();
        //设置ListView允许进行自动筛选。
        mListView.setTextFilterEnabled(true);
        //设置数据的适配器
        mListView.setAdapter(mMapSearchFilterItemAdapter);
        //设置ListView的监听器。
        initListViewListener();
    }

    /**
     * 初始化搜索展示列表所对应表项的基础信息。
     */
    private void initSearchItem(){
        for(int i=0;i<poems.size();i++){
            itemList.add(new MapSearchFilterItem(poems.get(i).getName()));
        }
        mMapSearchFilterItemAdapter=new MapSearchFilterItemAdapter(MapActivity.this,R.layout.map_search_filter,itemList);
    }

    /**
     * initListViewListener();
     */
    private void initListViewListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<poems.size();i++){
                    if(poems.get(i).getName().equals(itemList.get(position).getName())){
                        LatLng searchedPoemPosition = poems.get(i).getPosition();
                        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(searchedPoemPosition);
                        baiduMap.animateMapStatus(update);
                        mSearchView.clearFocus();
                    }
                }
                buttonRelativeLayout2.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 初始化搜索框模块,搜索框模块默认的最初状态为展开状态。
     */
    private void initSearchView(){
        mSearchView=(SearchView) mapSearch.findViewById(R.id.id_map_search_view);
        //SearchView默认为展开状态。
        mSearchView.onActionViewExpanded();
        //设置SearchView的进入动画为中心展开。
        AnimatorComeOrOut.setCenterComeAnimator(mSearchView);
        //初始化搜索框模块的监听器。
        initSearchListener();
    }

    /**
     * 初始化搜索框模块的监听器。
     */
    private void initSearchListener(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean judgeSuccess=false;
                for(int i=0;i<poems.size();i++){
                    if( (poems.get(i).getName()).equals("《"+query+"》")){
                        Toast.makeText(MapActivity.this,"为您查询的内容是:"+query+"   正在移动到对应位置",Toast.LENGTH_SHORT).show();
                        LatLng searchedPoemPosition = poems.get(i).getPosition();
                        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(searchedPoemPosition);
                        baiduMap.animateMapStatus(update);
                        int id =mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
                        EditText textView=(EditText)mSearchView.findViewById(id);
                        textView.setText("");
                        mSearchView.clearFocus();
                        judgeSuccess=true;
                        break;
                    }
                }
                if(judgeSuccess==false) {
                    Toast.makeText(MapActivity.this, "抱歉未为您找到对应内容", Toast.LENGTH_SHORT).show();
                }
                buttonRelativeLayout2.setVisibility(View.VISIBLE);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    // 清除ListView的过滤    
                    mMapSearchFilterItemAdapter.clear();
                }else{
                    // 使用用户输入的内容对ListView的列表项进行过滤    
                    mMapSearchFilterItemAdapter.getFilter().filter(newText);

                }
                return true;
            }
        });

        mSearchView.setOnQueryTextFocusChangeListener(new android.view.View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(itemList.size()==0){
                        AnimatorComeOrOut.setTopTranslateOutAnimator(mListView,1500,-500f);
                    }else {

                        mListView.setVisibility(View.VISIBLE);
                        AnimatorComeOrOut.setTopTranslateComeAnimator(mListView,1000,-500f);
                    }
                    exitInfo(View.GONE,View.GONE);
                } else {
                    AnimatorComeOrOut.setTopTranslateOutAnimator(mListView,1500,-500f);
                    exitInfo(View.GONE,View.GONE);
                }
            }
        });
    }

    /**
     * 初始化FilterMenu监听器。(保留按键接口，考虑被替换)
     */
    private void initFilterMenuOnMenuChangeListener(){
        listener= new FilterMenu.OnMenuChangeListener() {
            @Override
            public void onMenuItemClick(View view, int position) {
                Toast.makeText(MapActivity.this, "Touched position " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuCollapse() {

            }

            @Override
            public void onMenuExpand() {

            }
        };
    }

    /**
     * 函数主要处理在某些组件失去焦点或者获取焦点的时候底部的信息布局的可视状态应该为哪一种选项。
     * @param markerInfoNumber 接收展开时底部信息栏的可视性参数。
     * @param buttonRelativeLayout2Number 接收在收缩时底部按钮栏的可视性参数。
     */
    protected void exitInfo(int markerInfoNumber,int buttonRelativeLayout2Number){
        markerInfo.setVisibility(markerInfoNumber);
        buttonRelativeLayout2.setVisibility(buttonRelativeLayout2Number);
    }

    /**
     * 函数主要处理百度地图中有效点被点击之后底部信息弹窗的显示，已加入优化。
     * @param mMarkerLy 接收底部的布局。
     * @param poem 接收要进行展示的信息类。
     */
    protected void popupInfo(LinearLayout mMarkerLy, poetry poem) {
        AnimatorComeOrOut.setBottomTranslateComeAnimator(mMarkerLy.findViewById(R.id.id_map_button_relative),800,800f);
        AnimatorComeOrOut.setBottomTranslateComeAnimator(mMarkerLy.findViewById(R.id.id_marker_poem_content_CardView),700,1500f);
        AnimatorComeOrOut.setBottomTranslateComeAnimator(mMarkerLy.findViewById(R.id.id_maker_inf_bottom),700,1000f);
        if (mMarkerLy.getTag() == null) {
            viewHolder = new ViewHolder();
            //在把com.example.lbstest.MarkerInfo组件读入后，利用读入的组件直接操作。
            viewHolder.poem_vote = (ImageView) mMarkerLy.findViewById(R.id.id_marker_poem_vote);
            viewHolder.poem_content = (TextView) mMarkerLy.findViewById(id_marker_poem_content);
            viewHolder.poem_title = (TextView) mMarkerLy.findViewById(id_marker_poem_title);
            viewHolder.poem_vote_number = (TextView) mMarkerLy.findViewById(R.id.id_marker_poem_vote_number);
            mMarkerLy.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) mMarkerLy.getTag();
        viewHolder.poem_vote.setImageResource(R.drawable.maker_inf_bottom_vote);
        viewHolder.poem_content.setText(poem.getContent());
        viewHolder.poem_title.setText(poem.getName());
        viewHolder.poem_vote_number.setText("741");
    }

    /**
     * 此函数用于处理用户的当前位置并将信息进行存储，如果为第一次定位则移动到用户的当前位置。
     * @param location 接收调用者传递的location信息。
     */
    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            //获取当前的经纬度信息。
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            //生成MapStatusUpdate对象，并将需要存储的信息记录。
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            //让地图按照信息进行刷新。
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            //修改运行次数的判断参数。
            isFirstLocate = false;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(mXDirection).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        //对当前的各种信息进行存储和设置。
        mCurrentAccracy = location.getRadius();
        baiduMap.setMyLocationData(locData);
        mCurrentLantitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();
        Toast.makeText(MapActivity.this,mCurrentLantitude+"?"+mCurrentLongitude,Toast.LENGTH_LONG).show();
        //设置用户当前位置的图标
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.navi_self);
        MyLocationConfiguration config = new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker);
        baiduMap.setMyLocationConfiguration(config);
    }

    /**
     * 用户当前位置监测开启函数。
     */
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    /**
     * 初始化以及设置用户当前位置定位函数。
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //设置为每0.1秒进行一次检测。
        option.setScanSpan(100);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        VenueDisplayController.VenueDisplay(baiduMap,mClusterManager,Integer.valueOf(String.valueOf(data)));
    }

    /**
     * 对地图界面各按键事件进行反馈。
     * @param v 判断是哪一个对应的组件
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    /**
     * 对权限的申请进行反馈所需函数。
     * @param requestCode 请求码 用于唯一的确定一次请求。
     * @param permissions 请求列表。
     * @param grantResults 对用户所选择的是否成功进行记录。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    //用于处理欢迎界面的模块。
    //    private void showWelcomeAndLoadingSurface(){
//
//        FontController.setTheRunningAndRegularForTextView(this,(TextView)findViewById(R.id.id_map_welcomeAndLoadingIntroduce1));
//        FontController.setTheRunningAndRegularForTextView(this,(TextView)findViewById(R.id.id_map_welcomeAndLoadingIntroduce2));
//        animationForMapWelcomeAndLoading= AnimationUtils.loadAnimation(this, R.anim.welcome_countdown);
//        mapWelcomAndLoadingRelativeLayout=(RelativeLayout) findViewById(R.id.id_map_WelcomeAndLoading_Layout);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getCount();
//                handler.sendEmptyMessageDelayed(0, 1000);
//                mapWelcomeAndLoadingTime.startAnimation(animationForMapWelcomeAndLoading);
//
//            }
//        }).start();
//    }
//    private int getCount() {
//        countDownNumber--;
//        if (countDownNumber == 0) {
//            mapWelcomAndLoadingRelativeLayout.setVisibility(View.GONE);
//        }
//        return countDownNumber;
//    }

//    public void searchButtonProcess(LI_BAI liBai) {
//        convertProvince=liBai.getProvince();
//                convertCounty=liBai.getCounty();
//        Log.d(TAG,"search before"+convertLongitude+" "+convertLatitude);
//        mSearch.geocode(new GeoCodeOption().city(
//                convertProvince).address(convertCounty));
//        Log.d(TAG,"search after"+convertLongitude+" "+convertLatitude);
//    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onConnectHotSpotMessage(String var1, int var2) {
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {

                navigateTo(location);

            }
        }
    }

    private class ViewHolder {
        ImageView poem_vote;
        TextView poem_content;
        TextView poem_title;
        TextView poem_vote_number;
    }

    /**
     * 此类为实现了ClusterItem接口的类可用于ClusterManager接受的类类型。
     * @author BeiYun
     */
    public static class poetry extends Overlay implements ClusterItem {
        private final LatLng pPosition;
        //诗词的名字
        private String name;
        //诗词的内容
        private String content;
        public poetry(LatLng latLng, String name, String content) {
            pPosition = latLng;
            this.name = name;
            this.content = content;
        }
        @Override
        public LatLng getPosition() {
            return pPosition;
        }
        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
    }

    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
        ms = new MapStatus.Builder().zoom(9).build();
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }
//    @Override
//    public void onGetGeoCodeResult(GeoCodeResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            if(originalNumber==1){
//            mSearch.geocode(new GeoCodeOption().city(
//            convertProvince).address(convertProvince));
//                originalNumber=originalNumber+1;
//            }else {
//                Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                        .show();
//            }
//            return;
//        }
////        convertLatLng =new LatLng(32.939723, 115.325541);
//        String strInfo = String.format("纬度：%f 经度：%f",
//                result.getLocation().latitude, result.getLocation().longitude);
//        convertLatitude=result.getLocation().latitude;
//        convertLongitude=result.getLocation().longitude;
//        Log.d(TAG,"  "+convertLatitude+"      "+convertLongitude);
//        Toast.makeText(MapActivity.this, strInfo, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
//            return;
//        }
//        Toast.makeText(MapActivity.this, result.getAddress(),
//                Toast.LENGTH_LONG).show();
//
//    }
@Override
protected void onStart() {
    super.onStart();
    client.connect();
    //在本活动开启的时候开始检测方向。
    mOrientationListener.start();
    AppIndex.AppIndexApi.start(client, getIndexApiAction());
}

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mOrientationListener.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}