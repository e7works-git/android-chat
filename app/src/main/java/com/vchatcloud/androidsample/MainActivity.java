package com.vchatcloud.androidsample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vchatcloud.android.VChatCloud;
import com.vchatcloud.android.constant.SocketSet;
import com.vchatcloud.android.manager.SocketManager;
import com.vchatcloud.android.utils.Util;
import com.vchatcloud.androidsample.resourse.Constant;
import com.vchatcloud.androidsample.resourse.ProfileRescource;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = Util.ClassName(MainActivity.class);
    //    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);
    boolean doubleBackToExitPressedOnce = false;
    private EditText et_nick;
    private ImageView icon_img;
    private ImageButton icon_left;
    private ImageButton icon_right;
    private Button btn_login;
    private boolean buttonFlag = false;
    private GestureDetector detector;
    private static NotificationManager notificationManager;
    private static NotificationChannel notificationChannel;
    private String roomId = null;

    public static boolean getLoading() {
        if (SocketSet.CONNECTED == VChatCloud.getInstance().getSocketStatus()) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() { // 뒤로가기 한번 막음
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) { // 키보드 자동으로 내려주는 함수
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        // 로딩 인텐트
        Intent loading = new Intent(this, LoadingActivity.class);
        startActivity(loading);

        defSetting();
        notificationInit();
        event();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        // 저장된 내용 활용
        String nick_name = PreferenceManager.getString(MainActivity.this,"nickName");
        et_nick.setText(nick_name);
        roomId = Constant.ROOM_ID;

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
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        SocketManager.getInstance().onDestroy();
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

    private void defSetting() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        et_nick = findViewById(R.id.et_nick);
        icon_img = findViewById(R.id.icon_img);
        icon_left = findViewById(R.id.icon_left);
        icon_right = findViewById(R.id.icon_right);
        btn_login = findViewById(R.id.btn_login);

    }

    private void notificationInit()  {
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
    }

    private void event() {
        ProfileRescource.setIndex(PreferenceManager.getInt(getApplicationContext(),"nickIcon"));
        icon_img.setImageResource(ProfileRescource.getProfile());
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

        et_nick.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) { // 완료버튼
                    btn_login.callOnClick();
                }
                return true;
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonFlag) {
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("room", roomId); // 룸 ID
//                    intent.putExtra("started", started); // 시작 알림
                    intent.putExtra("nick", et_nick.getText().toString()); // 닉네임 정보
                    intent.putExtra("nickIcon", ProfileRescource.getIndex()); // 닉네임 아이콘 정보
                    startActivity(intent);
                } else {
                    // 아이디가 오류
                    Toast toast = Toast.makeText(MainActivity.this, "사용자 아이디를 입력해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    if (et_nick.requestFocus()) {
                        // 키보드 강제로 올리기
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }
            }
        });

        icon_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon_img.setImageResource(ProfileRescource.setLeft());
            }
        });

        icon_left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                icon_img.setImageResource(ProfileRescource.setLeft());
                return false;
            }
        });

        icon_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon_img.setImageResource(ProfileRescource.setRight());
            }
        });

        icon_right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                icon_img.setImageResource(ProfileRescource.setRight());
                return false;
            }
        });

        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            //화면이 눌린채 손가락이 가속해서 움직였다 떼어지는 경우
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int SWIPE_THRESHOLD = 100;
                int SWIPE_VELOCITY_THRESHOLD = 100;
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                float absX = Math.abs(distanceX);
                float absY = Math.abs(distanceY);
                float absVelocityX = Math.abs(velocityX);

                if (absX > absY && absX > SWIPE_THRESHOLD && absVelocityX > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0) {
                        icon_img.setImageResource(ProfileRescource.setLeft());
                    } else {
                        icon_img.setImageResource(ProfileRescource.setRight());
                    }
                }
                return true;
            }
        });

        icon_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return true;
            }

        });

    }

}