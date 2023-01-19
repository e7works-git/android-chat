package com.vchatcloud.androidsample.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vchatcloud.androidsample.R;
import com.vchatcloud.androidsample.adapter.EmojiAdapter;
import com.vchatcloud.androidsample.resourse.EmojiRescourse;

public class EmojiFragment extends Fragment {

    RadioGroup emojiGroup;
    RadioButton emoji1, emoji2, emoji3, emoji4, emoji5, emoji6, emoji7;
    GridView grid_view;
    EmojiAdapter emojiAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_emoji_pager, container, false);
        emojiGroup = (RadioGroup) view.findViewById(R.id.emojiGroup);
        grid_view = (GridView) view.findViewById(R.id.grid_view);
        emoji1 = (RadioButton) view.findViewById(R.id.emoji1);
        emoji2 = (RadioButton) view.findViewById(R.id.emoji2);
        emoji3 = (RadioButton) view.findViewById(R.id.emoji3);
        emoji4 = (RadioButton) view.findViewById(R.id.emoji4);
        emoji5 = (RadioButton) view.findViewById(R.id.emoji5);
        emoji6 = (RadioButton) view.findViewById(R.id.emoji6);
        emoji7 = (RadioButton) view.findViewById(R.id.emoji7);

        emoji1.setChecked(true);

        emojiAdapter = new EmojiAdapter(getContext(), EmojiRescourse.getEmojiList(1));
        grid_view.setAdapter(emojiAdapter);

        emojiGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton radioButton = radioGroup.findViewById(checkedId);

                switch (checkedId) {
                    case R.id.emoji1:
                        emojiAdapter.setEmojis(EmojiRescourse.getEmojiList(1));
                        emojiAdapter.notifyDataSetChanged();
                        break;
                    case R.id.emoji2:
                        emojiAdapter.setEmojis(EmojiRescourse.getEmojiList(2));
                        emojiAdapter.notifyDataSetChanged();
                        break;
                    case R.id.emoji3:
                        emojiAdapter.setEmojis(EmojiRescourse.getEmojiList(3));
                        emojiAdapter.notifyDataSetChanged();
                        break;
                    case R.id.emoji4:
                        emojiAdapter.setEmojis(EmojiRescourse.getEmojiList(4));
                        emojiAdapter.notifyDataSetChanged();
                        break;
                    case R.id.emoji5:
                        emojiAdapter.setEmojis(EmojiRescourse.getEmojiList(5));
                        emojiAdapter.notifyDataSetChanged();
                        break;
                    case R.id.emoji6:
                        emojiAdapter.setEmojis(EmojiRescourse.getEmojiList(6));
                        emojiAdapter.notifyDataSetChanged();
                        break;
                    case R.id.emoji7:
                        emojiAdapter.setEmojis(EmojiRescourse.getEmojiList(7));
                        emojiAdapter.notifyDataSetChanged();
                        break;
                }

            }
        });

        return view;
    }

}
