package com.vchatcloud.androidsample.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vchatcloud.androidsample.ChatActivity;
import com.vchatcloud.androidsample.Message;
import com.vchatcloud.androidsample.R;
import com.vchatcloud.androidsample.photoView.ViewPagerActivity;
import com.vchatcloud.androidsample.resourse.Constant;
import com.vchatcloud.androidsample.resourse.EmojiRescourse;
import com.vchatcloud.androidsample.resourse.ProfileRescource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static ArrayList<Message> $MessageList;
    private Message $Message;
    private Context $Context;
    private static final int VIEW_TYPE_USER_MESSAGE = 10;
    private static final int VIEW_TYPE_USER_ME_MESSAGE = 11;

    private static final int VIEW_TYPE_USER_MESSAGE_DUP = 12;
    private static final int VIEW_TYPE_USER_ME_MESSAGE_DUP = 13;

    private static final int VIEW_TYPE_USER_EMOJI = 14;
    private static final int VIEW_TYPE_USER_ME_EMOJI = 15;

    private static final int VIEW_TYPE_USER_FILE = 16;
    private static final int VIEW_TYPE_USER_ME_FILE = 17;

    private static final int VIEW_TYPE_NOTICE_MESSAGE = 20;
    private static final int VIEW_TYPE_ADMIN_MESSAGE = 30;

    private static final int VIEW_TYPE_USER_JOIN = 1;
    private static final int VIEW_TYPE_USER_LEAVE = 2;

    private OnItemLongClickListener $ItemLongClickListener;

    public interface OnItemLongClickListener {
        void onUserMessageItemLongClick(Message message, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        $ItemLongClickListener = listener;
    }

    public ChatListAdapter(Context context) {
        $MessageList = new ArrayList<Message>();
        $Context = context;
    }

    // 메시지 입력 ( 추가 )
    public void addMsg(Message message) {
        try {
            $MessageList.add(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return $MessageList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_USER_JOIN:
            case VIEW_TYPE_USER_LEAVE:
                return new UserNoticeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_notice, parent, false));
            case VIEW_TYPE_USER_MESSAGE:
                return new UserMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user, parent, false));
            case VIEW_TYPE_USER_MESSAGE_DUP:
                return new UserMessageDupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_dup, parent, false));
            case VIEW_TYPE_USER_ME_MESSAGE:
                return new UserMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_me, parent, false));
            case VIEW_TYPE_USER_ME_MESSAGE_DUP:
                return new UserMessageDupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_me_dup, parent, false));
            case VIEW_TYPE_USER_EMOJI:
                return new EmojiMesageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_emoji, parent, false));
            case VIEW_TYPE_USER_ME_EMOJI:
                return new EmojiMesageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_emoji_me, parent, false));
            case VIEW_TYPE_USER_FILE:
                return new FileMesageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_file, parent, false));
            case VIEW_TYPE_USER_ME_FILE:
                return new FileMesageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_file_me, parent, false));
            case VIEW_TYPE_ADMIN_MESSAGE:
                return new AdminMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_admin, parent, false));
            case VIEW_TYPE_NOTICE_MESSAGE:
                return new NoteceMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_notice, parent, false));
            default:
                return null;
        }
    }

    // 아이템 타입
    @Override
    public int getItemViewType(int position) {
        $Message = $MessageList.get(position);

        Boolean oldDateFlag = false;
        Boolean oldTimeFlag = false;
        String msgDt = "";
        String lastMsgDt = "";
        String newDate = "";
        String lastDate = "";
        Message listMsg = null;

        try {
            if (position != 0) {
                listMsg = $MessageList.get(position -1);

                if (!"".equalsIgnoreCase(listMsg.getMessageDt())) {
                    lastMsgDt = new SimpleDateFormat("a hh:mm").format(new SimpleDateFormat("yyyyMMddHHmmss").parse(listMsg.getMessageDt()));
                    lastDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(new SimpleDateFormat("yyyyMMddHHmmss").parse(listMsg.getMessageDt()));
                } else if (!"".equalsIgnoreCase(listMsg.getDate())) {
                    lastMsgDt = new SimpleDateFormat("a hh:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(listMsg.getDate()));
                    lastDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(listMsg.getDate()));
                }
            }

            if (!"".equalsIgnoreCase($Message.getMessageDt())) {
                msgDt = new SimpleDateFormat("a hh:mm").format(new SimpleDateFormat("yyyyMMddHHmmss").parse($Message.getMessageDt()));
                newDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(new SimpleDateFormat("yyyyMMddHHmmss").parse($Message.getMessageDt()));
            } else if (!"".equalsIgnoreCase($Message.getDate())) {
                msgDt = new SimpleDateFormat("a hh:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse($Message.getDate()));
                newDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse($Message.getDate()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        oldDateFlag = lastDate.equalsIgnoreCase(newDate);
        oldTimeFlag = lastMsgDt.equalsIgnoreCase(msgDt);

        try {
            switch ($Message.getType()) {
                case "join":
                    return VIEW_TYPE_USER_JOIN;
                case "leave":
                    return VIEW_TYPE_USER_LEAVE;
                case "notice":
                case "kick" :
                case "unKick" :
                case "mute" :
                case "unMute" :
                case "duplicate" :
                case "preMute" :
                case "perUnMute" :
                    return VIEW_TYPE_NOTICE_MESSAGE;
                case "emoji_img":
                    if (ChatActivity.deviceUuid.equalsIgnoreCase($Message.getClientKey())) {
                        return VIEW_TYPE_USER_ME_EMOJI;
                    } else {
                        return VIEW_TYPE_USER_EMOJI;
                    }
                case "file":
                    if (ChatActivity.deviceUuid.equalsIgnoreCase($Message.getClientKey())) {
                        return VIEW_TYPE_USER_ME_FILE;
                    } else {
                        return VIEW_TYPE_USER_FILE;
                    }
                case "msg":
                case "whisper":
                case "preWhisper":
                    if (ChatActivity.deviceUuid.equalsIgnoreCase($Message.getClientKey())) {
                        if ($MessageList.size() > 1 && position > 1) {
                            if ($MessageList.get(position).getClientKey().equalsIgnoreCase($MessageList.get(position - 1).getClientKey())) {
                                if (oldTimeFlag) {
                                    return VIEW_TYPE_USER_ME_MESSAGE_DUP;
                                } else {
                                    return VIEW_TYPE_USER_ME_MESSAGE;
                                }
                            } else {
                                return VIEW_TYPE_USER_ME_MESSAGE;
                            }
                        } else {
                            return VIEW_TYPE_USER_ME_MESSAGE;
                        }
                    } else {
                        if ($MessageList.size() > 1 && position > 1) {
                            if ($MessageList.get(position).getClientKey().equalsIgnoreCase($MessageList.get(position - 1).getClientKey())) {
                                if (oldTimeFlag) {
                                    return VIEW_TYPE_USER_MESSAGE_DUP;
                                } else {
                                    return VIEW_TYPE_USER_MESSAGE;
                                }
                            } else {
                                return VIEW_TYPE_USER_MESSAGE;
                            }
                        } else {
                            return VIEW_TYPE_USER_MESSAGE;
                        }
                    }
                case "":
                    return VIEW_TYPE_ADMIN_MESSAGE;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // 객체 바인딩
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = null;
        message = $MessageList.get(position);
        Boolean oldDateFlag = false;
        Boolean oldTimeFlag = false;
        String msgDt = "";
        String lastMsgDt = "";
        String newDate = "";
        String lastDate = "";
        Message listMsg = null;

        try {
            if (position != 0) {
                listMsg = $MessageList.get(position -1);

                if (!"".equalsIgnoreCase(listMsg.getMessageDt())) {
                    lastMsgDt = new SimpleDateFormat("a hh:mm").format(new SimpleDateFormat("yyyyMMddHHmmss").parse(listMsg.getMessageDt()));
                    lastDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(new SimpleDateFormat("yyyyMMddHHmmss").parse(listMsg.getMessageDt()));
                } else if (!"".equalsIgnoreCase(listMsg.getDate())) {
                    lastMsgDt = new SimpleDateFormat("a hh:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(listMsg.getDate()));
                    lastDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(listMsg.getDate()));
                }
            }

            if (!"".equalsIgnoreCase(message.getMessageDt())) {
                msgDt = new SimpleDateFormat("a hh:mm").format(new SimpleDateFormat("yyyyMMddHHmmss").parse(message.getMessageDt()));
                newDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(new SimpleDateFormat("yyyyMMddHHmmss").parse(message.getMessageDt()));
            } else if (!"".equalsIgnoreCase(message.getDate())) {
                msgDt = new SimpleDateFormat("a hh:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(message.getDate()));
                newDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(message.getDate()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        oldDateFlag = lastDate.equalsIgnoreCase(newDate);
        oldTimeFlag = lastMsgDt.equalsIgnoreCase(msgDt);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER_JOIN:
            case VIEW_TYPE_USER_LEAVE:
                ((UserNoticeHolder) holder).bind($Context, message);
                break;
            case VIEW_TYPE_USER_MESSAGE:
            case VIEW_TYPE_USER_ME_MESSAGE:
                ((UserMessageHolder) holder).bind($Context, message, oldDateFlag, position, msgDt, newDate, $ItemLongClickListener);
                break;
            case VIEW_TYPE_USER_MESSAGE_DUP:
            case VIEW_TYPE_USER_ME_MESSAGE_DUP:
                if (oldTimeFlag) {
                    ((UserMessageDupHolder) holder).bind($Context, message, oldDateFlag, position, msgDt, $ItemLongClickListener);
                } else {
                    ((UserMessageHolder) holder).bind($Context, message, oldDateFlag, position, msgDt, newDate, $ItemLongClickListener);
                }
                break;
            case VIEW_TYPE_USER_EMOJI:
            case VIEW_TYPE_USER_ME_EMOJI:
                ((EmojiMesageHolder) holder).bind($Context, message, position, msgDt);
                break;
            case VIEW_TYPE_USER_FILE:
            case VIEW_TYPE_USER_ME_FILE:
                ((FileMesageHolder) holder).bind($Context, message, position, msgDt);
                break;
            case VIEW_TYPE_ADMIN_MESSAGE:
                ((AdminMessageHolder) holder).bind($Context, message, oldDateFlag);
                break;
            case VIEW_TYPE_NOTICE_MESSAGE:
                ((NoteceMessageHolder) holder).bind($Context, message, oldDateFlag, position);
                break;
            default:
                break;
        }
    }

    // 유저 입퇴장
    private class UserNoticeHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        public UserNoticeHolder(@NonNull View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_open_chat_notice);
        }
        void bind(Context $Context, final Message message) {
            messageText.setText(message.getMessage());
        };
    }

    // 유저 메시지
    private class UserMessageHolder extends RecyclerView.ViewHolder {
        TextView nicknameText, messageText, timeText, dateText;
        ImageView profileImage;

        public UserMessageHolder(@NonNull View itemView) {
            super(itemView);
            nicknameText = (TextView) itemView.findViewById(R.id.text_open_chat_nickname);
            messageText = (TextView) itemView.findViewById(R.id.text_open_chat_message);
            timeText = (TextView) itemView.findViewById(R.id.text_open_chat_time);
            profileImage = (ImageView) itemView.findViewById(R.id.text_open_chat_icon);
            dateText = (TextView) itemView.findViewById(R.id.text_open_chat_date);
        }

        void bind(Context $Context, final Message message, boolean oldDateFlag, final int position, String msgDt, String newDate, @Nullable final OnItemLongClickListener longClickListener) {
            nicknameText.setText(message.getNickName());
            messageText.setText(message.getMessage());
            timeText.setText(msgDt);

            ProfileRescource.setIndex(0);
            if (message.getMessageType() != null) {
                ProfileRescource.setIndex(Integer.parseInt((String) message.getMessageType().get("profile")) -1);
            }

            profileImage.setImageResource(ProfileRescource.getProfile());
            profileImage.setClipToOutline(true);

            if (oldDateFlag) {
                dateText.setVisibility(View.GONE);
            } else {
                dateText.setVisibility(View.VISIBLE);
                dateText.setText(newDate);
            }

            if (longClickListener != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longClickListener.onUserMessageItemLongClick(message, position);
                        return true;
                    }
                });
            }

        }
    }

    // 이모지
    private class EmojiMesageHolder extends  RecyclerView.ViewHolder {
        TextView nicknameText;
        ImageView profileImage, emojiImage;

        public EmojiMesageHolder(@NonNull View itemView) {
            super(itemView);
            nicknameText = (TextView) itemView.findViewById(R.id.text_open_chat_nickname);
            profileImage = (ImageView) itemView.findViewById(R.id.text_open_chat_icon);
            emojiImage = (ImageView) itemView.findViewById(R.id.text_open_emoji);
        }
        void bind(Context $Context, final Message message, final int position, String msgDt) {
            nicknameText.setText(message.getNickName());

            // 내부 리소스중 이모지 정보 로딩
            emojiImage.setImageResource(EmojiRescourse.getUrlToEmojiInfo(message.getMessage()));

            ProfileRescource.setIndex(0);
            if (message.getMessageType() != null) {
                ProfileRescource.setIndex(Integer.parseInt((String) message.getMessageType().get("profile")) -1);
            }
            profileImage.setImageResource(ProfileRescource.getProfile());
            profileImage.setClipToOutline(true);
        }
    }

    // 파일
    private class FileMesageHolder extends RecyclerView.ViewHolder {
        TextView nicknameText;
        ImageView profileImage, fileImage, videoIcon, videoFullscreen;
        VideoView fileVideo;
        LinearLayout imageLayout, videoLayout, audioLayout, fileLayout;
        SeekBar videoSeekBar;

        public FileMesageHolder(@NonNull View itemView) {
            super(itemView);
            nicknameText = (TextView) itemView.findViewById(R.id.text_open_chat_nickname);
            profileImage = (ImageView) itemView.findViewById(R.id.text_open_chat_icon);
            fileImage = (ImageView) itemView.findViewById(R.id.text_open_image);
            fileVideo = (VideoView) itemView.findViewById(R.id.text_open_video);
            imageLayout = (LinearLayout) itemView.findViewById(R.id.text_open_image_layout);
            videoLayout = (LinearLayout) itemView.findViewById(R.id.text_open_video_layout);
            videoIcon = (ImageView) itemView.findViewById(R.id.text_open_pause_icon);
            videoSeekBar = (SeekBar) itemView.findViewById(R.id.text_open_seekbar);
            videoFullscreen = (ImageView) itemView.findViewById(R.id.text_open_video_fullscreen);

            audioLayout = (LinearLayout) itemView.findViewById(R.id.text_open_audio_layout);
            fileLayout = (LinearLayout) itemView.findViewById(R.id.text_open_file_layout);
        }
        private void allVisivilityOff() {
            imageLayout.setVisibility(View.GONE);
            videoLayout.setVisibility(View.GONE);
            audioLayout.setVisibility(View.GONE);
            fileLayout.setVisibility(View.GONE);
        }
        void bind(Context $Context, final Message message, final int position, String msgDt) {
            nicknameText.setText(message.getNickName());

            ProfileRescource.setIndex(0);
            if (message.getMessageType() != null) {
                ProfileRescource.setIndex(Integer.parseInt((String) message.getMessageType().get("profile")) -1);
            }
            profileImage.setImageResource(ProfileRescource.getProfile());
            profileImage.setClipToOutline(true);

            try {
                allVisivilityOff();
                // 파일 파싱 시작
                JSONObject object = null;
                JSONArray arr = (JSONArray) new JSONParser().parse(message.getMessage().toString());
                object = (JSONObject) arr.get(0);

                ChatActivity.$ProgressBarLayout.setVisibility(View.GONE);

                Log.d("파일정보", "사이즈 :: " + object.get("size"));
                Log.d("파일정보", "만료일 :: " + object.get("expire"));
                Log.d("파일정보", "파일명 :: " + object.get("name"));
                Log.d("파일정보", "파일아이디 :: " + object.get("id"));
                Log.d("파일정보", "파일타입 :: " + object.get("type"));

                String type = object.get("type") != null ? object.get("type").toString() : "";
                Log.d("type :: ", type);
                switch (type.toUpperCase()) {
                    // 이미지 타입
                    case "PNG":
                    case "JPG":
                    case "JPGE":
                    case "BMP":
                    case "TIFF":
                    case "GIF":
                    case "WEBP":
                        imageLayout.setVisibility(View.VISIBLE); // 이미지 뷰 활성화

//                        float width = MainActivity.getAppMetrics().widthPixels / MainActivity.getAppMetrics().density;

                        try {
                            String url = Constant.BASE + Constant.LOADFILE + "?fileKey=" + object.get("id");

                            Glide.with(itemView)
                                    .load(url)
//                                    .placeholder(R.drawable.ic_baseline_image_24)
                                    .thumbnail(Glide.with($Context).load(R.drawable.loading_spinner))
                                    .error(R.drawable.ic_baseline_image_24)
//                                    .override((int) ( 270 * MainActivity.getAppMetrics().density + 0.5), (int) ( 390 * MainActivity.getAppMetrics().density + 0.5))
                                    .fallback(R.drawable.ic_baseline_image_24)
                                    .into(fileImage);

                            fileImage.setClipToOutline(true);

                            fileImage.setOnTouchListener(new View.OnTouchListener() {
                                boolean actionFlag = false;
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                                        actionFlag = true;
                                    } else if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP && actionFlag) {
                                        Context context = view.getContext();
                                        Intent photoView = new Intent(context, ViewPagerActivity.class);
                                        photoView.putExtra("url", url);
                                        context.startActivity(photoView);
                                        actionFlag = false;
                                    } else if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE && !actionFlag) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                actionFlag = false;
                                            }
                                        }, 1000);
                                    }
                                    return true;
                                }
                            });
//                                ChatActivity ca = new ChatActivity();
//                                ca.Picture_okhttp_bt(url, fileImage);

//                                imageView.setClipToOutline(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }
                        break;
                    case "MP4":
                    case "AVI":
                    case "WMV":
                    case "MPG":
                    case "MPEG":
                    case "MKV":
                    case "MOV":
                        String url = Constant.BASE + Constant.LOADFILE + "?fileKey=" + object.get("id");
                        fileVideo.setVideoURI(Uri.parse(url));
                        Log.e("파일은??", fileVideo + "");
                        String _name = object.get("name") != null ? object.get("name").toString() : null;
                        // 영상 모두 시청 완료 이벤트
                        fileVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                Log.e("로딩은??", "완료");
                                fileVideo.seekTo(0);
                                videoLayout.performClick();
                            }
                        });
                        // 영상 로드 실패 이벤트
                        fileVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            String name = _name;
                            int retry = 3;
                            @Override
                            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                retry--;
                                if (retry < 0) {
                                    if (name != null) {
                                        Toast.makeText($Context.getApplicationContext(), "비디오 로딩 실패(" + name + ")", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText($Context.getApplicationContext(), "비디오 로딩 실패", Toast.LENGTH_SHORT).show();
                                    }
                                    return true;
                                } else {
                                    fileVideo.setVideoURI(Uri.parse(url));
                                }
                                return true;
                            }
                        });
