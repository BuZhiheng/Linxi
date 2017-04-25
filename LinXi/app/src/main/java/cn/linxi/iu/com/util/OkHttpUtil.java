package cn.linxi.iu.com.util;
import android.content.Context;
import android.util.Log;

import org.xutils.common.util.MD5;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.linxi.iu.com.LXApplication;
import cn.linxi.iu.com.model.CommonCode;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by BuZhiheng on 2016/7/7.
 * Desc 网络请求框架,使用OkHttp3.0 + rxjava
 */
public class OkHttpUtil {
    private static OkHttpClient client;
    public static ExecutorService executor = Executors.newCachedThreadPool();
    //限制系统中执行线程的数量。
    public static Subscription get(final String url, Subscriber<String> sub){
        Log.i(">>>", url);
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final Request request = new Request
                        .Builder()
                        .url(url)
                        .build();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response resp = client.newCall(request).execute();
                            if (resp.isSuccessful()) {
                                subscriber.onNext(resp.body().string());
                                subscriber.onCompleted();
                            } else {
                                subscriber.onError(null);
                            }
                        } catch (IOException e) {
                            subscriber.onError(null);
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sub);
    }
    public static Subscription post(final String url, final RequestBody requestBody, Subscriber<String> sub){
        final String finalUrl;
        if (url.contains("?")){//................无语,扫码接口，二维码已经带有"?"字符
            finalUrl = url + getSign().replace("?", "&");
        } else {
            finalUrl = url + getSign();
        }
        Log.i(">>>", finalUrl);
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final Request request = new Request
                        .Builder()
                        .url(finalUrl)
                        .post(requestBody)
                        .build();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response resp = client.newCall(request).execute();
                            if (resp.isSuccessful()) {
                                subscriber.onNext(resp.body().string());
                                subscriber.onCompleted();
                            } else {
                                subscriber.onError(null);
                            }
                        } catch (IOException e) {
                            subscriber.onError(null);
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sub);
    }
    public static String getSign(){
        long timestamp = System.currentTimeMillis();
        String ver = WindowUtil.getAppVersionName();
        int random = WindowUtil.getRandom();
        String sign = MD5.md5(timestamp + ver + random + CommonCode.APP_KEY);
        String s = "?timestamp="+timestamp+"&ver="+ver+"&random="+random+"&sign="+sign;
        return s;
    }
    public static String getCodeSign(String phone){
        long timestamp = System.currentTimeMillis();
        String ver = WindowUtil.getAppVersionName();
        int random = WindowUtil.getRandom();
        String sign = MD5.md5(timestamp + ver + random + CommonCode.APP_KEY + phone);
        String s = "?timestamp="+timestamp+"&ver="+ver+"&random="+random+"&sign="+sign;
        return s;
    }
    public static void initHttps(Context context) {
//        client = new OkHttpClient();
//        final InputStream inputStream;
//        SSLSocketFactory sslSocketFactory = null;
//        X509TrustManager trustManager;
//        try {
//            //https://kyfw.12306.cn/otn/
//            inputStream = context.getAssets().open("srca.cer"); // 得到证书的输入流
//            trustManager = trustManagerForCertificates(inputStream);//以流的方式读入证书
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{trustManager}, null);
//            sslSocketFactory = sslContext.getSocketFactory();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        client = new OkHttpClient.Builder()
//                .sslSocketFactory(sslSocketFactory)
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .build();
    }
    public static X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends java.security.cert.Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }
        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (java.security.cert.Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }
        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }
    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // 这里添加自定义的密码，默认
            InputStream in = null;
            // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}