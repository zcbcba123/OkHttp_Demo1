git //参考网址:https://www.jianshu.com/p/da4a806e599b
导入:implementation 'com.squareup.okhttp3:okhttp:3.10.0'
开启权限:internet (如果使用DiskLruCache还要开启写外存的权限?//)
#1.1异步get:#
String url = "http://wwww.baidu.com";
    OkHttpClient okHttpClient = new OkHttpClient();
    Request request = new Request.Builder()
            .url(url)
            .get()//默认就是可以不写
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
异步发起的请求会被加入到 Dispatcher 中的 runningAsyncCalls双端队列中通过线程池来执行。

#1.2同步get请求:#
String url="https://www.baidu.com";
OkHttpClient okHttpClient = new OkHttpClient();
final Request request = new Request.Builder()
        .url(url)
        .build();
final Call call = okHttpClient.newCall(request);
new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            Response response = call.execute();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}).start();

#2.1POST提交String#
post需要多构造一个RequestBody,并需要指定MediaType用于描述请求/响应，关于MediaType查看:https://tools.ietf.org/html/rfc2045
RequestBody的几种构造方式:
public static RequestBody create(@Nullable MediaType contentType, File file) 
public static RequestBody create(@Nullable MediaType contentType, byte[] content) 
public static RequestBody create(@Nullable MediaType contentType, String content) 
public static RequestBody create(@Nullable MediaType contentType, ByteString content) 
public static RequestBody create(@Nullable MediaType contentType, byte[] content,int offset,int byteCount) 

String url = "https://api.github.com/markdown/raw";
OkHttpClient okHttpClient = new OkHttpClient();
String postBody = "ceshi";
RequestBody postbody = RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody);
Request request = new Request.Builder()
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

#2.2POST方式提交流#
例子1:
String url = "https://api.github.com/markdown/raw";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody postbody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("I am zs");
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };
        Request request = new Request.Builder()
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

例子2:
String url = "https://api.github.com/markdown/raw";
OkHttpClient okHttpClient = new OkHttpClient();
RequestBody postbody = new RequestBody() {
    @Override
    public MediaType contentType() {
        return MEDIA_TYPE_MARKDOWN;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.writeUtf8("Numbers\n");
        sink.writeUtf8("-------\n");
        for (int i = 2; i <= 997; i++) {
            sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));//求因子
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
Request request = new Request.Builder()
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

#POST提交文件#
String url = "https://api.github.com/markdown/raw";
OkHttpClient okHttpClient = new OkHttpClient();
File file = new File("/sdcard/DingTalk/test(2)_3.txt");
RequestBody filebody = RequestBody.create(MEDIA_TYPE_MARKDOWN, file);
Request request = new Request.Builder().url(url)
        .post(filebody).build();
okHttpClient.newCall(request).enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
        Headers headers = response.headers();
        for (int i = 0; i < headers.size(); i++) {
            Log.d(TAG, headers.name(i) + ":" + headers.value(i));
        }
        Log.d(TAG, "onResponse: " + response.body().string());
    }
});

#POST方式提交表单#
//待测试
OkHttpClient okHttpClient = new OkHttpClient();
//        String url = "http://192.168.1.101:8080/customer/listform";
String url="https://en.wikipedia.org/w/index.php";
/* JSONObject j = new JSONObject();
try {
    j.put("ss", "ff");
} catch (JSONException e) {
    e.printStackTrace();
}
String json = j.toString();*/
RequestBody body = new FormBody.Builder()
        .add("search", "ee")
//                .add("tt2", "ee2")
        .build();
Request request = new Request.Builder()
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

//待测试
String url = "https://api.github.com/markdown/raw";
//String url = "http://192.168.1.101:8080/customer/listfile";
OkHttpClient okHttpClient = new OkHttpClient();
File file = new File("/sdcard/DingTalk/test(2)_3.txt");
RequestBody filebody = RequestBody.create(MEDIA_TYPE_FORM, file);
RequestBody body = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(String.valueOf(MEDIA_TYPE_FORM), "shumei", filebody)
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

提交表单时，使用 RequestBody 的实现类FormBody来描述请求体，它可以携带一些经过编码的 key-value 请求体，键值对存储在下面两个集合中：

  private final List<String> encodedNames;
  private final List<String> encodedValues

#POST方式提交分块请求#
//待测试
OkHttpClient client = new OkHttpClient();
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        MultipartBody body = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition","form-data;name=\"title\""),
                        RequestBody.create(null,"Square Logo")
                )
                .addPart(Headers.of("Content-Disposition","form-data;name=\"image\""),
                        RequestBody.create(MEDIA_TYPE_PNG,new File("/sdcard/DCIM/P91112-135221.png")))
                .build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID" + IMGUR_CLIENT_ID)
                .url("https://api.imgur.com/3/image")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });

#拦截器-interceptor#
OkHttp的拦截器链可谓是其整个框架的精髓，用户可传入的 interceptor 分为两类：
①一类是全局的 interceptor，该类 interceptor 在整个拦截器链中最早被调用，通过 OkHttpClient.Builder#addInterceptor(Interceptor) 传入；
②另外一类是非网页请求的 interceptor ，这类拦截器只会在非网页请求中被调用，并且是在组装完请求之后，真正发起网络请求前被调用，所有的 interceptor 被保存在 List<Interceptor> interceptors 集合中，按照添加顺序来逐个调用，具体可参考 RealCall#getResponseWithInterceptorChain() 方法。通过 OkHttpClient.Builder#addNetworkInterceptor(Interceptor) 传入；


/**
     * 拦截每一个原始请求以及耗费时间
     */
    private void post_interceptor_demo_1() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();
        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .header("User-Agent", "OkHttp Example")
                .build();
okHttpClient.newCall(request).enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "onFailure: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ResponseBody body = response.body();
        if (body != null) {
            Log.d(TAG, "onResponse: " + response.body().string());
            body.close();
        }
    }
});

class LoggingInterceptor implements Interceptor {
    private final String TAG="LoggingInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.nanoTime();
        Log.d(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        long endTime = System.nanoTime();
        Log.d(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (endTime - startTime) / 1e6d, response.headers()));
        return response;
    }
}

注意到一点是这个请求做了重定向，原始的 request url 是 http://www.publicobject.com/helloworld.tx，而响应的 request url 是 https://publicobject.com/helloworld.txt，这说明一定发生了重定向，但是做了几次重定向其实我们这里是不知道的，要知道这些的话，可以使用 addNetworkInterceptor()去做。更多的关于 interceptor的使用以及它们各自的优缺点，请移步OkHttp官方说明文档(https://github.com/square/okhttp/wiki/Interceptors)。

拦截器链结构图片:
https://upload-images.jianshu.io/upload_images/3631399-164b722ab35ae9bf.png

IV.自定义dns服务
Okhttp默认情况下使用的是系统的

V.其他
1.推荐让 OkHttpClient 保持单例，用同一个 OkHttpClient 实例来执行你的所有请求，因为每一个 OkHttpClient 实例都拥有自己的连接池和线程池，重用这些资源可以减少延时和节省资源，如果为每个请求创建一个 OkHttpClient 实例，显然就是一种资源的浪费。当然，也可以使用如下的方式来创建一个新的 OkHttpClient 实例，它们共享连接池、线程池和配置信息。

 OkHttpClient eagerClient = client.newBuilder()
        .readTimeout(500, TimeUnit.MILLISECONDS)
        .build();
    Response response = eagerClient.newCall(request).execute();

每一个Call（其实现是RealCall）只能执行一次，否则会报异常，具体参见 RealCall#execute()
