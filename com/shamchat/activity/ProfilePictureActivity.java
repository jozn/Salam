package com.shamchat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.path.android.jobqueue.JobManager;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.jobs.UpdatePhoneContactsDBJob;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import de.greenrobot.event.EventBus;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfilePictureActivity extends AppCompatActivity {
    public static int CROPED_IMAGE = 0;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_PICTURE = 1;
    private Button buttonContinue;
    private int currentapiVersion;
    private EditText editTextPassword;
    private EditText editTextShamID;
    private EditText editTextUserName;
    private String email;
    private String imgPath;
    private JobManager jobManager;
    private DisplayImageOptions options;
    private String password;
    private String phoneNumber;
    private byte[] profileImageByteArray;
    private ImageView profilePhotoUpload;
    private User user;
    private Utils utils;

    /* renamed from: com.shamchat.activity.ProfilePictureActivity.1 */
    class C08401 implements OnClickListener {
        C08401() {
        }

        public final void onClick(View v) {
            ProfilePictureActivity.this.selectOrTakePhoto();
        }
    }

    /* renamed from: com.shamchat.activity.ProfilePictureActivity.1HttpAsyncTask */
    class AnonymousClass1HttpAsyncTask extends AsyncTask<String, String, String> {
        JSONArray array;
        JSONObject arrayObject;
        JSONObject main;
        final /* synthetic */ String val$email;
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$profilePicture;
        final /* synthetic */ String val$profilePictureBase64;
        final /* synthetic */ String val$shamID;
        final /* synthetic */ String val$userID;
        final /* synthetic */ String val$userName;

        AnonymousClass1HttpAsyncTask(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
            this.val$profilePicture = str;
            this.val$shamID = str2;
            this.val$userName = str3;
            this.val$profilePictureBase64 = str4;
            this.val$userID = str5;
            this.val$email = str6;
            this.val$password = str7;
            this.main = new JSONObject();
            this.array = new JSONArray();
            this.arrayObject = new JSONObject();
        }

        protected final /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            return doInBackground$4af589aa();
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            String str = (String) obj;
            super.onPostExecute(str);
            if (str != null) {
                try {
                    CharSequence string = new JSONObject(str).getString(NotificationCompatApi21.CATEGORY_STATUS);
                    Log.d(NotificationCompatApi21.CATEGORY_STATUS, string);
                    Log.d("response after updating fields", str);
                    if (string.equals("success")) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("userId", this.val$userID);
                        contentValues.put("chatId", this.val$shamID);
                        contentValues.put("name", this.val$userName);
                        contentValues.put("profileimage_url", this.val$profilePicture);
                        contentValues.put(NotificationCompatApi21.CATEGORY_EMAIL, this.val$email);
                        contentValues.put("mobileNo", ProfilePictureActivity.this.phoneNumber);
                        ProfilePictureActivity.this.getContentResolver().insert(UserProvider.CONTENT_URI_USER, contentValues);
                        contentValues = new ContentValues();
                        contentValues.put(ChatActivity.INTENT_EXTRA_USER_ID, this.val$userID);
                        ProfilePictureActivity.this.getContentResolver().insert(UserProvider.CONTENT_URI_NOTIFICATION, contentValues);
                        Editor edit = PreferenceManager.getDefaultSharedPreferences(ProfilePictureActivity.this).edit();
                        edit.putString("account_jabberID", this.val$userID + "@rabtcdn.com");
                        edit.putString("account_jabberPW", this.val$password);
                        edit.putString("current_user_id", ProfilePictureActivity.this.user.userId);
                        edit.putString("user_password", this.val$password);
                        edit.putString("registration_status", "r_v_l_i");
                        edit.putString("user_mobileNo", ProfilePictureActivity.this.user.mobileNo);
                        edit.apply();
                        ProfilePictureActivity.this.jobManager.addJobInBackground(new UpdatePhoneContactsDBJob());
                        Intent intent = new Intent(ProfilePictureActivity.this, MainWindow.class);
                        intent.putExtra("register", true);
                        intent.setFlags(339738624);
                        ProfilePictureActivity.this.startActivity(intent);
                        ProfilePictureActivity.this.finish();
                        ProgressBarDialogLogin.getInstance().dismiss();
                        return;
                    }
                    try {
                        ProgressBarDialogLogin.getInstance().dismiss();
                    } catch (Exception e) {
                    }
                    Toast.makeText(ProfilePictureActivity.this, string, ProfilePictureActivity.SELECT_PICTURE).show();
                } catch (Exception e2) {
                    try {
                        ProgressBarDialogLogin.getInstance().dismiss();
                    } catch (Exception e3) {
                    }
                    Toast.makeText(ProfilePictureActivity.this, e2.getMessage(), ProfilePictureActivity.SELECT_PICTURE).show();
                    e2.printStackTrace();
                }
            }
        }

        protected final void onPreExecute() {
            super.onPreExecute();
            if (this.val$profilePicture != null) {
                Log.d("profileurl", this.val$profilePicture);
            }
            try {
                this.arrayObject.put("chatId", this.val$shamID);
                this.arrayObject.put(NotificationCompatApi21.CATEGORY_EMAIL, BuildConfig.VERSION_NAME);
                this.arrayObject.put("userName", this.val$userName);
                this.arrayObject.put("profileImageBytes", this.val$profilePictureBase64);
                this.arrayObject.put("profileImageUrl", this.val$profilePicture);
                this.arrayObject.put("mobileNo", ProfilePictureActivity.this.phoneNumber);
                this.main.put("userId", this.val$userID);
                this.array.put(this.arrayObject);
                this.main.put("dataFields", this.array);
            } catch (JSONException e) {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e2) {
                }
                e.printStackTrace();
            }
        }

        private String doInBackground$4af589aa() {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            if (this.main == null) {
                System.out.println("null main");
            } else {
                System.out.println(this.main.toString());
            }
            try {
                HttpPost httpPost = new HttpPost(ProfilePictureActivity.this.getResources().getString(2131493167) + "updateUserDataField.htm");
                List<NameValuePair> data = new ArrayList();
                data.add(new BasicNameValuePair("userDataFiledJson", this.main.toString()));
                httpPost.entity = new UrlEncodedFormEntity(data);
                System.out.println("httppost " + httpPost.uri);
                InputStream inputStream = httpclient.execute((HttpUriRequest) httpPost).getEntity().getContent();
                BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseStr = new StringBuilder();
                while (true) {
                    String responseLineStr = bufferreader.readLine();
                    if (responseLineStr != null) {
                        responseStr.append(responseLineStr);
                    } else {
                        bufferreader.close();
                        inputStream.close();
                        return responseStr.toString();
                    }
                }
            } catch (ClientProtocolException e) {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e2) {
                }
                e.printStackTrace();
                return null;
            } catch (ClientProtocolException e3) {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e4) {
                }
                e3.printStackTrace();
                return null;
            }
        }
    }

    /* renamed from: com.shamchat.activity.ProfilePictureActivity.2 */
    class C08412 implements OnClickListener {
        C08412() {
        }

        public final void onClick(View v) {
            if (ProfilePictureActivity.this.isEmpty(ProfilePictureActivity.this.editTextUserName)) {
                ProfilePictureActivity.this.editTextUserName.setHint(ProfilePictureActivity.this.getResources().getString(2131493448));
                ProfilePictureActivity.this.editTextUserName.setHintTextColor(SupportMenu.CATEGORY_MASK);
            } else if (ProfilePictureActivity.this.isEmpty(ProfilePictureActivity.this.editTextShamID)) {
                ProfilePictureActivity.this.editTextShamID.setHint(ProfilePictureActivity.this.getResources().getString(2131493448));
                ProfilePictureActivity.this.editTextShamID.setHintTextColor(SupportMenu.CATEGORY_MASK);
            } else if (ProfilePictureActivity.this.isEmpty(ProfilePictureActivity.this.editTextPassword)) {
                ProfilePictureActivity.this.editTextPassword.setHint(ProfilePictureActivity.this.getResources().getString(2131493448));
                ProfilePictureActivity.this.editTextPassword.setHintTextColor(SupportMenu.CATEGORY_MASK);
            } else {
                ProfilePictureActivity.this.password = ProfilePictureActivity.this.editTextPassword.getText().toString().trim();
                ProfilePictureActivity.this.password = Utils.convertToEnglishDigits(ProfilePictureActivity.this.password);
                if (ProfilePictureActivity.this.password == null || ProfilePictureActivity.this.password.length() < 6) {
                    ProfilePictureActivity.this.editTextPassword.setText(BuildConfig.VERSION_NAME);
                    ProfilePictureActivity.this.editTextPassword.setHint("Password must be greater than six characters");
                    ProfilePictureActivity.this.editTextPassword.setHintTextColor(SupportMenu.CATEGORY_MASK);
                } else if (ProfilePictureActivity.this.buttonContinue.isEnabled()) {
                    ProfilePictureActivity.this.buttonContinue.setEnabled(false);
                    ProfilePictureActivity.this.password = ProfilePictureActivity.this.password.replaceAll(" ", "_");
                    ProfilePictureActivity.this.assignPasswordToUser(ProfilePictureActivity.this.phoneNumber, ProfilePictureActivity.this.password);
                    try {
                        ProgressBarDialogLogin.getInstance().show(ProfilePictureActivity.this.getSupportFragmentManager(), BuildConfig.VERSION_NAME);
                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(ProfilePictureActivity.this.getApplicationContext(), 2131493260, 0).show();
                }
            }
        }
    }

    /* renamed from: com.shamchat.activity.ProfilePictureActivity.2HttpAsyncTask */
    class AnonymousClass2HttpAsyncTask extends AsyncTask<String, String, String> {
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$phoneNumber;

        AnonymousClass2HttpAsyncTask(String str, String str2) {
            this.val$phoneNumber = str;
            this.val$password = str2;
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            String str = (String) obj;
            super.onPostExecute(str);
            try {
                ProgressBarDialogLogin.getInstance().dismiss();
            } catch (Exception e) {
            }
            if (str != null) {
                try {
                    String string = new JSONObject(str).getString(NotificationCompatApi21.CATEGORY_STATUS);
                    Log.d(NotificationCompatApi21.CATEGORY_STATUS, string);
                    if (string.equals("success")) {
                        ProfilePictureActivity.this.loginWithMobileNo(this.val$phoneNumber, this.val$password);
                    } else if (string.equals("user_not_verified")) {
                        Toast.makeText(ProfilePictureActivity.this, 2131493434, ProfilePictureActivity.SELECT_PICTURE).show();
                    } else if (string.equals("user_not_exists")) {
                        Toast.makeText(ProfilePictureActivity.this, 2131493232, ProfilePictureActivity.SELECT_PICTURE).show();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Toast.makeText(ProfilePictureActivity.this, e2.getMessage(), ProfilePictureActivity.SELECT_PICTURE).show();
                }
            }
        }

        protected final void onPreExecute() {
            super.onPreExecute();
        }

        private String doInBackground(String... params) {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                String encodedData = URLEncoder.encode(params[0], "UTF-8");
                InputStream inputStream = httpclient.execute((HttpUriRequest) new HttpGet(ProfilePictureActivity.this.getResources().getString(2131493167) + "assignedPasswordToUser.htm?mobileNo=" + encodedData + "&password=" + URLEncoder.encode(params[ProfilePictureActivity.SELECT_PICTURE], "UTF-8"))).getEntity().getContent();
                BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseStr = new StringBuilder();
                while (true) {
                    String responseLineStr = bufferreader.readLine();
                    if (responseLineStr != null) {
                        responseStr.append(responseLineStr);
                    } else {
                        bufferreader.close();
                        inputStream.close();
                        return responseStr.toString();
                    }
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    /* renamed from: com.shamchat.activity.ProfilePictureActivity.3 */
    class C08423 implements DialogInterface.OnClickListener {
        final /* synthetic */ CharSequence[] val$items;

        C08423(CharSequence[] charSequenceArr) {
            this.val$items = charSequenceArr;
        }

        public final void onClick(DialogInterface dialog, int item) {
            Intent intent;
            if (this.val$items[item].equals("Take Photo")) {
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                if (ProfilePictureActivity.this.currentapiVersion == 9 || ProfilePictureActivity.this.currentapiVersion == 10) {
                    intent.putExtra("output", ProfilePictureActivity.this.setImageUri());
                }
                ProfilePictureActivity.this.startActivityForResult(intent, ProfilePictureActivity.REQUEST_CAMERA);
            } else if (this.val$items[item].equals("Choose from Library")) {
                intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                ProfilePictureActivity.this.startActivityForResult(Intent.createChooser(intent, "Select File"), ProfilePictureActivity.SELECT_PICTURE);
            } else if (this.val$items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        }
    }

    /* renamed from: com.shamchat.activity.ProfilePictureActivity.3HttpAsyncTask */
    class AnonymousClass3HttpAsyncTask extends AsyncTask<String, String, String> {
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$phoneNumber;

        /* renamed from: com.shamchat.activity.ProfilePictureActivity.3HttpAsyncTask.1 */
        class C08431 extends JsonHttpResponseHandler {
            final /* synthetic */ String val$profImageByte;

            C08431(String str) {
                this.val$profImageByte = str;
            }

            public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                try {
                    if (response.getString(NotificationCompatApi21.CATEGORY_STATUS).equalsIgnoreCase("success")) {
                        System.out.println("FileUploadManager result is success");
                        String url = response.getJSONObject("data").getString("file_view_url");
                        if (url == null || url.length() <= ProfilePictureActivity.SELECT_PICTURE) {
                            ProfilePictureActivity.this.updateUserInformation(ProfilePictureActivity.this.user.userId, ProfilePictureActivity.this.user.chatId, ProfilePictureActivity.this.user.username, this.val$profImageByte, BuildConfig.VERSION_NAME, ProfilePictureActivity.this.user.email, AnonymousClass3HttpAsyncTask.this.val$password);
                        } else {
                            ProfilePictureActivity.this.updateUserInformation(ProfilePictureActivity.this.user.userId, ProfilePictureActivity.this.user.chatId, ProfilePictureActivity.this.user.username, this.val$profImageByte, url, ProfilePictureActivity.this.user.email, AnonymousClass3HttpAsyncTask.this.val$password);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ProfilePictureActivity.this.updateUserInformation(ProfilePictureActivity.this.user.userId, ProfilePictureActivity.this.user.chatId, ProfilePictureActivity.this.user.username, this.val$profImageByte, BuildConfig.VERSION_NAME, ProfilePictureActivity.this.user.email, AnonymousClass3HttpAsyncTask.this.val$password);
            }

            public final void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }

            public final void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }
        }

        AnonymousClass3HttpAsyncTask(String str, String str2) {
            this.val$phoneNumber = str;
            this.val$password = str2;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected final /* bridge */ /* synthetic */ void onPostExecute(java.lang.Object r10) {
            /*
            r9 = this;
            r8 = 1;
            r10 = (java.lang.String) r10;
            super.onPostExecute(r10);
            r0 = com.shamchat.activity.ProgressBarDialogLogin.getInstance();	 Catch:{ Exception -> 0x0176 }
            r0.dismiss();	 Catch:{ Exception -> 0x0176 }
        L_0x000d:
            if (r10 == 0) goto L_0x0129;
        L_0x000f:
            r0 = new org.json.JSONObject;	 Catch:{ Exception -> 0x012f }
            r0.<init>(r10);	 Catch:{ Exception -> 0x012f }
            r1 = "phone_loginwithphone";
            r2 = r9.val$phoneNumber;	 Catch:{ Exception -> 0x012f }
            android.util.Log.d(r1, r2);	 Catch:{ Exception -> 0x012f }
            r1 = "status";
            r1 = r0.getString(r1);	 Catch:{ Exception -> 0x012f }
            r2 = "status_loginwithphonenu";
            android.util.Log.d(r2, r1);	 Catch:{ Exception -> 0x012f }
            r2 = "success";
            r2 = r1.equals(r2);	 Catch:{ Exception -> 0x012f }
            if (r2 == 0) goto L_0x016b;
        L_0x002e:
            r1 = "data";
            r0 = r0.getJSONObject(r1);	 Catch:{ Exception -> 0x012f }
            r1 = "user";
            r0 = r0.getJSONObject(r1);	 Catch:{ Exception -> 0x012f }
            r1 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r2 = new com.shamchat.models.User;	 Catch:{ Exception -> 0x012f }
            r2.<init>(r0);	 Catch:{ Exception -> 0x012f }
            r1.user = r2;	 Catch:{ Exception -> 0x012f }
            r0 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r0 = r0.user;	 Catch:{ Exception -> 0x012f }
            r1 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r1 = r1.editTextShamID;	 Catch:{ Exception -> 0x012f }
            r1 = r1.getText();	 Catch:{ Exception -> 0x012f }
            r1 = r1.toString();	 Catch:{ Exception -> 0x012f }
            r1 = r1.trim();	 Catch:{ Exception -> 0x012f }
            r0.chatId = r1;	 Catch:{ Exception -> 0x012f }
            r0 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r0 = r0.user;	 Catch:{ Exception -> 0x012f }
            r1 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r1 = r1.editTextUserName;	 Catch:{ Exception -> 0x012f }
            r1 = r1.getText();	 Catch:{ Exception -> 0x012f }
            r1 = r1.toString();	 Catch:{ Exception -> 0x012f }
            r1 = r1.trim();	 Catch:{ Exception -> 0x012f }
            r0.username = r1;	 Catch:{ Exception -> 0x012f }
            r0 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r0 = r0.user;	 Catch:{ Exception -> 0x012f }
            r1 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r1 = r1.email;	 Catch:{ Exception -> 0x012f }
            r0.email = r1;	 Catch:{ Exception -> 0x012f }
            r4 = "";
            r0 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r0 = r0.profileImageByteArray;	 Catch:{ Exception -> 0x012f }
            if (r0 == 0) goto L_0x0141;
        L_0x0090:
            r0 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r0 = r0.profileImageByteArray;	 Catch:{ Exception -> 0x012f }
            r1 = 0;
            r1 = android.util.Base64.encodeToString(r0, r1);	 Catch:{ Exception -> 0x012f }
            r2 = new java.io.File;	 Catch:{ Exception -> 0x012f }
            r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x012f }
            r0.<init>();	 Catch:{ Exception -> 0x012f }
            r3 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x012f }
            r0 = r0.append(r3);	 Catch:{ Exception -> 0x012f }
            r3 = "/salam/profileimage";
            r0 = r0.append(r3);	 Catch:{ Exception -> 0x012f }
            r3 = java.io.File.separator;	 Catch:{ Exception -> 0x012f }
            r0 = r0.append(r3);	 Catch:{ Exception -> 0x012f }
            r3 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r3 = r3.user;	 Catch:{ Exception -> 0x012f }
            r3 = r3.userId;	 Catch:{ Exception -> 0x012f }
            r0 = r0.append(r3);	 Catch:{ Exception -> 0x012f }
            r3 = ".png";
            r0 = r0.append(r3);	 Catch:{ Exception -> 0x012f }
            r0 = r0.toString();	 Catch:{ Exception -> 0x012f }
            r2.<init>(r0);	 Catch:{ Exception -> 0x012f }
            r0 = r2.exists();	 Catch:{ Exception -> 0x012f }
            if (r0 == 0) goto L_0x00d8;
        L_0x00d5:
            r2.delete();	 Catch:{ Exception -> 0x012f }
        L_0x00d8:
            r0 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x012a }
            r3 = r2.getPath();	 Catch:{ IOException -> 0x012a }
            r0.<init>(r3);	 Catch:{ IOException -> 0x012a }
            r3 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ IOException -> 0x012a }
            r3 = r3.profileImageByteArray;	 Catch:{ IOException -> 0x012a }
            r0.write(r3);	 Catch:{ IOException -> 0x012a }
            r0.close();	 Catch:{ IOException -> 0x012a }
        L_0x00ed:
            r0 = new com.loopj.android.http.RequestParams;	 Catch:{ Exception -> 0x012f }
            r0.<init>();	 Catch:{ Exception -> 0x012f }
            r3 = "userId";
            r4 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r4 = r4.user;	 Catch:{ Exception -> 0x012f }
            r4 = r4.userId;	 Catch:{ Exception -> 0x012f }
            r0.put(r3, r4);	 Catch:{ Exception -> 0x012f }
            r3 = "file";
            r0.put(r3, r2);	 Catch:{ Exception -> 0x012f }
            r2 = new com.loopj.android.http.AsyncHttpClient;	 Catch:{ Exception -> 0x012f }
            r2.<init>();	 Catch:{ Exception -> 0x012f }
            r3 = 5;
            r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
            r2.setMaxRetriesAndTimeout(r3, r4);	 Catch:{ Exception -> 0x012f }
            r3 = 300000; // 0x493e0 float:4.2039E-40 double:1.482197E-318;
            r2.setTimeout(r3);	 Catch:{ Exception -> 0x012f }
            r3 = 300000; // 0x493e0 float:4.2039E-40 double:1.482197E-318;
            r2.setResponseTimeout(r3);	 Catch:{ Exception -> 0x012f }
            r3 = com.shamchat.androidclient.SHAMChatApplication.getMyApplicationContext();	 Catch:{ Exception -> 0x012f }
            r4 = "http://static.rabtcdn.com/upload_file.php";
            r5 = new com.shamchat.activity.ProfilePictureActivity$3HttpAsyncTask$1;	 Catch:{ Exception -> 0x012f }
            r5.<init>(r1);	 Catch:{ Exception -> 0x012f }
            r2.post(r3, r4, r0, r5);	 Catch:{ Exception -> 0x012f }
        L_0x0129:
            return;
        L_0x012a:
            r0 = move-exception;
            r0.printStackTrace();	 Catch:{ Exception -> 0x012f }
            goto L_0x00ed;
        L_0x012f:
            r0 = move-exception;
            r0.printStackTrace();
            r1 = com.shamchat.activity.ProfilePictureActivity.this;
            r0 = r0.getMessage();
            r0 = android.widget.Toast.makeText(r1, r0, r8);
            r0.show();
            goto L_0x0129;
        L_0x0141:
            r0 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r1 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r1 = r1.user;	 Catch:{ Exception -> 0x012f }
            r1 = r1.userId;	 Catch:{ Exception -> 0x012f }
            r2 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r2 = r2.user;	 Catch:{ Exception -> 0x012f }
            r2 = r2.chatId;	 Catch:{ Exception -> 0x012f }
            r3 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r3 = r3.user;	 Catch:{ Exception -> 0x012f }
            r3 = r3.username;	 Catch:{ Exception -> 0x012f }
            r5 = "";
            r6 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r6 = r6.user;	 Catch:{ Exception -> 0x012f }
            r6 = r6.email;	 Catch:{ Exception -> 0x012f }
            r7 = r9.val$password;	 Catch:{ Exception -> 0x012f }
            r0.updateUserInformation(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x012f }
            goto L_0x0129;
        L_0x016b:
            r0 = com.shamchat.activity.ProfilePictureActivity.this;	 Catch:{ Exception -> 0x012f }
            r2 = 1;
            r0 = android.widget.Toast.makeText(r0, r1, r2);	 Catch:{ Exception -> 0x012f }
            r0.show();	 Catch:{ Exception -> 0x012f }
            goto L_0x0129;
        L_0x0176:
            r0 = move-exception;
            goto L_0x000d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shamchat.activity.ProfilePictureActivity.3HttpAsyncTask.onPostExecute(java.lang.Object):void");
        }

        protected final void onPreExecute() {
            super.onPreExecute();
        }

        private String doInBackground(String... params) {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                InputStream inputStream = httpclient.execute((HttpUriRequest) new HttpGet(ProfilePictureActivity.this.getResources().getString(2131493167) + "loginWithMobileNo.htm?mobileNo=" + URLEncoder.encode(params[0], "UTF-8") + "&password=" + params[ProfilePictureActivity.SELECT_PICTURE])).getEntity().getContent();
                BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseStr = new StringBuilder();
                while (true) {
                    String responseLineStr = bufferreader.readLine();
                    if (responseLineStr != null) {
                        responseStr.append(responseLineStr);
                    } else {
                        bufferreader.close();
                        inputStream.close();
                        return responseStr.toString().trim();
                    }
                }
            } catch (ClientProtocolException e) {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e2) {
                }
                e.printStackTrace();
                return null;
            } catch (ClientProtocolException e3) {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e4) {
                }
                e3.printStackTrace();
                return null;
            }
        }
    }

    public ProfilePictureActivity() {
        this.email = null;
        this.currentapiVersion = VERSION.SDK_INT;
    }

    static {
        CROPED_IMAGE = 2;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(SELECT_PICTURE);
        super.onCreate(savedInstanceState);
        setContentView(2130903098);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.utils = new Utils();
        Builder builder = new Builder();
        builder.displayer = new RoundedBitmapDisplayer();
        this.options = builder.build();
        try {
            this.phoneNumber = getIntent().getExtras().getString("user_mobileNo");
        } catch (Exception e) {
            this.phoneNumber = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user_mobileNo", null);
        }
        this.profilePhotoUpload = (ImageView) findViewById(2131362064);
        this.buttonContinue = (Button) findViewById(2131362072);
        this.editTextUserName = (EditText) findViewById(2131362066);
        this.editTextShamID = (EditText) findViewById(2131362068);
        this.editTextPassword = (EditText) findViewById(2131362071);
        this.profilePhotoUpload.setOnClickListener(new C08401());
        this.buttonContinue.setOnClickListener(new C08412());
    }

    public void onResume() {
        super.onResume();
        try {
            EventBus.getDefault().register(this, true, 0);
        } catch (Throwable th) {
        }
    }

    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        }
        return true;
    }

    private void selectOrTakePhoto() {
        CharSequence[] items = new CharSequence[REQUEST_CAMERA];
        items[0] = "Take Photo";
        items[SELECT_PICTURE] = "Choose from Library";
        items[2] = "Cancel";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new C08423(items));
        builder.show();
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == REQUEST_CAMERA) {
            if (this.currentapiVersion == 9 || this.currentapiVersion == 10) {
                cropCapturedImage(Uri.fromFile(new File(getImagePath())));
            } else if (data != null) {
                String tempPath = getPath(data.getData(), this);
                Utils.scaleDownImageSize$5c843e57((Bitmap) data.getExtras().get("data"));
                cropCapturedImage(Uri.fromFile(new File(tempPath)));
            } else {
                Toast.makeText(this, "Error occured", SELECT_PICTURE).show();
            }
        } else if (requestCode == SELECT_PICTURE) {
            cropCapturedImage(Uri.fromFile(new File(getPath(data.getData(), this))));
        } else if (requestCode == CROPED_IMAGE) {
            Bitmap thePic = Utils.fixOrientation((Bitmap) data.getExtras().getParcelable("data"));
            ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
            thePic.compress(CompressFormat.JPEG, 70, imageByteStream);
            thePic.recycle();
            this.profileImageByteArray = imageByteStream.toByteArray();
            this.profilePhotoUpload.setImageBitmap(BitmapFactory.decodeByteArray(this.profileImageByteArray, 0, this.profileImageByteArray.length));
        }
    }

    public void cropCapturedImage(Uri picUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(picUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", SELECT_PICTURE);
        cropIntent.putExtra("aspectY", SELECT_PICTURE);
        cropIntent.putExtra("outputX", AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
        cropIntent.putExtra("outputY", AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, CROPED_IMAGE);
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = new String[SELECT_PICTURE];
        projection[0] = "_data";
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndexOrThrow("_data"));
    }

    public Uri setImageUri() {
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public String getImagePath() {
        return this.imgPath;
    }

    private void updateUserInformation(String userID, String shamID, String userName, String profilePictureBase64, String profilePicture, String email, String password) {
        new AnonymousClass1HttpAsyncTask(profilePicture, shamID, userName, profilePictureBase64, userID, email, password).execute(new String[]{userID, shamID, userName, profilePicture, email});
    }

    private void assignPasswordToUser(String phoneNumber, String password) {
        new AnonymousClass2HttpAsyncTask(phoneNumber, password).execute(new String[]{phoneNumber, password});
    }

    private void loginWithMobileNo(String phoneNumber, String password) {
        new AnonymousClass3HttpAsyncTask(phoneNumber, password).execute(new String[]{phoneNumber, password});
    }
}
