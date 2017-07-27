
package com.HWDTEMPT.tool;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.HWDTEMPT.hwdtempt.NetRegister;
import com.HWDTEMPT.hwdtempt.PersonInfo;
import com.HWDTEMPT.Util.HttpUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class downuser {
    private Context context = null;
    public downuser (Context context)
    {
       
    }
    

    public static boolean updateuser(String username, String phone, String Height, String weight,
            String age, String sex) {
        JSONObject jsObject;

        try {
            jsObject = jsonupdate(username, phone, Height, weight, age, sex);
            if (jsObject.getString("modiy") != null) {
                return true;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public static List<String> dowuserList(String username) {
        ArrayList<String> list = new ArrayList<String>();
        JSONObject jsObject;
        try {
            // phone,Height,weight,age,sex,userNum
            jsObject = jsonlistuser(username);
            list.add(jsObject.getJSONObject("userinfo").getString("phone"));
            list.add(jsObject.getJSONObject("userinfo").getString("tall"));
            list.add(jsObject.getJSONObject("userinfo").getString("weihgt"));
            list.add(jsObject.getJSONObject("userinfo").getString("ecgsex"));
            Log.e("1111111111", list.get(0));
            Log.e("22222", list.get(1));
            Log.e("3333", list.get(2));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    private static JSONObject jsonupdate(String username, String phone, String Height, String weight,
            String age, String sex)
            throws Exception {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("username", username);
        map1.put("phone", phone);
        map1.put("Height", Height);
        map1.put("weight", weight);
        map1.put("age", age);

        String url1 = HttpUtil.BASE_URL + "modifyuser.do?action=updateuser1";
        JSONObject regjsonback = new JSONObject(HttpUtil.postRequest(url1, map1));
        return regjsonback;
        // phone,Height,weight,age,sex,userNum

    }

    public static JSONObject jsonlistuser(String username) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        String url1 = HttpUtil.BASE_URL + "modifyuser.do?action=listuser_andr";
        JSONObject regjsonback = new JSONObject(HttpUtil.postRequest(url1, map));
        return regjsonback;

    }

}
