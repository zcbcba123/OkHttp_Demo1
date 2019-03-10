package com.love.flower.okhttp_demo1;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

import static com.love.flower.okhttp_demo1.PermisionUtils.verifyStoragePermissions;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
//    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/xml;charset=utf-8");
    private static final MediaType MEDIA_TYPE_FORM= MediaType.parse("multipart/form-data");
    private static final MediaType MEDIA_TYPE_ALL = MediaType.parse("application/octet-stream");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//    private static final MediaType MEDIA_TYPE_ALL = MediaType.parse("text/plain");
    private TextView tv;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv_main);
        verifyStoragePermissions(this);
    }

    public void main(View view){
//        get_demo();
//        post_demo();
//        post_sink_demo();
//        post_file_demo2();
//        post_png_demo2();
//        post_json_demo();
//        post_formbody_demo();
        post_down_demo();


    }

    private void post_down_demo() {
            File fpng = new File(Environment.getExternalStorageDirectory(),"zs");
            File f = new File(Environment.getExternalStorageDirectory(),"");
            if (!f.exists()){
                return;
            }
            File file = new File(f, "Statstic.txt");
            File filepng2 = new File(fpng, "test.png");
            //        String url = "https://api.github.com/markdown/raw";
            String url = "http://192.168.1.101:8080/customer/listdown";
//            okHttpClient.cookieJar(new CookieJar());
//        File file = new File("/storage/emulated/0/shumei.txt");


            RequestBody filebody = RequestBody.create(MEDIA_TYPE_ALL, file);
            RequestBody filepng = RequestBody.create(MEDIA_TYPE_ALL, filepng2);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("qq","dd")
                    .addFormDataPart("qq2","dd2")
                    .addFormDataPart("file", "2.txt", filebody)
                    .addFormDataPart("file", "1.png", filepng)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .post(body)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                    Log.d(TAG, "onResponse: " + response.body().string());
                }
            });
    }
    public void CacheResponse(File cacheDirectory) throws Exception {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(cacheDirectory, cacheSize);

        okHttpClient = new OkHttpClient();
//        okHttpClient.setCache(cache);
    }

    private void post_formbody_demo() {
        OkHttpClient okHttpClient=new OkHttpClient();
        String url="http://192.168.1.101:8080/customer/listform";
        JSONObject j = new JSONObject();
        try {
            j.put("ss", "ff");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = j.toString();
        RequestBody body= new FormBody.Builder()
                .add("tt","ee")
                .add("tt2","ee2")
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println();
            }
        });

    }

    private void post_json_demo() {
        OkHttpClient okHttpClient=new OkHttpClient();
        String url="http://192.168.1.101:8080/customer/listjson";
        JSONObject j = new JSONObject();
        try {
            j.put("ss", "ff");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = j.toString();
        RequestBody body=RequestBody.create(MEDIA_TYPE_JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println();
            }
        });
    }

    private void post_png_demo2() {
        if (hasSdcard()) {
            File fpng = new File(Environment.getExternalStorageDirectory(),"zs");
            File f = new File(Environment.getExternalStorageDirectory(),"");
            if (!f.exists()){
                return;
            }
            File file = new File(f, "Statstic.txt");
            File filepng2 = new File(fpng, "test.png");
            //        String url = "https://api.github.com/markdown/raw";
            String url = "http://192.168.1.101:8080/customer/listpng";
            OkHttpClient okHttpClient = new OkHttpClient();
//            okHttpClient.cookieJar(new CookieJar());
//        File file = new File("/storage/emulated/0/shumei.txt");
            RequestBody filebody = RequestBody.create(MEDIA_TYPE_ALL, file);
            RequestBody filepng = RequestBody.create(MEDIA_TYPE_ALL, filepng2);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("qq","dd")
                    .addFormDataPart("qq2","dd2")
                    .addFormDataPart("file", "2.txt", filebody)
                    .addFormDataPart("file", "1.png", filepng)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                    Log.d(TAG, "onResponse: " + response.body().string());
                }
            });
        }

    }

    private void post_file_demo2() {
//        String url = "https://api.github.com/markdown/raw";
        String url = "http://192.168.1.101:8080/customer/listfile";
        OkHttpClient okHttpClient = new OkHttpClient();
        File file = new File("/storage/emulated/0/shumei.txt");
        RequestBody filebody=RequestBody.create(MEDIA_TYPE_FORM,file);
        RequestBody body=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(String.valueOf(MEDIA_TYPE_FORM),"shumei",filebody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }



    private void post_sink_demo() {
//                String url = "https://api.github.com/markdown/raw";
        String url = "http://192.168.1.101:8080/customer/list";
        OkHttpClient okHttpClient = new OkHttpClient();
//        String postBody="ceshi";
//        RequestBody postbody=RequestBody.create(MEDIA_TYPE_MARKDOWN,postBody);

        RequestBody postbody=new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
//                sink.writeUtf8("dd");
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }


            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };
        Request request=new Request.Builder()
                .url(url)
                .post(postbody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                ResponseBody body = response.body();
                final String string = body.string();
                Log.d(TAG, "onResponse: " + string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv.setText(string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void post_demo() {
//        String url = "https://api.github.com/markdown/raw";
        String url = "http://192.168.1.101:8080/customer/listbody";
        OkHttpClient okHttpClient = new OkHttpClient();
        String postBody="ceshi";
        RequestBody postbody=RequestBody.create(MEDIA_TYPE_MARKDOWN,postBody);
        Request request=new Request.Builder()
                .url(url)
                .post(postbody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                ResponseBody body = response.body();
                final String string = body.string();
                Log.d(TAG, "onResponse: " + string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv.setText(string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }); 
    }

    private void get_demo() {
        String url = "http://192.168.1.101:8080/customer/list?username=444";
//        String url = "http://publicobject.com/helloworld.txt";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                ResponseBody body = response.body();
                final String string = body.string();
                Log.d(TAG, "onResponse: " + string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv.setText(string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public boolean hasSdcard(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else {
            return false;
        }
    }
}
