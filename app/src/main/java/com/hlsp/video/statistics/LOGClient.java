package com.hlsp.video.statistics;


import com.hlsp.video.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.Deflater;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wangjwchn on 16/8/2.
 */
public class LOGClient {
    private String mEndPoint;
    private String mAccessKeyID;
    private String mAccessKeySecret;
    private String mAccessToken;
    private String mProject;
    private String mHttpType;

    public LOGClient(String endPoint, String accessKeyID, String accessKeySecret, String projectName) {
        mHttpType = "http://";
        if (endPoint != "") mEndPoint = endPoint;
        else throw new NullPointerException("endpoint is null");
        if (mEndPoint.startsWith("http://")) {
            mEndPoint = mEndPoint.substring(7);
        } else if (mEndPoint.startsWith("https://")) {
            mEndPoint = mEndPoint.substring(8);
            mHttpType = "https://";
        }
        while (mEndPoint.endsWith("/")) {
            mEndPoint = mEndPoint.substring(0, mEndPoint.length() - 1);
        }

        if (accessKeyID != "") mAccessKeyID = accessKeyID;
        else throw new NullPointerException("accessKeyID is null");

        if (accessKeySecret != "") mAccessKeySecret = accessKeySecret;
        else throw new NullPointerException("accessKeySecret is null");

        if (projectName != "") mProject = projectName;
        else throw new NullPointerException("projectName is null");

        mAccessToken = "";
    }

    public static String GetMGTTime() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        String str = sdf.format(cd.getTime());
        return str;
    }

    public static String hmac_sha1(String encryptText, String encryptKey) throws Exception {
        byte[] keyBytes = encryptKey.getBytes("UTF-8");
        byte[] dataBytes = encryptText.getBytes("UTF-8");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(keyBytes, "HmacSHA1"));
        return new String(Base64Kit.encode(mac.doFinal(dataBytes)));
    }

    public void SetToken(String token) {
        mAccessToken = token;
    }

    public String GetToken() {
        return mAccessToken;
    }

    public String GetEndPoint() {
        return mEndPoint;
    }

    public String GetKeyID() {
        return mAccessKeyID;
    }

    public String GetKeySecret() {
        return mAccessKeySecret;
    }

    public void PostLog(final LogGroup logGroup, String logStoreName) throws LogException {
        final String httpUrl = mHttpType + mProject + "." + mEndPoint + "/logstores/" + logStoreName + "/shards/lb";
        byte[] httpPostBody;
        try {
            httpPostBody = logGroup.LogGroupToJsonString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new LogException("LogClientError", "Failed to pass log to utf-8 bytes", e, "");
        }
        final byte[] httpPostBodyZipped = GzipFrom(httpPostBody);
        final Map<String, String> httpHeaders = GetHttpHeadersFrom(logStoreName, httpPostBody, httpPostBodyZipped);
        HttpPostRequest(httpUrl, httpHeaders, httpPostBodyZipped);
    }

    public void HttpPostRequest(String url, Map<String, String> headers, byte[] body) throws LogException {
        URL u;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            throw new LogException("LogClientError", "illegal post url", e, "");
        }

        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) u.openConnection();
        } catch (IOException e) {
            throw new LogException("LogClientError", "fail to create HttpURLConnection", e, "");
        }


        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new LogException("LogClientError", "fail to set http request method to  POST", e, "");
        }
        conn.setDoOutput(true);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

        try {
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(body);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            throw new LogException("LogClientError", "fail to post data to URL:" + url, e, "");
        }


        try {
            int responseCode = conn.getResponseCode();
            String request_id = conn.getHeaderField("x-log-requestid");

            if (request_id == null) {
                request_id = "";
            }
            if (responseCode != 200) {
                InputStream error_stream = conn.getErrorStream();
                if (error_stream != null) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(error_stream));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    CheckError(response.toString(), request_id);
                    throw new LogException("LogServerError", "Response code:"
                            + String.valueOf(responseCode) + "\nMessage:"
                            + response.toString(), request_id);
                } else {
                    throw new LogException("LogServerError", "Response code:"
                            + String.valueOf(responseCode)
                            + "\nMessage: fail to connect to the server",
                            request_id);
                }
            }// else success
        } catch (IOException e) {
            throw new LogException("LogServerError",
                    "Failed to parse response data", "");
        }
    }

    private void CheckError(String error_message, String request_id) throws LogException {
        try {
            JSONObject obj = (JSONObject) GsonUtil.JSONToObject(error_message,String.class);
            if (obj != null && obj.has("errorCode") && obj.has("errorMessage")) {
                throw new LogException(obj.getString("errorCode"), obj.getString("errorMessage"), request_id);
            }
        } catch (JSONException e) {
        }
    }

    public Map<String, String> GetHttpHeadersFrom(String logStoreName, byte[] body, byte[] bodyZipped) throws LogException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-log-apiversion", "0.6.0");
        headers.put("x-log-signaturemethod", "hmac-sha1");
        headers.put("Content-Type", "application/json");
        headers.put("Date", GetMGTTime());
        headers.put("Content-MD5", ParseToMd5U32From(bodyZipped));
        headers.put("Content-Length", String.valueOf(bodyZipped.length));
        headers.put("x-log-bodyrawsize", String.valueOf(body.length));
        headers.put("x-log-compresstype", "deflate");
        headers.put("Host", mProject + "." + mEndPoint);

        StringBuilder signStringBuf = new StringBuilder("POST" + "\n").
                append(headers.get("Content-MD5") + "\n").
                append(headers.get("Content-Type") + "\n").
                append(headers.get("Date") + "\n");
        String token = mAccessToken;
        if (token != null && token != "") {
            headers.put("x-acs-security-token", token);
            signStringBuf.append("x-acs-security-token:" + headers.get("x-acs-security-token") + "\n");
        }
        signStringBuf.append("x-log-apiversion:0.6.0\n").
                append("x-log-bodyrawsize:" + headers.get("x-log-bodyrawsize") + "\n").
                append("x-log-compresstype:deflate\n").
                append("x-log-signaturemethod:hmac-sha1\n").
                append("/logstores/" + logStoreName + "/shards/lb");
        String signString = signStringBuf.toString();
        try {
            String sign = hmac_sha1(signString, mAccessKeySecret);
            headers.put("Authorization", "LOG " + mAccessKeyID + ":" + sign);
        } catch (Exception e) {
            throw new LogException("LogClientError", "fail to get encode signature", e, "");
        }
        return headers;
    }

    private String ParseToMd5U32From(byte[] bytes) throws LogException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String res = new BigInteger(1, md.digest(bytes)).toString(16).toUpperCase();
            StringBuilder zeros = new StringBuilder();
            for (int i = 0; i + res.length() < 32; i++) {
                zeros.append("0");
            }
            return zeros.toString() + res;
        } catch (NoSuchAlgorithmException e) {
            throw new LogException("LogClientError", "Not Supported signature method " + "MD5", e, "");
        }
    }

    private byte[] GzipFrom(byte[] jsonByte) throws LogException {
        ByteArrayOutputStream out = null;
        Deflater compresser = new Deflater();
        try {
            out = new ByteArrayOutputStream(jsonByte.length);
            compresser.setInput(jsonByte);
            compresser.finish();
            byte[] buf = new byte[10240];
            while (compresser.finished() == false) {
                int count = compresser.deflate(buf);
                out.write(buf, 0, count);
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new LogException("LogClientError", "fail to zip data", "");
        } finally {
            compresser.end();
            try {
                if (out.size() != 0) out.close();
            } catch (IOException e) {
            }
        }
    }
}