//                        MediaController mediaController = new MediaController(fileVideo.getContext()) {
//                        };
//                        fileVideo.setMediaController(mediaController);
                        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                int duration = fileVideo.getDuration();
                                fileVideo.seekTo(((duration / 100) * seekBar.getProgress()));
                            }
                        });
                        fileVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                fileVideo.seekTo(0);
                                fileVideo.setBackgroundColor(Color.argb(95,0,10,0));
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int position = fileVideo.getCurrentPosition();
                                        int duration = fileVideo.getDuration();
                                        if (duration > 0) {
                                            int pos = (100 * position) / duration;
                                            videoSeekBar.setProgress(pos);
                                            handler.postDelayed(this, 300);
                                        }
                                    }
                                }, 300);

                                View.OnClickListener lis;
                                videoLayout.setOnClickListener(lis = new View.OnClickListener() {
                                    boolean play = true;
                                    @Override
                                    public void onClick(View view) {
                                        if (play) {
                                            fileVideo.setBackgroundColor(Color.argb(0, 0, 10, 0));
                                            videoIcon.setVisibility(View.GONE);
                                            fileVideo.start();
                                            play = false;
                                        } else {
                                            fileVideo.setBackgroundColor(Color.argb(95, 0, 10, 0));
                                            videoIcon.setVisibility(View.VISIBLE);
                                            fileVideo.pause();
                                            play = true;
                                        }
                                    }
                                });

                            }
                        });
                        videoFullscreen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri uri = Uri.parse(url);
                                intent.setDataAndType(uri, "video/*");
                                $Context.startActivity(intent);
                            }
                        });
                        videoLayout.setVisibility(View.VISIBLE);
                        break;
                    case "MP3":
                    case "WAV":
                        audioLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        fileLayout.setVisibility(View.VISIBLE);
                        Log.d("bind default", "default :: " + object.get("name"));
                        fileImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 유저 중복 메시지 ( 동일 인물 )
    private class UserMessageDupHolder extends RecyclerView.ViewHolder {
        TextView  messageText, timeText;

        public UserMessageDupHolder(@NonNull View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_open_chat_message);
            timeText = (TextView) itemView.findViewById(R.id.text_open_chat_time);
        }

        void bind(Context $Context, final Message message, boolean oldDateFlag, final int position, String msgDt, @Nullable final OnItemLongClickListener longClickListener) {
            messageText.setText(message.getMessage());
            timeText.setText(msgDt);

            if (longClickListener != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longClickListener.onUserMessageItemLongClick(message, position);
                        return true;
                    }
                });
            }
        }
    }

    // 어드민 메시지
    private class AdminMessageHolder extends RecyclerView.ViewHolder {

        public AdminMessageHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Context $Context, Message message, boolean oldDateFlag) {
        }
    }

    // 공지 메시지
    private class NoteceMessageHolder extends RecyclerView.ViewHolder {
        TextView nicknameText, messageText;

        public NoteceMessageHolder(@NonNull View itemView) {
            super(itemView);
            nicknameText = (TextView) itemView.findViewById(R.id.text_open_channel_list_name);
            messageText = (TextView) itemView.findViewById(R.id.text_open_channel_list_participant_count);

        }

         void bind(Context $Context, Message message, boolean oldDateFlag, int position) {
            nicknameText.setText(message.getNickName());
            messageText.setText(message.getMessage());
        }
    }

}