package com.vchatcloud.androidsample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.vchatcloud.android.VChatCloud;
import com.vchatcloud.android.constant.SocketSet;
import com.vchatcloud.android.manager.SocketManager;
import com.vchatcloud.android.utils.Util;
import com.vchatcloud.androidsample.adapter.HorizontalListView;
import com.vchatcloud.androidsample.resourse.Constant;
import com.vchatcloud.androidsample.resourse.ProfileRescource;

public class MainActivity extends AppCompatActivity {
    private HorizontalListView listview;
    private EditText et_nick;
    private Button btn_send;
    private static int profile_select = 0;
    private Boolean started = false;
    private Boolean buttonFlag = false;
    private static NotificationManager notificationManager;
    private static NotificationChannel notificationChannel;
    private String roomId = null;
    private static final String TAG = Util.ClassName(MainActivity.class);
    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);
    private static DisplayMetrics appMetrics;

    // 소켓 로딩완료 여부
    public static boolean getLoading() {
        if (SocketSet.CONNECTED == VChatCloud.getInstance().getSocketStatus()) {
            return true;
        }
        return false;
    }

    // 메인에서 뒤로가기 버튼
    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed("\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다", 3);
    }

    // 알림 메니저
    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

    // 프로필 url 정보
    public static int ProfileInfo(int index) {
        return ProfileRescource.profiles[index];
    }

    // 프로필 셋팅
    private void setUpHorizontalListView() {
        listview = (HorizontalListView) findViewById(R.id.listview);
        listview.setAdapter(new IconListAdapter());
    }

    public static DisplayMetrics getAppMetrics() {
        return appMetrics;
    }

    // 클릭리스터 오버라이드
    private abstract class OnClickListenerPutIndex implements View.OnClickListener {
        protected int index;
        protected LinearLayout linearLayout;
        public OnClickListenerPutIndex(int index, LinearLayout linearLayout) {
            this.index = index;
            this.linearLayout = linearLayout;
        }
    }

    // 아이콘 어뎁터
    private class IconListAdapter extends BaseAdapter {

        public IconListAdapter(){
            super();
        }

        public int getCount() {
            return ProfileRescource.profiles.length;
        }

        public Object getItem(int position) {
            return ProfileInfo(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View retval =  LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_icon_list_item, null, true);
            ImageButton item = retval.findViewById(R.id.profile_image);
            LinearLayout itemBack = retval.findViewById(R.id.profile_layout);

            // 기존 이미지에 라운드 제거
            OvalShape ovalShape = new OvalShape();
            ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
            shapeDrawable.getPaint().setStrokeWidth(0F);
            shapeDrawable.getPaint().setColor(Color.parseColor("#FFFFFF"));
            item.setBackground(shapeDrawable);
            item.setClipToOutline(true);

            // 각각 이미지 적용
            item.setImageResource(ProfileInfo(position));
            GradientDrawable myGrad = (GradientDrawable) getApplicationContext().getDrawable(R.drawable.profile_img_back_ground);
//            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            myGrad.setStroke((int) ( 3 * getAppMetrics().density + 0.5 ), getApplicationContext().getColor(R.color.profileOffColor));
            itemBack.setBackground(myGrad);
            // 클릭 리스너
            item.setOnClickListener(new OnClickListenerPutIndex(position, itemBack) {
                @Override
                public void onClick(View v) {
                    ImageButton bt = (ImageButton) v;
                    GradientDrawable myGrad2 = (GradientDrawable) getApplicationContext().getDrawable(R.drawable.profile_img_back_ground);
                    DisplayMetrics outMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

                    myGrad2.setStroke((int) (3 * outMetrics.density + 0.5), getApplicationContext().getColor(R.color.profileOffColor));
                    for (int i = 0; i < listview.getAdapter().getCount(); i++) {
                        View all = (View) listview.getItemChildAt(i);
                        if (all != null) {
                            LinearLayout itemBack2 = all.findViewById(R.id.profile_layout);
                            itemBack2.setBackground(myGrad2);
                        }
                    }
                    GradientDrawable myGrad = (GradientDrawable) getApplicationContext().getDrawable(R.drawable.profile_img_back_ground);
                    myGrad.setStroke((int) (3 * outMetrics.density + 0.5), getApplicationContext().getColor(R.color.colorAccent));
                    itemBack.setBackground(myGrad);
                    profile_select = position + 1;
                }
            });
            return retval;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        roomId = Constant.ROOM_ID;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 로딩 인텐트
        Intent loadingIntent = new Intent(this, LoadingActivity.class);
        startActivity(loadingIntent);

        // 아이콘 리스트 셋팅(아이콘)
        setUpHorizontalListView();

        GradientDrawable myGrad = (GradientDrawable) getApplicationContext().getDrawable(R.drawable.profile_img_back_ground);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        appMetrics = outMetrics;

        // 알림 셋팅
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(String.valueOf(R.string.channel_id));
//            NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationChannel = new NotificationChannel(String.valueOf(R.string.channel_id), String.valueOf(R.string.channel_name), NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(String.valueOf(R.string.channel_description));
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // 기본 엑션 셋팅
        et_nick = findViewById(R.id.et_nick);
        btn_send = findViewById(R.id.btn_send);

        Log.d(TAG, "버전 :: " + VChatCloud.VERSION);

        et_nick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                buttonFlag = true;
                if (start < 1 && count < 1) {
                    if (charSequence.length() < 1) {
                        buttonFlag = false;
                        et_nick.setError("사용자 이름을 한글자 이상 입력해 주세요.");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 텍스트 완료버튼 엑션
        et_nick.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) { // 완료버튼
                    btn_send.callOnClick();
                }
                return true;
            }
        });

        // 확인 버튼 액션
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonFlag && profile_select != 0) {
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("room", roomId); // 룸 ID
                    intent.putExtra("started", started); // 시작 알림
                    intent.putExtra("nick", et_nick.getText().toString()); // 닉네임 정보
                    intent.putExtra("nickIcon", profile_select); // 닉네임 아이콘 정보
                    startActivity(intent);
                } else {
                    // 유효성 불능
                    if (!buttonFlag) {
                        // 아이디가 오류
                        Toast toast = Toast.makeText(MainActivity.this, "사용자 아이디를 입력해 주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                        if (et_nick.requestFocus()) {
                            // 키보드 강제로 올리기
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        }
                    } else if (profile_select == 0) {
                        Toast toast = Toast.makeText(MainActivity.this, "캐릭터를 선택해 주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        // 저장된 내용 활용
        String nick_name = PreferenceManager.getString(MainActivity.this,"nickName");
        et_nick.setText(nick_name);

        try {
            Intent intent = getIntent();
            if (intent.hasExtra("message")) {
                Log.d(TAG, intent.getStringExtra("message"));
                Toast.makeText(MainActivity.this, "시스템오류: "+intent.getStringExtra("message"),Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, intent.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume");
//        Map channels = VChatCloud.getInstance().getChannels();
//        if (channels.toString().length() < 3) {
//            // 처음 시작
//        } else {
//
////            VChatCloud.getInstance().channelReset();
////            VChatCloud.getInstance().isInitializedReset();
////            VChatCloud.getInstance();
//        }
//        Log.d(TAG, "channels :: " + channels.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        SocketManager.getInstance().onDestroy();
    }
}