package com.vchatcloud.androidsample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vchatcloud.android.VChatCloud;
import com.vchatcloud.android.VChatCloudException;
import com.vchatcloud.android.callback.ChannelCallback;
import com.vchatcloud.android.callback.JoinChannelCallback;
import com.vchatcloud.android.constant.SocketSet;
import com.vchatcloud.android.handlers.MessageHandler;
import com.vchatcloud.android.manager.Channel;
import com.vchatcloud.android.manager.ChannelOptions;
import com.vchatcloud.android.manager.SocketManager;
import com.vchatcloud.androidsample.adapter.ChatListAdapter;
import com.vchatcloud.androidsample.fragment.EmojiFragment;
import com.vchatcloud.androidsample.resourse.Constant;

import org.conscrypt.Conscrypt;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//

public class ChatActivity extends AppCompatActivity {
    private boolean emojiFlag = false;
    private static int msgId = 1;
    private String room_id;
    private String nick_name;
    public static int nick_icon_index;
    private static JSONObject userInfo = null;
    public static String deviceUuid;
    private static Channel channel = null;
    private ChannelOptions options;
    private static ChatListAdapter $Adapter;
    private static RecyclerView $RecyclerView;
    private TextView $Title;
    private Button $SendButton;
    private ImageButton $EmojiButton;
    private ImageButton $UploadButton;
    private FloatingActionButton $Fab;
    ProgressBar $ProgressBarValue;
    public static LinearLayout $ProgressBarLayout;
    private EditText $EditText;
    private boolean scrollstate = true;
    private LinearLayoutManager $LayoutManager;
    private int count = -1;
    private String toastMsg = "";
    private int toastTime = 0;
    private String CHANNEL_ID = "vchatcloud_noti_channel";
    public static boolean notificationFlag = false;
    private static FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private EmojiFragment emojiFragment;
    private Context $context;


    private static final String TAG = "vchatcloud";

    private ProgressRequestBody.Listener  listener = new ProgressRequestBody.Listener() {
        @Override
        public void onProgress(int progress) {
            Log.d("프로그래스",""+progress);
            Log.d("프로그래스",":: "+(progress == 100));
            $ProgressBarValue.setProgress(progress);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
        setContentView(R.layout.chat_list);

        $context = getApplicationContext();

        // 파라미터 전달 받기
        Intent intent = getIntent();
        Log.d(TAG, intent.getStringExtra("room"));
        Log.d(TAG, intent.getStringExtra("nick"));
        Log.d(TAG, intent.getIntExtra("nickIcon", 1) + "");
        room_id = intent.getStringExtra("room");
        nick_name = intent.getStringExtra("nick");
        nick_icon_index = intent.getIntExtra("nickIcon", 1) - 1;
        fragmentManager = getSupportFragmentManager();
        emojiFragment = new EmojiFragment();

        userInfo = new JSONObject();
        userInfo.put("profile", String.valueOf(nick_icon_index + 1));

        // uuid 생성
        uuid(ChatActivity.this);

        options = new ChannelOptions();
        options.setChannelKey(room_id);
        options.setClientKey(deviceUuid);
        options.setNickName(nick_name);
        options.setUserInfo(userInfo);

        // 채워질 화면 셋팅
        setUpRecyclerView();

        // 채팅박스 셋팅
        $SendButton = (Button) findViewById(R.id.button_chat_send);
        $EmojiButton = (ImageButton) findViewById(R.id.button_open_channel_chat_emoji);
        $UploadButton = (ImageButton) findViewById(R.id.button_open_channel_chat_upload);

        $EditText = (EditText) findViewById(R.id.edittext_chat_message);
        $Title = (TextView) findViewById(R.id.text_open_chat_title);
        $Fab = (FloatingActionButton) findViewById(R.id.fab);

        $ProgressBarValue = (ProgressBar) findViewById(R.id.progress_bar_value);
        $ProgressBarLayout = (LinearLayout) findViewById(R.id.progress_bar);
        $Fab.setBackgroundColor(Color.parseColor("#ffffff"));
        $Fab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        $Fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                $RecyclerView.smoothScrollToPosition($Adapter.getItemCount());
            }
        });

        $EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    $SendButton.setEnabled(true);
                    $SendButton.setBackgroundColor(Color.parseColor("#2bc48a"));
                    $SendButton.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    $SendButton.setEnabled(false);
                    $SendButton.setBackgroundColor(Color.parseColor("#00000000"));
                    $SendButton.setTextColor(Color.parseColor("#878788"));
                }
            }
        });

        // 처음 시작은 비활성화
        $SendButton.setEnabled(false);
        $SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject param = new JSONObject();
                JSONObject messageType = new JSONObject();
                messageType.put("profile", String.valueOf(nick_icon_index + 1));

                param.put("message", $EditText.getText().toString());
                param.put("mimeType", "text");
                param.put("messageType", messageType.toJSONString());
                if (userInfo != null) {
                    param.put("userInfo", userInfo.toJSONString());
                }

                sendMsg(param);

