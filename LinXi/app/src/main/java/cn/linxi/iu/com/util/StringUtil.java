package cn.linxi.iu.com.util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.linxi.iu.com.model.ShoppingCar;

/**
 * Created by buzhiheng on 2016/8/4.
 */
public class StringUtil {
    public static final String EX_EMAIL = "^w+[-+.]w+)*@w+([-.]w+)*.w+([-.]w+)*$";
    public static final String EX_PHONE = "^1[3|4|5|7|8]\\d{9}$";//判断国内电话号码
    public static final String EX_NUMBER_ABC = "^[A-Za-z0-9]+$";//判断密码,字母开头,数字结尾
    public static final String regEx = "^[A-Za-z0-9]+$";
    public static final String EX_CAR_IDENTITY = "^[\u4e00-\u9fa5]{1}[A-Z][A-Z0-9]{5}$";
    public static final String EX_CHINESE = "[\u4e00-\u9fa5]";//检测是否包含中文
    public static final String EX_CARID_SIMPLE = "京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼";

    /**
     * 字符串为空的时候返回true
     **/
    public static boolean isNull(String s){
        if (s == null || "".equals(s)){
            return true;
        } else {
            return false;
        }
    }
    public static boolean isJson(String json){
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean strEX(String str, String ex){
        Pattern pattern = Pattern.compile(ex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    public static boolean strEXChinese(String str, String ex){
        Pattern p = Pattern.compile(ex);
        Matcher m = p.matcher(str);
        return m.find();
    }
    public static boolean isCarId(String id){
        if (isNull(id)){
            return false;
        }
        if (id.length() != 7){
            return false;
        }
        if (!EX_CARID_SIMPLE.contains(id.substring(0,1))){
            return false;
        }
        if (!strEX(id,EX_CAR_IDENTITY)){
            return false;
        }
        return true;
    }
    public static String phoneSetMiddleGone(String phone){
        if (isNull(phone)){
            return "";
        } else {
            if (phone.length()<11){
                return phone;
            } else {
                String sMiddle = phone.substring(3,7);
                return phone.replaceFirst(sMiddle,"****");
            }
        }
    }
    public static boolean isWrongNum(String s){
        if (isNull(s)){
            return true;
        }
        if (s.startsWith(".") || s.endsWith(".")){
            return true;
        }
        if ("0".equals(s)){
            return true;
        }
        return false;
    }
    public static String getShoppingCarJs(ShoppingCar car){
        JSONObject json = new JSONObject();
        try {
            json.put("sid",car.sid);
            json.put("num",car.num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
    public static String getShoppingCarJson(List<ShoppingCar> list){
        if (list != null && list.size() > 0){
            JSONArray array = new JSONArray();
            for (int i=0;i<list.size();i++){
                ShoppingCar car = list.get(i);
                JSONObject json = new JSONObject();
                try {
                    json.put("sid",car.sid);
                    json.put("num",car.num);
                    array.put(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return array.toString();
        }
        return "";
    }
}