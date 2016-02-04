package com.loopj.android.http;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonHttpResponseHandler extends TextHttpResponseHandler {
    boolean useRFC5179CompatibilityMode;

    /* renamed from: com.loopj.android.http.JsonHttpResponseHandler.1 */
    class C04791 implements Runnable {
        final /* synthetic */ Header[] val$headers;
        final /* synthetic */ byte[] val$responseBytes;
        final /* synthetic */ int val$statusCode;

        /* renamed from: com.loopj.android.http.JsonHttpResponseHandler.1.1 */
        class C04771 implements Runnable {
            final /* synthetic */ Object val$jsonResponse;

            C04771(Object obj) {
                this.val$jsonResponse = obj;
            }

            public final void run() {
                if (!JsonHttpResponseHandler.this.useRFC5179CompatibilityMode && this.val$jsonResponse == null) {
                    JsonHttpResponseHandler.this.onSuccess$79de7b53(null);
                } else if (this.val$jsonResponse instanceof JSONObject) {
                    JsonHttpResponseHandler.this.onSuccess(C04791.this.val$statusCode, C04791.this.val$headers, (JSONObject) this.val$jsonResponse);
                } else if (this.val$jsonResponse instanceof JSONArray) {
                    AsyncHttpClient.log.m20w("JsonHttpRH", "onSuccess(int, Header[], JSONArray) was not overriden, but callback was received");
                } else if (!(this.val$jsonResponse instanceof String)) {
                    JsonHttpResponseHandler.onFailure$6af5acc(new JSONException("Unexpected response type " + this.val$jsonResponse.getClass().getName()));
                } else if (JsonHttpResponseHandler.this.useRFC5179CompatibilityMode) {
                    JsonHttpResponseHandler.this.onFailure(C04791.this.val$statusCode, C04791.this.val$headers, (String) this.val$jsonResponse, new JSONException("Response cannot be parsed as JSON data"));
                } else {
                    JsonHttpResponseHandler.this.onSuccess$79de7b53((String) this.val$jsonResponse);
                }
            }
        }

        /* renamed from: com.loopj.android.http.JsonHttpResponseHandler.1.2 */
        class C04782 implements Runnable {
            final /* synthetic */ JSONException val$ex;

            C04782(JSONException jSONException) {
                this.val$ex = jSONException;
            }

            public final void run() {
                JsonHttpResponseHandler.onFailure$6af5acc(this.val$ex);
            }
        }

        C04791(byte[] bArr, int i, Header[] headerArr) {
            this.val$responseBytes = bArr;
            this.val$statusCode = i;
            this.val$headers = headerArr;
        }

        public final void run() {
            try {
                JsonHttpResponseHandler.this.postRunnable(new C04771(JsonHttpResponseHandler.this.parseResponse(this.val$responseBytes)));
            } catch (JSONException ex) {
                JsonHttpResponseHandler.this.postRunnable(new C04782(ex));
            }
        }
    }

    /* renamed from: com.loopj.android.http.JsonHttpResponseHandler.2 */
    class C04822 implements Runnable {
        final /* synthetic */ Header[] val$headers;
        final /* synthetic */ byte[] val$responseBytes;
        final /* synthetic */ int val$statusCode;
        final /* synthetic */ Throwable val$throwable;

        /* renamed from: com.loopj.android.http.JsonHttpResponseHandler.2.1 */
        class C04801 implements Runnable {
            final /* synthetic */ Object val$jsonResponse;

            C04801(Object obj) {
                this.val$jsonResponse = obj;
            }

            public final void run() {
                if (!JsonHttpResponseHandler.this.useRFC5179CompatibilityMode && this.val$jsonResponse == null) {
                    JsonHttpResponseHandler.this.onFailure(C04822.this.val$statusCode, C04822.this.val$headers, null, C04822.this.val$throwable);
                } else if (this.val$jsonResponse instanceof JSONObject) {
                    JsonHttpResponseHandler.onFailure$6af5acc(C04822.this.val$throwable);
                } else if (this.val$jsonResponse instanceof JSONArray) {
                    AsyncHttpClient.log.m21w("JsonHttpRH", "onFailure(int, Header[], Throwable, JSONArray) was not overriden, but callback was received", C04822.this.val$throwable);
                } else if (this.val$jsonResponse instanceof String) {
                    JsonHttpResponseHandler.this.onFailure(C04822.this.val$statusCode, C04822.this.val$headers, (String) this.val$jsonResponse, C04822.this.val$throwable);
                } else {
                    JsonHttpResponseHandler.onFailure$6af5acc(new JSONException("Unexpected response type " + this.val$jsonResponse.getClass().getName()));
                }
            }
        }

        /* renamed from: com.loopj.android.http.JsonHttpResponseHandler.2.2 */
        class C04812 implements Runnable {
            final /* synthetic */ JSONException val$ex;

            C04812(JSONException jSONException) {
                this.val$ex = jSONException;
            }

            public final void run() {
                JsonHttpResponseHandler.onFailure$6af5acc(this.val$ex);
            }
        }

        C04822(byte[] bArr, int i, Header[] headerArr, Throwable th) {
            this.val$responseBytes = bArr;
            this.val$statusCode = i;
            this.val$headers = headerArr;
            this.val$throwable = th;
        }

        public final void run() {
            try {
                JsonHttpResponseHandler.this.postRunnable(new C04801(JsonHttpResponseHandler.this.parseResponse(this.val$responseBytes)));
            } catch (JSONException ex) {
                JsonHttpResponseHandler.this.postRunnable(new C04812(ex));
            }
        }
    }

    public JsonHttpResponseHandler() {
        super("UTF-8");
        this.useRFC5179CompatibilityMode = true;
    }

    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        AsyncHttpClient.log.m20w("JsonHttpRH", "onSuccess(int, Header[], JSONObject) was not overriden, but callback was received");
    }

    public static void onFailure$6af5acc(Throwable throwable) {
        AsyncHttpClient.log.m21w("JsonHttpRH", "onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received", throwable);
    }

    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        AsyncHttpClient.log.m21w("JsonHttpRH", "onFailure(int, Header[], String, Throwable) was not overriden, but callback was received", throwable);
    }

    public final void onSuccess$79de7b53(String responseString) {
        AsyncHttpClient.log.m20w("JsonHttpRH", "onSuccess(int, Header[], String) was not overriden, but callback was received");
    }

    public final void onSuccess(int statusCode, Header[] headers, byte[] responseBytes) {
        if (statusCode != 204) {
            Runnable parser = new C04791(responseBytes, statusCode, headers);
            if (this.useSynchronousMode || this.usePoolThread) {
                parser.run();
                return;
            } else {
                new Thread(parser).start();
                return;
            }
        }
        onSuccess(statusCode, headers, new JSONObject());
    }

    public final void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
        if (responseBytes != null) {
            Runnable parser = new C04822(responseBytes, statusCode, headers, throwable);
            if (this.useSynchronousMode || this.usePoolThread) {
                parser.run();
                return;
            } else {
                new Thread(parser).start();
                return;
            }
        }
        AsyncHttpClient.log.m19v("JsonHttpRH", "response body is null, calling onFailure(Throwable, JSONObject)");
        onFailure$6af5acc(throwable);
    }

    protected final Object parseResponse(byte[] responseBody) throws JSONException {
        if (responseBody == null) {
            return null;
        }
        Object result = null;
        String jsonString = TextHttpResponseHandler.getResponseString(responseBody, getCharset());
        if (jsonString != null) {
            jsonString = jsonString.trim();
            if (this.useRFC5179CompatibilityMode) {
                if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
                    result = new JSONTokener(jsonString).nextValue();
                }
            } else if ((jsonString.startsWith("{") && jsonString.endsWith("}")) || (jsonString.startsWith("[") && jsonString.endsWith("]"))) {
                result = new JSONTokener(jsonString).nextValue();
            } else if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
                result = jsonString.substring(1, jsonString.length() - 1);
            }
        }
        if (result == null) {
            return jsonString;
        }
        return result;
    }
}