//                channel.sendMessage(param, new ChannelCallback() {
//                    @Override
//                    public void callback(Object o, VChatCloudException e) {
//                        if (e != null) {
//                            Log.d(TAG,"메시지 전송오류");
//                        }
//                    }
//                });
                $EditText.setText("");
            }
        });

        $EmojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("$EmojiButton", "이모지?" + emojiFlag);
                if (emojiFlag) {
                    closeEmoji();
                } else {
                    openEmoji();
                }
            }
        });
        
        $UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        // 채팅 화면 컨트롤
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            Log.d("키보드","활성화 전");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FragmentManager getEmojiManager() {
        return fragmentManager;
    }

    public static void sendMsg(JSONObject param) {
        $RecyclerView.smoothScrollToPosition($Adapter.getItemCount());
        channel.sendMessage(param, new ChannelCallback() {
            @Override
            public void callback(Object o, VChatCloudException e) {
                if (e != null) {
                    Log.d(TAG,"메시지 전송오류");
                }
                $RecyclerView.smoothScrollToPosition($Adapter.getItemCount());
            }
        });
    }

    private void openEmoji() {
        emojiFlag = true;
        $EmojiButton.setImageResource(R.drawable.ic_round_mood_24_active);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, emojiFragment).commitAllowingStateLoss();

        Log.d("openEmoji", "오픈되었나?");
    }

    private void closeEmoji() {
        emojiFlag = false;
        $EmojiButton.setImageResource(R.drawable.ic_round_mood_24);
        transaction = fragmentManager.beginTransaction();
        transaction.remove(emojiFragment).commitAllowingStateLoss();

        Log.d("closeEmoji", "종료?");
    }

    private void openGallery() {
        // TODO 현재 사진 선택만 가능 ( 파일공유는 조금더 찾아봐야 함 )
        Intent intent = new Intent();
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "*/*");
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        launcher.launch(intent);
    }

    @SuppressLint("Range")
    ActivityResultLauncher<Intent> previewLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Log.d("콜백 >> ", "" + result);
    });

    // 콜백
    @SuppressLint("Range")
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        String filePath = null;
        String fileName = null;
        Intent data;
        Uri uri;
        Cursor cursor;
        if (result.getResultCode() == Activity.RESULT_OK) {
            data = result.getData();
            Log.d("data", data + "");

            if (data != null) {
                uri = data.getData();

                Log.d(TAG, uri.getPath());
                Log.d(TAG, uri.getScheme());

                String[] projection = {"_data"};

                cursor = getContentResolver().query(uri, null, null, null, null);
                Log.d("debug", cursor.toString());
                if (uri.getScheme().equalsIgnoreCase("content")) {
                    try {
                        Log.d(TAG, cursor.toString());

                        if (cursor == null){
                            fileName = uri.getLastPathSegment();
                            filePath = uri.getPath();
                        } else {
                            cursor.moveToFirst();
                            Log.d(TAG, "cnt :: " + cursor.getColumnCount());

                            cursor.moveToFirst();
                            int index = cursor.getColumnIndex("_data");
                            filePath = cursor.getString(index);
                            if (filePath != null) {
                                fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                            }
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                } else if (uri.getScheme().equalsIgnoreCase("file")) {
                    Log.d(TAG, uri.getPath());
                }

                Log.d(TAG, "이거>> " + filePath + ", >>" + fileName);
                if (filePath == null) {
                    return;
                }
                File file = new File(filePath);
                $ProgressBarLayout.setVisibility(View.VISIBLE);
                $ProgressBarValue.setProgress(0);

                ProgressRequestBody requestBody = new ProgressRequestBody(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("roomId", options.channelKey)
                        .addFormDataPart("file",fileName, RequestBody.create(MultipartBody.FORM, file))
                        .build(), listener);

                Request request = new Request.Builder()
                        .url(Constant.BASE + Constant.SAVEFILE)
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        $ProgressBarLayout.setVisibility(View.GONE);
                        Log.w(TAG, "onFailure: " + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jo = (JSONObject) new JSONParser().parse(response.body().string());
                            Log.d(TAG, jo.toString());
                            JSONObject data = (JSONObject) jo.get("data");
                            Log.d(TAG, "data :: " + data);
                            Log.d(TAG, "fileNm :: " + data.get("fileNm"));
                            Log.d(TAG, "fileSize :: " + data.get("fileSize"));
                            Log.d(TAG, "expire :: " + data.get("expire"));
                            Log.d(TAG, "fileKey :: " + data.get("fileKey"));
                            Log.d(TAG, "fileSizeText :: " + data.get("fileSizeText"));
                            Log.d(TAG, "fileExt :: " + data.get("fileExt"));
                            Log.d(TAG, "result_cd :: " + jo.get("result_cd"));
                            Log.d(TAG, "result_msg :: " + jo.get("result_msg"));
                            JSONObject param = new JSONObject();
                            JSONObject message = new JSONObject();
                            JSONArray list = new JSONArray();
                            JSONObject messageType = new JSONObject();
                            message.put("id", data.get("fileKey"));
                            message.put("name", data.get("fileNm"));
                            message.put("type", data.get("fileExt"));
                            message.put("size", data.get("fileSize"));
                            message.put("expire", data.get("expire"));
                            list.add(message);
                            messageType.put("profile", String.valueOf(nick_icon_index + 1));

                            param.put("message", list.toJSONString());
                            param.put("mimeType", "file");
                            param.put("messageType", messageType.toJSONString());
                            if (userInfo != null) {
                                param.put("userInfo", userInfo.toJSONString());
                            }

                            sendMsg(param);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    });

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.getNotificationManager().cancelAll();
        notificationFlag = false;
        Log.w(TAG,"채팅 onStart" + VChatCloud.getInstance().getChannels().toString());
        if (channel != null) {
            if (VChatCloud.getInstance().getSocketStatus() == SocketSet.CLOSED) {
                SocketManager.getInstance().onDestroy();
                channel = null;
            } else {
                return;
            }
        }
        channel = VChatCloud.getInstance().joinChannel(options, new JoinChannelCallback() {
            public void callback(JSONArray history, VChatCloudException e) {
                Log.w(TAG, "조인 콜백");
                super.callback(history, e);
                int historySize = history.size() -1;
                for (; historySize >= 0; historySize--) {
                    Log.w(TAG, "" + history.get(historySize) );
                    messageExposure(new Message((JSONObject) history.get(historySize)), false);
                }
                $Title.setText(channel.getRoomName());
                PreferenceManager.setString(getApplicationContext(),"nickName", nick_name);
            }
        });

        channel.setHandler(new MessageHandler() {
            public void onNotifyLeaveUser(JSONObject data) { // 접속 해제 이벤트
                Message msg = new Message(data);
                msg.setType("leave");
                msg.setMessage(msg.getNickName() + " 님이 나가셨습니다.");
                messageExposure(msg, false);
            }
            public void onNotifyMessage(JSONObject data) { // 메시지 이벤트
                Message msg = new Message(data);
                msg.setType("msg");

                messageExposure(msg, false);
            }

            public void onNotifyWhisper(JSONObject data) { // 귓속말 이벤트
                Log.d("이벤트 수신", data.toJSONString());
                Message msg = new Message(data);
                msg.setType("whisper");

                messageExposure(msg, false);
            }

            public void onNotifyNotice(JSONObject data) { // 공지 이벤트
                Message msg = new Message(data);
                msg.setType("notice");

                messageExposure(msg, false);
            }

            public void onNotifyCustom(JSONObject data) { // 커스텀 이벤트
                Message msg = new Message(data);
                msg.setType("custom");

                messageExposure(msg, false);
            }

            public void onNotifyJoinUser(JSONObject data) { // 접속 이벤트
                Message msg = new Message(data);
                if (!nick_name.equalsIgnoreCase(msg.getNickName())) {
                    msg.setType("join");
                    msg.setMessage(msg.getNickName() + " 님이 입장하셨습니다.");
                    messageExposure(msg, false);
                }
            }
            public void onPersonalWhisper(JSONObject data) {
                Log.d("여기가 들어오나", "!!!");
                Message msg = new Message(data);
                msg.setType("preWhisper");

                messageExposure(msg, false);
            }

            public void onPersonalKickUser(JSONObject data) { //
                Message msg = new Message(data);
                msg.setType("preKick");

                messageExposure(msg, false);
            }

            public void onPersonalUnkickUser(JSONObject data) {
                Message msg = new Message(data);
                msg.setType("preUnKick");

                messageExposure(msg, false);
            }

            public void onPersonalMuteUser(JSONObject data) {
                Message msg = new Message(data);
                msg.setType("preMute");

                messageExposure(msg, false);
            }

            public void onPersonalUnmuteUser(JSONObject data) {
                Message msg = new Message(data);
                msg.setType("perUnMute");

                messageExposure(msg, false);
            }

            public void onPersonalDuplicateUser(JSONObject data) {
                Message msg = new Message(data);
                msg.setType("duplicate");

                messageExposure(msg, false);
            }

            public void onNotifyKickUser(JSONObject data) {
                Message msg = new Message(data);
                msg.setType("kick");

                messageExposure(msg, false);
            }

            public void onNotifyUnkickUser(JSONObject data) {
                Message msg = new Message(data);
                msg.setType("unKick");

                messageExposure(msg, false);
            }

            public void onNotifyMuteUser(JSONObject data) {
                Message msg = new Message(data);
                msg.setType("mute");

                messageExposure(msg, false);
            }

            public void onNotifyUnmuteUser(JSONObject data) {
                Message msg = new Message(data);
                msg.setType("unMute");

                messageExposure(msg, false);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationFlag = true;
        Log.w("채팅","onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("채팅","onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.getInstance().onDestroy();
        channel = null;
        Log.w(TAG,"종료");
        Log.w("채팅","onDestroy");
    }

    public void toastLong (String msg) {
        toastMsg = msg;
        toastTime = 1;
        toast();
    }

    public void toastShort (String msg) {
        toastMsg = msg;
        toastTime = 0;
        toast();
    }

    private void toast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChatActivity.this, toastMsg, toastTime).show();
            }
        });
    }

    public void messageExposure(Message message, final boolean sendScroll) {
        int profile_index = 0;
        if (ChatActivity.notificationFlag && (message.getMimeType().equalsIgnoreCase("text"))) {
            ++msgId;
//            PushCallDisplay();
            NotificationCompat.Builder builder = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new NotificationCompat.Builder(this, String.valueOf(R.string.channel_id));
            }else {
                builder = new NotificationCompat.Builder(this);
            }

            Intent resultIntent = new Intent(this, ChatActivity.class);
            resultIntent.putExtra("scroll","true");
            PendingIntent pendingIntent = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
                } else {
                    pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),defaultSoundUri);
            ringtone.play();

            if (message.getMessageType() != null) {
                profile_index = Integer.parseInt((String) message.getMessageType().get("profile")) - 1;
            }

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), MainActivity.ProfileInfo(profile_index));
            Log.d(TAG, message.getClientKey());
            builder
                    .setContentTitle(message.getNickName())
                    .setContentText(message.getMessage())
                    .setSmallIcon(R.drawable.vchat_logo)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.notificationColor))
                    .setLargeIcon(bitmap) // 사용자 아이콘?
