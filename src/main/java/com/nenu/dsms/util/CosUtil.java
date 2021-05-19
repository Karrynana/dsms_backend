package com.nenu.dsms.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSSigner;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class CosUtil {
    private final String secretId = "AKIDtqDVcX29VhCZkjwEukKebaTEq68x8Xkr";
    private final String secretKey = "A8xrbm494uc2LUTXRDxYlpjU8czCvuiM";
    private final HttpProtocol protocol = HttpProtocol.https;
    private final String bucketSuffix = ".cos.ap-beijing.myqcloud.com/";
    private final String priFix;

    public static final CosUtil INSTANCE = new CosUtil();

    private final String bucketName;

    public final COSClient cosClient;

    private CosUtil() {

        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("ap-beijing");
        ClientConfig clientConfig = new ClientConfig(region);
        bucketName = "ms0-1258865812";
        clientConfig.setHttpProtocol(protocol);
        priFix = protocol.equals(HttpProtocol.http) ? "http://" : "https://";
        cosClient = new COSClient(cred, clientConfig);
    }

    public String put(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        cosClient.putObject(putObjectRequest);
        return getBucketAccessAddress() + key;
    }

    /**
     * 获取带权限的下载链接
     */
    public URL getSourceUrl(String key) {
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
        req.setExpiration(expirationDate);
        URL url = cosClient.generatePresignedUrl(req);
        System.out.println(url.toString());
        return url;
    }

    public void deleteSource(String key) {
        cosClient.deleteObject(bucketName, key);
    }

    public String getDownloadSign(String key) {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        COSSigner signer = new COSSigner();
        // 设置过期时间为1个小时
        Date expiredTime = new Date(System.currentTimeMillis() + 3600L * 1000L);
        return signer.buildAuthorizationStr(HttpMethodName.GET, key, cred, expiredTime);
    }

    public String getBucketAccessAddress() {
        return priFix + bucketName + bucketSuffix;
    }

    public String getKeyFromUrl(String url) {
        String key = url.substring(getBucketAccessAddress().length());
        System.out.println(key);
        return key;
    }
}
