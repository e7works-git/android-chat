package com.vchatcloud.androidsample.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.vchatcloud.androidsample.ChatActivity;
import com.vchatcloud.androidsample.R;

import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class EmojiAdapter extends BaseAdapter {
    private Context $context;
    private List<Integer> emojis;
    private LayoutInflater inflater;
    private String msgStart;
    private String msgEnd;

    public EmojiAdapter(Context c, org.json.JSONObject data) {
        this.$context = c;
        this.emojis = (List<Integer>) data.opt("data");
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        msgStart = data.optString("start") + data.optString("item") + "/" + data.optString("item") + "_";
        msgEnd = data.optString("end");
    }

    public void setEmojis(org.json.JSONObject data) {
        this.emojis = (List<Integer>) data.opt("data");
        msgStart = data.optString("start") + data.optString("item") + "/" + data.optString("item") + "_";
        msgEnd = data.optString("end");
    }

    @Override
    public int getCount() {
        return emojis.size();
    }

    @Override
    public Object getItem(int i) {
        return emojis.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private int count = 0;



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.inflater.inflate(R.layout.chat_emoji_layout, viewGroup, false);
        }
        ImageView viewById = (ImageView) view.findViewById(R.id.target_emoji_item);
        viewById.setImageResource(emojis.get(i));
        viewById.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject param = new JSONObject();
                JSONObject messageType = new JSONObject();
                messageType.put("profile", String.valueOf(ChatActivity.nick_icon_index + 1));
                DecimalFormat df = new DecimalFormat("000");
                String text = msgStart + df.format(i + 1) + msgEnd;

                param.put("message", text);
                param.put("mimeType", "emoji_img");
                param.put("messageType", messageType.toJSONString());

                ChatActivity.sendMsg(param);
            }
        });

        return view;
    }
}