//                    .setOnlyAlertOnce(true)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent);

            Notification notification = builder.build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            String cId = message.getClientKey();
            msgId = 0;
            for (int i = 0; i < cId.length(); i++) {
                msgId = (cId.charAt(i) - 64) + msgId;
            }
            notificationManager.notify(msgId, notification);
        }
        
        // 이모지 처리
        if ("emoji_img".equalsIgnoreCase(message.getMimeType())) {
            message.setType("emoji_img");
        } else if ("file".equalsIgnoreCase(message.getMimeType())) {
            message.setType("file");
        }

        Log.d(TAG, message.toString());
        $Adapter.addMsg(message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                $Adapter.notifyDataSetChanged();
                if (scrollstate) {
                    $RecyclerView.smoothScrollToPosition($Adapter.getItemCount());
                }
                if (sendScroll) {
                    $RecyclerView.smoothScrollToPosition($Adapter.getItemCount());
                }
            }
        });
    }

    // 화면 강제로 기상
    public void PushCallDisplay(){
        Log.w(TAG,"\n"+"[PushCallDisplay() 메소드 : 화면 강제 기상 실시]");
        try {
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag")
            PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
            wakelock.acquire();
            wakelock.release();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),defaultSoundUri);
            ringtone.play(); //TODO [사운드 재생]
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

   // uuid 획득 ( 모바일 고유 )
    public void uuid(Context context) {
        String serial;
        String androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
        } catch (IllegalAccessException e) {
            serial = "serial";
        } catch (NoSuchFieldException e) {
            serial = "serial";
        }

        deviceUuid = new UUID(androidId.hashCode(), serial.hashCode()).toString();
    }

    // 리사이클뷰 셋팅
    private void setUpRecyclerView() {
        $RecyclerView = (RecyclerView) findViewById(R.id.recycler_channel_chat);
        $Adapter = new ChatListAdapter(ChatActivity.this);
        $LayoutManager = new LinearLayoutManager(ChatActivity.this);
        $RecyclerView.setLayoutManager($LayoutManager);
        $RecyclerView.setAdapter($Adapter);
        $RecyclerView.setItemViewCacheSize(30);

        // Load more messages when user reaches the top of the current message list.
        $RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if ($LayoutManager.findLastVisibleItemPosition() == $Adapter.getItemCount() - 1) {
                    $Fab.setVisibility(View.GONE);
                    scrollstate = true;
                } else {
                    $Fab.setVisibility(View.VISIBLE);
                    scrollstate = false;
                }
            }
        });

        $RecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                                  int oldleft, int oldtop, int oldright, int oldbottom) {

                if (bottom < oldbottom) {
                    if (scrollstate) {
                        final int lastAdapterItem = $Adapter.getItemCount() - 1;
                        $RecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                int recyclerViewPositionOffset = -1000000;
                                View bottomView = $LayoutManager.findViewByPosition(lastAdapterItem);
                                if (bottomView != null) {
                                    recyclerViewPositionOffset = 0 - bottomView.getHeight();
                                }
                                $LayoutManager.scrollToPositionWithOffset(lastAdapterItem, recyclerViewPositionOffset);
                            }
                        });
                    }
                }
            }
        });

        $RecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                closeEmoji();
                return false;
            }
        });

        $Adapter.setOnItemLongClickListener(new ChatListAdapter.OnItemLongClickListener() {

            @Override
            public void onUserMessageItemLongClick(Message message, int position) {
                Log.d("길게","눌렀냥");
                Log.d("message >> ", message.toString());

                AlertDialog.Builder ad = new AlertDialog.Builder(ChatActivity.this);
                final EditText et = new EditText(ChatActivity.this);
                ad.setTitle(message.getNickName() + " 님에게 귓속말 보내기");
                ad.setMessage("");
                ad.setView(et);
                ad.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Yes Btn Click");

                        // Text 값 받아서 로그 남기기
                        String value = et.getText().toString();
                        Log.d(TAG, value);
                        JSONObject json = new JSONObject();
                        json.put("receivedClientKey", message.getClientKey());
                        json.put("message", value);
                        json.put("mimeType", "text");
                        channel.sendWhisper(json,  new ChannelCallback() {
                            @Override
                            public void callback(Object o, VChatCloudException e) {
                                if (e != null) {
                                    Log.d(TAG,"메시지 전송오류");
                                }
                                $RecyclerView.smoothScrollToPosition($Adapter.getItemCount());
                            }
                        });
                        dialog.dismiss();     //닫기
                    }
                });
                // 취소 버튼 설정
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                ad.show();
            }
        });
    }
}
