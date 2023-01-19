package com.vchatcloud.androidsample.resourse;

import com.vchatcloud.androidsample.R;

import org.json.JSONException;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EmojiRescourse {

    public static int emojiInfo(String mainIndex, int subIndex) {
        switch (mainIndex) {
            case "emo01":
                return EmojiRescourse.emojiData1[subIndex];
            case "emo02":
                return EmojiRescourse.emojiData2[subIndex];
            case "emo03":
                return EmojiRescourse.emojiData3[subIndex];
            case "emo04":
                return EmojiRescourse.emojiData4[subIndex];
            case "emo05":
                return EmojiRescourse.emojiData5[subIndex];
            case "emo06":
                return EmojiRescourse.emojiData6[subIndex];
            case "emo07":
                return EmojiRescourse.emojiData7[subIndex];
            default:
                return EmojiRescourse.emojiData1[0];
        }
    }

    public static JSONObject getUrlParse(String url) {
        JSONObject json = new JSONObject();
        String lastStr = url.substring(url.lastIndexOf("/") + 1);
        if (lastStr == null) {
            return json;
        }
        try {
            json.put("mainIndex", lastStr.substring(0, lastStr.indexOf("_")));
            json.put("subIndex", Integer.parseInt(lastStr.substring(lastStr.indexOf("_") + 1, lastStr.indexOf("."))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static int getUrlToEmojiInfo(String url) {
        JSONObject emojiInfo = getUrlParse(url);
        return emojiInfo((String) emojiInfo.get("mainIndex"), ((int) emojiInfo.get("subIndex")) < 1 ? 0 : ((int) emojiInfo.get("subIndex")) -1);
    }

    // 내부 아이콘 리소스
    private static int[] emojiData1 = new int[] {
            R.drawable.img__emoticon_emo01_emo01_001
            ,R.drawable.img__emoticon_emo01_emo01_002
            ,R.drawable.img__emoticon_emo01_emo01_003
            ,R.drawable.img__emoticon_emo01_emo01_004
            ,R.drawable.img__emoticon_emo01_emo01_005
            ,R.drawable.img__emoticon_emo01_emo01_006
            ,R.drawable.img__emoticon_emo01_emo01_007
            ,R.drawable.img__emoticon_emo01_emo01_008
            ,R.drawable.img__emoticon_emo01_emo01_009
            ,R.drawable.img__emoticon_emo01_emo01_010
            ,R.drawable.img__emoticon_emo01_emo01_011
            ,R.drawable.img__emoticon_emo01_emo01_012
            ,R.drawable.img__emoticon_emo01_emo01_013
            ,R.drawable.img__emoticon_emo01_emo01_014
            ,R.drawable.img__emoticon_emo01_emo01_015
            ,R.drawable.img__emoticon_emo01_emo01_016
            ,R.drawable.img__emoticon_emo01_emo01_017
            ,R.drawable.img__emoticon_emo01_emo01_018
            ,R.drawable.img__emoticon_emo01_emo01_019
            ,R.drawable.img__emoticon_emo01_emo01_020
    };
    private static int[] emojiData2 = new int[] {
            R.drawable.img__emoticon_emo02_emo02_001
            ,R.drawable.img__emoticon_emo02_emo02_002
            ,R.drawable.img__emoticon_emo02_emo02_003
            ,R.drawable.img__emoticon_emo02_emo02_004
            ,R.drawable.img__emoticon_emo02_emo02_005
            ,R.drawable.img__emoticon_emo02_emo02_006
            ,R.drawable.img__emoticon_emo02_emo02_007
            ,R.drawable.img__emoticon_emo02_emo02_008
            ,R.drawable.img__emoticon_emo02_emo02_009
            ,R.drawable.img__emoticon_emo02_emo02_010
            ,R.drawable.img__emoticon_emo02_emo02_011
            ,R.drawable.img__emoticon_emo02_emo02_012
            ,R.drawable.img__emoticon_emo02_emo02_013
            ,R.drawable.img__emoticon_emo02_emo02_014
            ,R.drawable.img__emoticon_emo02_emo02_015
            ,R.drawable.img__emoticon_emo02_emo02_016
            ,R.drawable.img__emoticon_emo02_emo02_017
            ,R.drawable.img__emoticon_emo02_emo02_018
    };
    private static int[] emojiData3 = new int[] {
            R.drawable.img__emoticon_emo03_emo03_001
            ,R.drawable.img__emoticon_emo03_emo03_002
            ,R.drawable.img__emoticon_emo03_emo03_003
            ,R.drawable.img__emoticon_emo03_emo03_004
            ,R.drawable.img__emoticon_emo03_emo03_005
            ,R.drawable.img__emoticon_emo03_emo03_006
            ,R.drawable.img__emoticon_emo03_emo03_007
            ,R.drawable.img__emoticon_emo03_emo03_008
            ,R.drawable.img__emoticon_emo03_emo03_009
            ,R.drawable.img__emoticon_emo03_emo03_010
            ,R.drawable.img__emoticon_emo03_emo03_011
            ,R.drawable.img__emoticon_emo03_emo03_012
    };
    private static int[] emojiData4 = new int[] {
            R.drawable.img__emoticon_emo04_emo04_001
            ,R.drawable.img__emoticon_emo04_emo04_002
            ,R.drawable.img__emoticon_emo04_emo04_003
            ,R.drawable.img__emoticon_emo04_emo04_004
            ,R.drawable.img__emoticon_emo04_emo04_005
            ,R.drawable.img__emoticon_emo04_emo04_006
            ,R.drawable.img__emoticon_emo04_emo04_007
            ,R.drawable.img__emoticon_emo04_emo04_008
            ,R.drawable.img__emoticon_emo04_emo04_009
            ,R.drawable.img__emoticon_emo04_emo04_010
            ,R.drawable.img__emoticon_emo04_emo04_011
            ,R.drawable.img__emoticon_emo04_emo04_012
            ,R.drawable.img__emoticon_emo04_emo04_013
            ,R.drawable.img__emoticon_emo04_emo04_014
            ,R.drawable.img__emoticon_emo04_emo04_015
            ,R.drawable.img__emoticon_emo04_emo04_016
            ,R.drawable.img__emoticon_emo04_emo04_017
            ,R.drawable.img__emoticon_emo04_emo04_018
            ,R.drawable.img__emoticon_emo04_emo04_019
            ,R.drawable.img__emoticon_emo04_emo04_020
    };
    private static int[] emojiData5 = new int[] {
            R.drawable.img__emoticon_emo05_emo05_001
            ,R.drawable.img__emoticon_emo05_emo05_002
            ,R.drawable.img__emoticon_emo05_emo05_003
            ,R.drawable.img__emoticon_emo05_emo05_004
            ,R.drawable.img__emoticon_emo05_emo05_005
            ,R.drawable.img__emoticon_emo05_emo05_006
            ,R.drawable.img__emoticon_emo05_emo05_007
            ,R.drawable.img__emoticon_emo05_emo05_008
            ,R.drawable.img__emoticon_emo05_emo05_009
            ,R.drawable.img__emoticon_emo05_emo05_010
            ,R.drawable.img__emoticon_emo05_emo05_011
            ,R.drawable.img__emoticon_emo05_emo05_012
            ,R.drawable.img__emoticon_emo05_emo05_013
            ,R.drawable.img__emoticon_emo05_emo05_014
            ,R.drawable.img__emoticon_emo05_emo05_015
            ,R.drawable.img__emoticon_emo05_emo05_016
            ,R.drawable.img__emoticon_emo05_emo05_017
            ,R.drawable.img__emoticon_emo05_emo05_018
    };
    private static int[] emojiData6 = new int[] {
            R.drawable.img__emoticon_emo06_emo06_001
            ,R.drawable.img__emoticon_emo06_emo06_002
            ,R.drawable.img__emoticon_emo06_emo06_003
            ,R.drawable.img__emoticon_emo06_emo06_004
            ,R.drawable.img__emoticon_emo06_emo06_005
            ,R.drawable.img__emoticon_emo06_emo06_006
            ,R.drawable.img__emoticon_emo06_emo06_007
            ,R.drawable.img__emoticon_emo06_emo06_008
            ,R.drawable.img__emoticon_emo06_emo06_009
            ,R.drawable.img__emoticon_emo06_emo06_010
    };
    private static int[] emojiData7 = new int[] {
            R.drawable.img__emoticon_emo07_emo07_001
            ,R.drawable.img__emoticon_emo07_emo07_002
            ,R.drawable.img__emoticon_emo07_emo07_003
            ,R.drawable.img__emoticon_emo07_emo07_004
            ,R.drawable.img__emoticon_emo07_emo07_005
            ,R.drawable.img__emoticon_emo07_emo07_006
            ,R.drawable.img__emoticon_emo07_emo07_007
            ,R.drawable.img__emoticon_emo07_emo07_008
            ,R.drawable.img__emoticon_emo07_emo07_009
            ,R.drawable.img__emoticon_emo07_emo07_010
            ,R.drawable.img__emoticon_emo07_emo07_011
            ,R.drawable.img__emoticon_emo07_emo07_012
            ,R.drawable.img__emoticon_emo07_emo07_013
            ,R.drawable.img__emoticon_emo07_emo07_014
            ,R.drawable.img__emoticon_emo07_emo07_015
            ,R.drawable.img__emoticon_emo07_emo07_016
            ,R.drawable.img__emoticon_emo07_emo07_017
            ,R.drawable.img__emoticon_emo07_emo07_018
            ,R.drawable.img__emoticon_emo07_emo07_019
            ,R.drawable.img__emoticon_emo07_emo07_020
            ,R.drawable.img__emoticon_emo07_emo07_021
            ,R.drawable.img__emoticon_emo07_emo07_022
            ,R.drawable.img__emoticon_emo07_emo07_023
            ,R.drawable.img__emoticon_emo07_emo07_024
    };

    public static org.json.JSONObject getEmojiList(int i) {
        org.json.JSONObject json = new org.json.JSONObject();
        int[] data;

        switch (i) {
            case 1:
                data = emojiData1;
                break;
            case 2:
                data = emojiData2;
                break;
            case 3:
                data = emojiData3;
                break;
            case 4:
                data = emojiData4;
                break;
            case 5:
                data = emojiData5;
                break;
            case 6:
                data = emojiData6;
                break;
            case 7:
                data = emojiData7;
                break;
            default:
                data = emojiData1;
        }

        try {
            json.put("data", Arrays.stream(data).boxed().collect(Collectors.toList()));
            json.put("start", "img/emoticon/");
            json.put("item", "emo0" + i);
            json.put("end", ".png");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
