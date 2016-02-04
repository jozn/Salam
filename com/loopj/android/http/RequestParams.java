package com.loopj.android.http;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class RequestParams implements Serializable {
    protected String contentEncoding;
    protected String elapsedFieldInJsonStreamer;
    protected final ConcurrentHashMap<String, List<FileWrapper>> fileArrayParams;
    protected final ConcurrentHashMap<String, FileWrapper> fileParams;
    protected boolean forceMultipartEntity;
    protected boolean isRepeatable;
    protected final ConcurrentHashMap<String, StreamWrapper> streamParams;
    protected final ConcurrentHashMap<String, String> urlParams;
    protected final ConcurrentHashMap<String, Object> urlParamsWithObjects;
    protected boolean useJsonStreamer;

    /* renamed from: com.loopj.android.http.RequestParams.1 */
    class C04831 extends HashMap<String, String> {
        final /* synthetic */ String val$key;
        final /* synthetic */ String val$value;

        C04831(String str, String str2) {
            this.val$key = str;
            this.val$value = str2;
            put(this.val$key, this.val$value);
        }
    }

    public static class FileWrapper implements Serializable {
        public final String contentType;
        public final String customFileName;
        public final File file;

        public FileWrapper(File file) {
            this.file = file;
            this.contentType = null;
            this.customFileName = null;
        }
    }

    public static class StreamWrapper {
        public final boolean autoClose;
        public final String contentType;
        public final InputStream inputStream;
        public final String name;

        public StreamWrapper(InputStream inputStream, String name, String contentType, boolean autoClose) {
            this.inputStream = inputStream;
            this.name = name;
            this.contentType = contentType;
            this.autoClose = autoClose;
        }
    }

    public RequestParams() {
        this(null);
    }

    private RequestParams(Map<String, String> source) {
        this.urlParams = new ConcurrentHashMap();
        this.streamParams = new ConcurrentHashMap();
        this.fileParams = new ConcurrentHashMap();
        this.fileArrayParams = new ConcurrentHashMap();
        this.urlParamsWithObjects = new ConcurrentHashMap();
        this.forceMultipartEntity = false;
        this.elapsedFieldInJsonStreamer = "_elapsed";
        this.contentEncoding = "UTF-8";
        if (source != null) {
            for (Entry<String, String> entry : source.entrySet()) {
                put((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    public RequestParams(String key, String value) {
        this(new C04831(key, value));
    }

    public final void put(String key, String value) {
        if (key != null && value != null) {
            this.urlParams.put(key, value);
        }
    }

    public final void put(String key, File file) throws FileNotFoundException {
        if (file.exists()) {
            this.fileParams.put(key, new FileWrapper(file));
            return;
        }
        throw new FileNotFoundException();
    }

    public final String toString() {
        StringBuilder result = new StringBuilder();
        for (Entry<String, String> entry : this.urlParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append((String) entry.getKey());
            result.append("=");
            result.append((String) entry.getValue());
        }
        for (Entry<String, StreamWrapper> entry2 : this.streamParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append((String) entry2.getKey());
            result.append("=");
            result.append("STREAM");
        }
        for (Entry<String, FileWrapper> entry3 : this.fileParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append((String) entry3.getKey());
            result.append("=");
            result.append("FILE");
        }
        for (Entry<String, List<FileWrapper>> entry4 : this.fileArrayParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append((String) entry4.getKey());
            result.append("=");
            result.append("FILES(SIZE=").append(((List) entry4.getValue()).size()).append(")");
        }
        for (BasicNameValuePair kv : getParamsList(null, this.urlParamsWithObjects)) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(kv.name);
            result.append("=");
            result.append(kv.value);
        }
        return result.toString();
    }

    public final HttpEntity getEntity(ResponseHandlerInterface progressHandler) throws IOException {
        StreamWrapper streamWrapper;
        String str;
        if (this.useJsonStreamer) {
            boolean z = (this.fileParams.isEmpty() && this.streamParams.isEmpty()) ? false : true;
            JsonStreamerEntity jsonStreamerEntity = new JsonStreamerEntity(progressHandler, z, this.elapsedFieldInJsonStreamer);
            for (Entry entry : this.urlParams.entrySet()) {
                jsonStreamerEntity.addPart((String) entry.getKey(), entry.getValue());
            }
            for (Entry entry2 : this.urlParamsWithObjects.entrySet()) {
                jsonStreamerEntity.addPart((String) entry2.getKey(), entry2.getValue());
            }
            for (Entry entry22 : this.fileParams.entrySet()) {
                jsonStreamerEntity.addPart((String) entry22.getKey(), entry22.getValue());
            }
            for (Entry entry222 : this.streamParams.entrySet()) {
                streamWrapper = (StreamWrapper) entry222.getValue();
                if (streamWrapper.inputStream != null) {
                    str = (String) entry222.getKey();
                    InputStream inputStream = streamWrapper.inputStream;
                    String str2 = streamWrapper.name;
                    String str3 = streamWrapper.contentType;
                    jsonStreamerEntity.addPart(str, new StreamWrapper(inputStream, str2, str3 == null ? "application/octet-stream" : str3, streamWrapper.autoClose));
                }
            }
            return jsonStreamerEntity;
        } else if (!this.forceMultipartEntity && this.streamParams.isEmpty() && this.fileParams.isEmpty() && this.fileArrayParams.isEmpty()) {
            return createFormEntity();
        } else {
            FileWrapper fileWrapper;
            SimpleMultipartEntity simpleMultipartEntity = new SimpleMultipartEntity(progressHandler);
            simpleMultipartEntity.isRepeatable = this.isRepeatable;
            for (Entry entry2222 : this.urlParams.entrySet()) {
                simpleMultipartEntity.addPartWithCharset((String) entry2222.getKey(), (String) entry2222.getValue(), this.contentEncoding);
            }
            for (BasicNameValuePair basicNameValuePair : getParamsList(null, this.urlParamsWithObjects)) {
                simpleMultipartEntity.addPartWithCharset(basicNameValuePair.name, basicNameValuePair.value, this.contentEncoding);
            }
            for (Entry entry22222 : this.streamParams.entrySet()) {
                streamWrapper = (StreamWrapper) entry22222.getValue();
                if (streamWrapper.inputStream != null) {
                    str = (String) entry22222.getKey();
                    String str4 = streamWrapper.name;
                    InputStream inputStream2 = streamWrapper.inputStream;
                    String str5 = streamWrapper.contentType;
                    simpleMultipartEntity.out.write(simpleMultipartEntity.boundaryLine);
                    simpleMultipartEntity.out.write(SimpleMultipartEntity.createContentDisposition(str, str4));
                    simpleMultipartEntity.out.write(SimpleMultipartEntity.createContentType(str5));
                    simpleMultipartEntity.out.write(SimpleMultipartEntity.TRANSFER_ENCODING_BINARY);
                    simpleMultipartEntity.out.write(SimpleMultipartEntity.CR_LF);
                    byte[] bArr = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
                    while (true) {
                        int read = inputStream2.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        simpleMultipartEntity.out.write(bArr, 0, read);
                    }
                    simpleMultipartEntity.out.write(SimpleMultipartEntity.CR_LF);
                    simpleMultipartEntity.out.flush();
                }
            }
            for (Entry entry222222 : this.fileParams.entrySet()) {
                fileWrapper = (FileWrapper) entry222222.getValue();
                simpleMultipartEntity.addPart((String) entry222222.getKey(), fileWrapper.file, fileWrapper.contentType, fileWrapper.customFileName);
            }
            for (Entry entry2222222 : this.fileArrayParams.entrySet()) {
                for (FileWrapper fileWrapper2 : (List) entry2222222.getValue()) {
                    simpleMultipartEntity.addPart((String) entry2222222.getKey(), fileWrapper2.file, fileWrapper2.contentType, fileWrapper2.customFileName);
                }
            }
            return simpleMultipartEntity;
        }
    }

    private HttpEntity createFormEntity() {
        try {
            return new UrlEncodedFormEntity(getParamsList(), this.contentEncoding);
        } catch (UnsupportedEncodingException e) {
            AsyncHttpClient.log.m17e("RequestParams", "createFormEntity failed", e);
            return null;
        }
    }

    private List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList();
        for (Entry<String, String> entry : this.urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
        }
        lparams.addAll(getParamsList(null, this.urlParamsWithObjects));
        return lparams;
    }

    private List<BasicNameValuePair> getParamsList(String key, Object value) {
        List<BasicNameValuePair> params = new LinkedList();
        List list;
        Object nestedValue;
        if (value instanceof Map) {
            Map map = (Map) value;
            list = new ArrayList(map.keySet());
            if (list.size() > 0 && (list.get(0) instanceof Comparable)) {
                Collections.sort(list);
            }
            for (Object nestedKey : list) {
                Object nestedKey2;
                if (nestedKey2 instanceof String) {
                    nestedValue = map.get(nestedKey2);
                    if (nestedValue != null) {
                        if (key == null) {
                            nestedKey2 = (String) nestedKey2;
                        } else {
                            nestedKey2 = String.format(Locale.US, "%s[%s]", new Object[]{key, nestedKey2});
                        }
                        params.addAll(getParamsList(nestedKey2, nestedValue));
                    }
                }
            }
        } else if (value instanceof List) {
            list = (List) value;
            int listSize = list.size();
            for (nestedValueIndex = 0; nestedValueIndex < listSize; nestedValueIndex++) {
                params.addAll(getParamsList(String.format(Locale.US, "%s[%d]", new Object[]{key, Integer.valueOf(nestedValueIndex)}), list.get(nestedValueIndex)));
            }
        } else if (value instanceof Object[]) {
            for (Object paramsList : (Object[]) value) {
                params.addAll(getParamsList(String.format(Locale.US, "%s[%d]", new Object[]{key, Integer.valueOf(nestedValueIndex)}), paramsList));
            }
        } else if (value instanceof Set) {
            for (Object nestedValue2 : (Set) value) {
                params.addAll(getParamsList(key, nestedValue2));
            }
        } else {
            params.add(new BasicNameValuePair(key, value.toString()));
        }
        return params;
    }
}
