package cn.linxi.iu.com.model;
/**
 * Created by buzhiheng on 2016/9/13.
 */
public class WXPayToken {
    public Token token;
    public class Token{
        public String appid;
        public String partnerid;
        public String prepayid;
        public String noncestr;
        public String timestamp;
        public String sign;
    }
}