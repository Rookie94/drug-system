package com.ruoyi.common.utils.file;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.lifecycle.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.util.IOUtils;

import com.ruoyi.common.config.ZosConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

//@Slf4j
//@Component
//@ConditionalOnProperty(name = "zos.enable-storage", havingValue = "true")
public class ZosUtils {

//    @Autowired
//    private ZosConfig prop;
//
//    // 缓存 AmazonS3 客户端，避免重复创建
//    private AmazonS3 s3Client = null;
//
//    /* ---------------------------------------------------------
//       构建 AmazonS3 客户端（内网 endpoint）
//       --------------------------------------------------------- */
//    private synchronized AmazonS3 buildClient() {
//        if (s3Client != null) {
//            return s3Client;
//        }
//
//        try {
//            log.info("初始化 ZOS S3 客户端，Endpoint: {}, Bucket: {}",
//                    prop.getEndPoint(), prop.getBucketName());
//
//            // 验证配置
//            if (StringUtils.isEmpty(prop.getAccessKey())) {
//                throw new IllegalArgumentException("AccessKey 不能为空");
//            }
//            if (StringUtils.isEmpty(prop.getSecretKey())) {
//                throw new IllegalArgumentException("SecretKey 不能为空");
//            }
//            if (StringUtils.isEmpty(prop.getEndPoint())) {
//                throw new IllegalArgumentException("Endpoint 不能为空");
//            }
//            if (StringUtils.isEmpty(prop.getBucketName())) {
//                throw new IllegalArgumentException("BucketName 不能为空");
//            }
//
//            AWSCredentials cred = new BasicAWSCredentials(prop.getAccessKey(), prop.getSecretKey());
//
//            ClientConfiguration cfg = new ClientConfiguration();
//            cfg.setSignerOverride("AWSS3V4SignerType");
//            cfg.setProtocol(Protocol.HTTP);
//            cfg.setConnectionTimeout(30000);    // 30秒连接超时
//            cfg.setSocketTimeout(30000);        // 30秒socket超时
//            cfg.setMaxErrorRetry(3);            // 最大重试次数
//
//            // 清理 Endpoint，确保格式正确
//            String endpoint = prop.getEndPoint().trim();
//            if (!endpoint.startsWith("http://") && !endpoint.startsWith("https://")) {
//                endpoint = "http://" + endpoint;  // 默认使用 http
//            }
//            if (endpoint.endsWith("/")) {
//                endpoint = endpoint.substring(0, endpoint.length() - 1);
//            }
//
//            log.info("使用 Endpoint: {}, Region: cn", endpoint);
//
//            s3Client = AmazonS3ClientBuilder.standard()
//                    .withEndpointConfiguration(
//                            new AwsClientBuilder.EndpointConfiguration(endpoint, "cn"))
//                    .withCredentials(new AWSStaticCredentialsProvider(cred))
//                    .withClientConfiguration(cfg)
//                    .disableChunkedEncoding()
//                    .enablePathStyleAccess()
//                    .build();
//
//            // 测试连接
//            testConnection();
//
//            log.info("ZOS S3 客户端初始化成功");
//            return s3Client;
//
//        } catch (Exception e) {
//            log.error("初始化 ZOS S3 客户端失败", e);
//            throw new RuntimeException("初始化 ZOS 客户端失败: " + e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 测试连接和权限
//     */
//    private void testConnection() {
//        try {
//            String bucketName = prop.getBucketName();
//            log.info("测试连接和权限检查，Bucket: {}", bucketName);
//
//            // 1. 尝试列出 Buckets（需要全局权限）
//            try {
//                List<Bucket> buckets = s3Client.listBuckets();
//                log.info("可访问的 Buckets 数量: {}", buckets.size());
//            } catch (Exception e) {
//                log.warn("无法列出所有 Buckets（可能权限不足）: {}", e.getMessage());
//            }
//
//            // 2. 检查指定的 Bucket 是否存在
//            boolean bucketExists = false;
//            try {
//                bucketExists = s3Client.doesBucketExistV2(bucketName);
//                if (!bucketExists) {
//                    log.warn("Bucket {} 不存在，尝试创建...", bucketName);
//                    try {
//                        s3Client.createBucket(bucketName);
//                        log.info("Bucket {} 创建成功", bucketName);
//                        bucketExists = true;
//                    } catch (Exception e) {
//                        log.error("创建 Bucket 失败，请确保有创建权限: {}", e.getMessage());
//                    }
//                } else {
//                    log.info("Bucket {} 已存在", bucketName);
//                }
//            } catch (Exception e) {
//                log.error("检查 Bucket 是否存在失败: {}", e.getMessage());
//            }
//
//            // 3. 如果有权限，检查 Bucket ACL
//            if (bucketExists) {
//                try {
//                    AccessControlList acl = s3Client.getBucketAcl(bucketName);
//                    log.info("Bucket ACL 检查完成，所有者: {}", acl.getOwner().getDisplayName());
//                } catch (Exception e) {
//                    log.warn("获取 Bucket ACL 失败（可能权限不足）: {}", e.getMessage());
//                }
//            }
//
//            log.info("ZOS 连接测试完成");
//
//        } catch (Exception e) {
//            log.error("连接测试失败: {}", e.getMessage());
//            // 不抛出异常，让应用继续运行，可能只是权限问题
//        }
//    }
//
//    /* ---------------------------------------------------------
//       获取配置信息
//       --------------------------------------------------------- */
//    public String getBucketName() {
//        return prop.getBucketName();
//    }
//
//    public String getEndPoint() {
//        return prop.getEndPoint();
//    }
//
//    public String getDomain() {
//        return prop.getDomain();
//    }
//
//    /* ---------------------------------------------------------
//       简化上传方法
//       --------------------------------------------------------- */
//    public PutObjectResult putObject(String key, File file) {
//        return buildClient().putObject(prop.getBucketName(), key, file);
//    }
//
//    public PutObjectResult putObject(String key, File file, ObjectMetadata metadata) {
//        try (FileInputStream fis = new FileInputStream(file)) {
//            if (metadata == null) {
//                metadata = new ObjectMetadata();
//            }
//            metadata.setContentLength(file.length());
//            return buildClient().putObject(prop.getBucketName(), key, fis, metadata);
//        } catch (IOException e) {
//            throw new RuntimeException("读取文件失败: " + file.getPath(), e);
//        }
//    }
//
//    public PutObjectResult putObject(String bucket, String key, File file) {
//        return buildClient().putObject(bucket, key, file);
//    }
//
//    public PutObjectResult putObject(PutObjectRequest request) {
//        return buildClient().putObject(request);
//    }
//
//    /**
//     * 简单上传方法（针对小文件）
//     */
//    public PutObjectResult simpleUpload(String key, MultipartFile file) throws IOException {
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(file.getSize());
//        metadata.setContentType(file.getContentType());
//        if (file.getOriginalFilename() != null) {
//            metadata.addUserMetadata("original-filename", file.getOriginalFilename());
//        }
//
//        return buildClient().putObject(
//                prop.getBucketName(),
//                key,
//                file.getInputStream(),
//                metadata
//        );
//    }
//
//    /**
//     * 带重试的上传方法
//     */
//    public PutObjectResult putObjectWithRetry(String key, File file, int maxRetries) {
//        Exception lastException = null;
//        for (int i = 0; i < maxRetries; i++) {
//            try {
//                return putObject(key, file);
//            } catch (Exception e) {
//                lastException = e;
//                log.warn("上传失败，第 {} 次重试: {}", i + 1, e.getMessage());
//                if (i < maxRetries - 1) {
//                    try {
//                        Thread.sleep(1000 * (i + 1)); // 递增延迟
//                    } catch (InterruptedException ie) {
//                        Thread.currentThread().interrupt();
//                        throw new RuntimeException("上传被中断", ie);
//                    }
//                }
//            }
//        }
//        throw new RuntimeException("上传失败，重试 " + maxRetries + " 次后放弃", lastException);
//    }
//
//    /* ---------------------------------------------------------
//       检查 Bucket 是否存在
//       --------------------------------------------------------- */
//    public boolean bucketExists() {
//        return bucketExists(prop.getBucketName());
//    }
//
//    public boolean bucketExists(String bucketName) {
//        try {
//            return buildClient().doesBucketExistV2(bucketName);
//        } catch (Exception e) {
//            log.error("检查 Bucket 是否存在失败", e);
//            return false;
//        }
//    }
//
//    /* ---------------------------------------------------------
//       桶级操作
//       --------------------------------------------------------- */
//    public Bucket createBucket() {
//        return createBucket(prop.getBucketName());
//    }
//
//    public Bucket createBucket(String bucket) {
//        return buildClient().createBucket(bucket);
//    }
//
//    public void deleteBucket(String bucket) {
//        buildClient().deleteBucket(bucket);
//    }
//
//    public boolean headBucket(String bucket) {
//        try {
//            buildClient().headBucket(new HeadBucketRequest(bucket));
//            return true;
//        } catch (Exception e) {
//            log.warn("HeadBucket 失败: {}", e.getMessage());
//            return false;
//        }
//    }
//
//    public List<Bucket> listBuckets() {
//        return buildClient().listBuckets();
//    }
//
//    public void setBucketPolicy(String policyJson) {
//        setBucketPolicy(prop.getBucketName(), policyJson);
//    }
//
//    public void setBucketPolicy(String bucket, String policyJson) {
//        buildClient().setBucketPolicy(new SetBucketPolicyRequest(bucket, policyJson));
//    }
//
//    public String getBucketPolicy() {
//        return getBucketPolicy(prop.getBucketName());
//    }
//
//    public String getBucketPolicy(String bucket) {
//        return buildClient().getBucketPolicy(bucket).getPolicyText();
//    }
//
//    public void deleteBucketPolicy() {
//        deleteBucketPolicy(prop.getBucketName());
//    }
//
//    public void deleteBucketPolicy(String bucket) {
//        buildClient().deleteBucketPolicy(bucket);
//    }
//
//    public void setBucketAcl(CannedAccessControlList acl) {
//        setBucketAcl(prop.getBucketName(), acl);
//    }
//
//    public void setBucketAcl(String bucket, CannedAccessControlList acl) {
//        buildClient().setBucketAcl(bucket, acl);
//    }
//
//    public AccessControlList getBucketAcl() {
//        return getBucketAcl(prop.getBucketName());
//    }
//
//    public AccessControlList getBucketAcl(String bucket) {
//        return buildClient().getBucketAcl(bucket);
//    }
//
//    public void setBucketLifecycle(BucketLifecycleConfiguration config) {
//        setBucketLifecycle(prop.getBucketName(), config);
//    }
//
//    public void setBucketLifecycle(String bucket, BucketLifecycleConfiguration config) {
//        buildClient().setBucketLifecycleConfiguration(
//                new SetBucketLifecycleConfigurationRequest(bucket, config));
//    }
//
//    public BucketLifecycleConfiguration getBucketLifecycle() {
//        return getBucketLifecycle(prop.getBucketName());
//    }
//
//    public BucketLifecycleConfiguration getBucketLifecycle(String bucket) {
//        return buildClient().getBucketLifecycleConfiguration(
//                new GetBucketLifecycleConfigurationRequest(bucket));
//    }
//
//    public void deleteBucketLifecycle() {
//        deleteBucketLifecycle(prop.getBucketName());
//    }
//
//    public void deleteBucketLifecycle(String bucket) {
//        buildClient().deleteBucketLifecycleConfiguration(
//                new DeleteBucketLifecycleConfigurationRequest(bucket));
//    }
//
//    public void setBucketEncryption(ServerSideEncryptionConfiguration config) {
//        setBucketEncryption(prop.getBucketName(), config);
//    }
//
//    public void setBucketEncryption(String bucket, ServerSideEncryptionConfiguration config) {
//        SetBucketEncryptionRequest req = new SetBucketEncryptionRequest();
//        req.setBucketName(bucket);
//        req.setServerSideEncryptionConfiguration(config);
//        buildClient().setBucketEncryption(req);
//    }
//
//    public ServerSideEncryptionConfiguration getBucketEncryption() {
//        return getBucketEncryption(prop.getBucketName());
//    }
//
//    public ServerSideEncryptionConfiguration getBucketEncryption(String bucket) {
//        GetBucketEncryptionRequest req = new GetBucketEncryptionRequest();
//        req.setBucketName(bucket);
//        return buildClient().getBucketEncryption(req)
//                .getServerSideEncryptionConfiguration();
//    }
//
//    public void deleteBucketEncryption() {
//        deleteBucketEncryption(prop.getBucketName());
//    }
//
//    public void deleteBucketEncryption(String bucket) {
//        DeleteBucketEncryptionRequest req = new DeleteBucketEncryptionRequest();
//        req.setBucketName(bucket);
//        buildClient().deleteBucketEncryption(req);
//    }
//
//    public void setBucketWebsite(BucketWebsiteConfiguration config) {
//        setBucketWebsite(prop.getBucketName(), config);
//    }
//
//    public void setBucketWebsite(String bucket, BucketWebsiteConfiguration config) {
//        buildClient().setBucketWebsiteConfiguration(
//                new SetBucketWebsiteConfigurationRequest(bucket, config));
//    }
//
//    public BucketWebsiteConfiguration getBucketWebsite() {
//        return getBucketWebsite(prop.getBucketName());
//    }
//
//    public BucketWebsiteConfiguration getBucketWebsite(String bucket) {
//        return buildClient().getBucketWebsiteConfiguration(
//                new GetBucketWebsiteConfigurationRequest(bucket));
//    }
//
//    public void deleteBucketWebsite() {
//        deleteBucketWebsite(prop.getBucketName());
//    }
//
//    public void deleteBucketWebsite(String bucket) {
//        buildClient().deleteBucketWebsiteConfiguration(
//                new DeleteBucketWebsiteConfigurationRequest(bucket));
//    }
//
//    public void setBucketTagging(BucketTaggingConfiguration config) {
//        setBucketTagging(prop.getBucketName(), config);
//    }
//
//    public void setBucketTagging(String bucket, BucketTaggingConfiguration config) {
//        buildClient().setBucketTaggingConfiguration(
//                new SetBucketTaggingConfigurationRequest(bucket, config));
//    }
//
//    public BucketTaggingConfiguration getBucketTagging() {
//        return getBucketTagging(prop.getBucketName());
//    }
//
//    public BucketTaggingConfiguration getBucketTagging(String bucket) {
//        return buildClient().getBucketTaggingConfiguration(
//                new GetBucketTaggingConfigurationRequest(bucket));
//    }
//
//    public void deleteBucketTagging() {
//        deleteBucketTagging(prop.getBucketName());
//    }
//
//    public void deleteBucketTagging(String bucket) {
//        buildClient().deleteBucketTaggingConfiguration(
//                new DeleteBucketTaggingConfigurationRequest(bucket));
//    }
//
//    public void setBucketCors(BucketCrossOriginConfiguration config) {
//        setBucketCors(prop.getBucketName(), config);
//    }
//
//    public void setBucketCors(String bucket, BucketCrossOriginConfiguration config) {
//        buildClient().setBucketCrossOriginConfiguration(
//                new SetBucketCrossOriginConfigurationRequest(bucket, config));
//    }
//
//    public BucketCrossOriginConfiguration getBucketCors() {
//        return getBucketCors(prop.getBucketName());
//    }
//
//    public BucketCrossOriginConfiguration getBucketCors(String bucket) {
//        return buildClient().getBucketCrossOriginConfiguration(
//                new GetBucketCrossOriginConfigurationRequest(bucket));
//    }
//
//    public void deleteBucketCors() {
//        deleteBucketCors(prop.getBucketName());
//    }
//
//    public void deleteBucketCors(String bucket) {
//        buildClient().deleteBucketCrossOriginConfiguration(
//                new DeleteBucketCrossOriginConfigurationRequest(bucket));
//    }
//
//    public void setBucketLogging(BucketLoggingConfiguration config) {
//        setBucketLogging(prop.getBucketName(), config);
//    }
//
//    public void setBucketLogging(String bucket, BucketLoggingConfiguration config) {
//        buildClient().setBucketLoggingConfiguration(
//                new SetBucketLoggingConfigurationRequest(bucket, config));
//    }
//
//    public BucketLoggingConfiguration getBucketLogging() {
//        return getBucketLogging(prop.getBucketName());
//    }
//
//    public BucketLoggingConfiguration getBucketLogging(String bucket) {
//        return buildClient().getBucketLoggingConfiguration(
//                new GetBucketLoggingConfigurationRequest(bucket));
//    }
//
//    public void setBucketVersioning(BucketVersioningConfiguration config) {
//        setBucketVersioning(prop.getBucketName(), config);
//    }
//
//    public void setBucketVersioning(String bucket, BucketVersioningConfiguration config) {
//        buildClient().setBucketVersioningConfiguration(
//                new SetBucketVersioningConfigurationRequest(bucket, config));
//    }
//
//    public BucketVersioningConfiguration getBucketVersioning() {
//        return getBucketVersioning(prop.getBucketName());
//    }
//
//    public BucketVersioningConfiguration getBucketVersioning(String bucket) {
//        return buildClient().getBucketVersioningConfiguration(bucket);
//    }
//
//    public void setObjectLock(ObjectLockConfiguration config) {
//        setObjectLock(prop.getBucketName(), config);
//    }
//
//    public void setObjectLock(String bucket, ObjectLockConfiguration config) {
//        SetObjectLockConfigurationRequest req = new SetObjectLockConfigurationRequest();
//        req.setBucketName(bucket);
//        req.setObjectLockConfiguration(config);
//        buildClient().setObjectLockConfiguration(req);
//    }
//
//    public ObjectLockConfiguration getObjectLock() {
//        return getObjectLock(prop.getBucketName());
//    }
//
//    public ObjectLockConfiguration getObjectLock(String bucket) {
//        GetObjectLockConfigurationRequest req = new GetObjectLockConfigurationRequest();
//        req.setBucketName(bucket);
//        return buildClient().getObjectLockConfiguration(req)
//                .getObjectLockConfiguration();
//    }
//
//    /* ---------------------------------------------------------
//       对象级基础操作
//       --------------------------------------------------------- */
//
//    public S3Object getObject(String key) {
//        return getObject(prop.getBucketName(), key);
//    }
//
//    public S3Object getObject(String bucket, String key) {
//        try {
//            return buildClient().getObject(bucket, key);
//        } catch (Exception e) {
//            log.error("获取对象失败 bucket={}, key={}: {}", bucket, key, e.getMessage());
//            throw e;
//        }
//    }
//
//    public void download(String key, String localPath) throws IOException {
//        download(prop.getBucketName(), key, localPath);
//    }
//
//    public void download(String bucket, String key, String localPath) throws IOException {
//        try (S3Object obj = getObject(bucket, key);
//             S3ObjectInputStream in = obj.getObjectContent();
//             FileOutputStream out = new FileOutputStream(localPath)) {
//            IOUtils.copy(in, out);
//            log.info("文件下载成功: {} -> {}", key, localPath);
//        } catch (IOException e) {
//            log.error("文件下载失败: {}", e.getMessage());
//            throw e;
//        }
//    }
//
//    public void deleteObject(String key) {
//        deleteObject(prop.getBucketName(), key);
//    }
//
//    public void deleteObject(String bucket, String key) {
//        try {
//            buildClient().deleteObject(bucket, key);
//            log.info("删除对象成功: bucket={}, key={}", bucket, key);
//        } catch (Exception e) {
//            log.error("删除对象失败: {}", e.getMessage());
//            throw e;
//        }
//    }
//
//    public void deleteVersion(String key, String versionId) {
//        deleteVersion(prop.getBucketName(), key, versionId);
//    }
//
//    public void deleteVersion(String bucket, String key, String versionId) {
//        buildClient().deleteVersion(new DeleteVersionRequest(bucket, key, versionId));
//    }
//
//    public DeleteObjectsResult deleteObjects(List<DeleteObjectsRequest.KeyVersion> keys, boolean quiet) {
//        return deleteObjects(prop.getBucketName(), keys, quiet);
//    }
//
//    public DeleteObjectsResult deleteObjects(String bucket, List<DeleteObjectsRequest.KeyVersion> keys, boolean quiet) {
//        DeleteObjectsRequest req = new DeleteObjectsRequest(bucket);
//        req.setKeys(keys);
//        req.setQuiet(quiet);
//        return buildClient().deleteObjects(req);
//    }
//
//    public CopyObjectResult copyObject(String srcKey, String dstBucket, String dstKey) {
//        return copyObject(prop.getBucketName(), srcKey, dstBucket, dstKey);
//    }
//
//    public CopyObjectResult copyObject(String srcBucket, String srcKey, String dstBucket, String dstKey) {
//        return buildClient().copyObject(srcBucket, srcKey, dstBucket, dstKey);
//    }
//
//    public ObjectMetadata getObjectMetadata(String key) {
//        return getObjectMetadata(prop.getBucketName(), key);
//    }
//
//    public ObjectMetadata getObjectMetadata(String bucket, String key) {
//        return buildClient().getObjectMetadata(bucket, key);
//    }
//
//    public void setObjectAcl(String key, CannedAccessControlList acl) {
//        setObjectAcl(prop.getBucketName(), key, acl);
//    }
//
//    public void setObjectAcl(String bucket, String key, CannedAccessControlList acl) {
//        buildClient().setObjectAcl(bucket, key, acl);
//    }
//
//    public AccessControlList getObjectAcl(String key) {
//        return getObjectAcl(prop.getBucketName(), key);
//    }
//
//    public AccessControlList getObjectAcl(String bucket, String key) {
//        return buildClient().getObjectAcl(bucket, key);
//    }
//
//    public void setObjectTagging(String key, ObjectTagging tagging) {
//        setObjectTagging(prop.getBucketName(), key, tagging);
//    }
//
//    public void setObjectTagging(String bucket, String key, ObjectTagging tagging) {
//        buildClient().setObjectTagging(new SetObjectTaggingRequest(bucket, key, tagging));
//    }
//
//    public List<Tag> getObjectTagging(String key) {
//        return getObjectTagging(prop.getBucketName(), key);
//    }
//
//    public List<Tag> getObjectTagging(String bucket, String key) {
//        GetObjectTaggingResult result = buildClient().getObjectTagging(new GetObjectTaggingRequest(bucket, key));
//        return result == null ? Collections.emptyList() : result.getTagSet();
//    }
//
//    public void deleteObjectTagging(String key) {
//        deleteObjectTagging(prop.getBucketName(), key);
//    }
//
//    public void deleteObjectTagging(String bucket, String key) {
//        buildClient().deleteObjectTagging(new DeleteObjectTaggingRequest(bucket, key));
//    }
//
//    public void setObjectRetention(String key, ObjectLockRetention retention) {
//        setObjectRetention(prop.getBucketName(), key, retention);
//    }
//
//    public void setObjectRetention(String bucket, String key, ObjectLockRetention retention) {
//        SetObjectRetentionRequest req = new SetObjectRetentionRequest();
//        req.setBucketName(bucket);
//        req.setKey(key);
//        req.setRetention(retention);
//        buildClient().setObjectRetention(req);
//    }
//
//    public ObjectLockRetention getObjectRetention(String key) {
//        return getObjectRetention(prop.getBucketName(), key);
//    }
//
//    public ObjectLockRetention getObjectRetention(String bucket, String key) {
//        GetObjectRetentionRequest req = new GetObjectRetentionRequest();
//        req.setBucketName(bucket);
//        req.setKey(key);
//        return buildClient().getObjectRetention(req).getRetention();
//    }
//
//    public void setObjectLegalHold(String key, boolean hold) {
//        setObjectLegalHold(prop.getBucketName(), key, hold);
//    }
//
//    public void setObjectLegalHold(String bucket, String key, boolean hold) {
//        ObjectLockLegalHold legal = new ObjectLockLegalHold();
//        legal.setStatus(hold ? "ON" : "OFF");
//
//        SetObjectLegalHoldRequest req = new SetObjectLegalHoldRequest();
//        req.setBucketName(bucket);
//        req.setKey(key);
//        req.setLegalHold(legal);
//        buildClient().setObjectLegalHold(req);
//    }
//
//    public ObjectLockLegalHold getObjectLegalHold(String key) {
//        return getObjectLegalHold(prop.getBucketName(), key);
//    }
//
//    public ObjectLockLegalHold getObjectLegalHold(String bucket, String key) {
//        GetObjectLegalHoldRequest req = new GetObjectLegalHoldRequest();
//        req.setBucketName(bucket);
//        req.setKey(key);
//        return buildClient().getObjectLegalHold(req).getLegalHold();
//    }
//
//    public PutSymlinkResult putSymlink(String symlink, String target) {
//        return putSymlink(prop.getBucketName(), symlink, target);
//    }
//
//    public PutSymlinkResult putSymlink(String bucket, String symlink, String target) {
//        String url = prop.getEndPoint().replaceAll("/$", "") + "/" + bucket + "/" + symlink;
//        HttpPut put = new HttpPut(url);
//        put.addHeader("x-zos-symlink-target", target);
//        put.addHeader("Content-Length", "0");
//
//        // 签名
//        ZosV4Signer.sign(put,
//                prop.getEndPoint(),
//                "cn",
//                "s3",
//                prop.getAccessKey(),
//                prop.getSecretKey());
//
//        try (CloseableHttpClient http = HttpClients.createDefault();
//             CloseableHttpResponse resp = http.execute(put)) {
//
//            int code = resp.getStatusLine().getStatusCode();
//            if (code != 200) {
//                throw new RuntimeException("putSymlink error " + code);
//            }
//            PutSymlinkResult result = new PutSymlinkResult();
//            Header ver = resp.getFirstHeader("x-zos-version-id");
//            if (ver != null) result.setVersionId(ver.getValue());
//            return result;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public GetSymlinkResult getSymlink(String symlink) {
//        return getSymlink(prop.getBucketName(), symlink);
//    }
//
//    public GetSymlinkResult getSymlink(String bucket, String symlink) {
//        String url = prop.getEndPoint().replaceAll("/$", "") + "/" + bucket + "/" + symlink;
//        HttpHead head = new HttpHead(url);
//
//        // 签名
//        ZosSignableRequestAdapter adapter = new ZosSignableRequestAdapter(head, prop.getEndPoint());
//        AWSCredentials cred = new BasicAWSCredentials(prop.getAccessKey(), prop.getSecretKey());
//        AWS4Signer signer = new AWS4Signer();
//        signer.setServiceName("s3");
//        signer.setRegionName("cn");
//        signer.sign(adapter, cred);
//        adapter.writeBackHeaders();   // 把头写回 HttpHead
//
//        try (CloseableHttpClient http = HttpClients.createDefault();
//             CloseableHttpResponse resp = http.execute(head)) {
//
//            int code = resp.getStatusLine().getStatusCode();
//            if (code != 200) {
//                throw new RuntimeException("getSymlink error " + code);
//            }
//            GetSymlinkResult result = new GetSymlinkResult();
//            Header target = resp.getFirstHeader("x-zos-symlink-target");
//            if (target != null) result.setSymlinkTarget(target.getValue());
//            Header ver = resp.getFirstHeader("x-zos-version-id");
//            if (ver != null) result.setVersionId(ver.getValue());
//            return result;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public RestoreObjectResult restoreObject(String key, int days) {
//        return restoreObject(prop.getBucketName(), key, days);
//    }
//
//    public RestoreObjectResult restoreObject(String bucket, String key, int days) {
//        RestoreObjectRequest req = new RestoreObjectRequest(bucket, key);
//        req.setExpirationInDays(days);
//        return buildClient().restoreObjectV2(req);
//    }
//
//    /* ---------------------------------------------------------
//       分片上传
//       --------------------------------------------------------- */
//    public InitiateMultipartUploadResult initiateMultipartUpload(String key) {
//        return initiateMultipartUpload(prop.getBucketName(), key);
//    }
//
//    public InitiateMultipartUploadResult initiateMultipartUpload(String bucket, String key) {
//        return buildClient().initiateMultipartUpload(new InitiateMultipartUploadRequest(bucket, key));
//    }
//
//    public UploadPartResult uploadPart(String key, String uploadId, int partNumber,
//                                       File partFile, long offset, long size) {
//        return uploadPart(prop.getBucketName(), key, uploadId, partNumber, partFile, offset, size);
//    }
//
//    public UploadPartResult uploadPart(String bucket, String key, String uploadId, int partNumber,
//                                       File partFile, long offset, long size) {
//        UploadPartRequest req = new UploadPartRequest()
//                .withBucketName(bucket)
//                .withKey(key)
//                .withUploadId(uploadId)
//                .withPartNumber(partNumber)
//                .withFile(partFile)
//                .withFileOffset(offset)
//                .withPartSize(size);
//        return buildClient().uploadPart(req);
//    }
//
//    public CompleteMultipartUploadResult completeMultipartUpload(String key, String uploadId,
//                                                                 List<PartETag> partETags) {
//        return completeMultipartUpload(prop.getBucketName(), key, uploadId, partETags);
//    }
//
//    public CompleteMultipartUploadResult completeMultipartUpload(String bucket, String key, String uploadId,
//                                                                 List<PartETag> partETags) {
//        return buildClient().completeMultipartUpload(
//                new CompleteMultipartUploadRequest(bucket, key, uploadId, partETags));
//    }
//
//    public void abortMultipartUpload(String key, String uploadId) {
//        abortMultipartUpload(prop.getBucketName(), key, uploadId);
//    }
//
//    public void abortMultipartUpload(String bucket, String key, String uploadId) {
//        buildClient().abortMultipartUpload(new AbortMultipartUploadRequest(bucket, key, uploadId));
//    }
//
//    public PartListing listParts(String key, String uploadId) {
//        return listParts(prop.getBucketName(), key, uploadId);
//    }
//
//    public PartListing listParts(String bucket, String key, String uploadId) {
//        return buildClient().listParts(new ListPartsRequest(bucket, key, uploadId));
//    }
//
//    public MultipartUploadListing listMultipartUploads() {
//        return listMultipartUploads(prop.getBucketName());
//    }
//
//    public MultipartUploadListing listMultipartUploads(String bucket) {
//        return buildClient().listMultipartUploads(new ListMultipartUploadsRequest(bucket));
//    }
//
//    /* ---------------------------------------------------------
//       断点续传
//       --------------------------------------------------------- */
//    public ResumeUploadResult resumeUploadFile(ResumeUploadRequest request) {
//        TransferManager tm = TransferManagerBuilder.standard()
//                .withS3Client(buildClient())
//                .build();
//
//        PutObjectRequest putRequest = new PutObjectRequest(
//                request.getBucketName(),
//                request.getKey(),
//                new File(request.getFilePath()))
//                .withMetadata(request.getObjectMetadata());   // 不要 ACL/权限字段
//
//        Upload upload = tm.upload(putRequest);
//        try {
//            upload.waitForCompletion();
//            return new ResumeUploadResult();   // 自行封装
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new RuntimeException(e);
//        } finally {
//            tm.shutdownNow(false);
//        }
//    }
//
//    /* ---------------------------------------------------------
//       预签名 URL（外网 domain）
//       --------------------------------------------------------- */
//    public URL generatePresignedUrl(String key, HttpMethod method, Date expiration) {
//        return generatePresignedUrl(prop.getBucketName(), key, method, expiration);
//    }
//
//    public URL generatePresignedUrl(String bucket, String key, HttpMethod method, Date expiration) {
//        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key, method);
//        req.setExpiration(expiration);
//        return buildClient().generatePresignedUrl(req);
//    }
//
//    /* ---------------------------------------------------------
//       图片处理（持久化）
//       --------------------------------------------------------- */
//    public void processImage(String srcKey, String dstKey, String process) {
//        processImage(prop.getBucketName(), srcKey, prop.getBucketName(), dstKey, process);
//    }
//
//    public void processImage(String srcBucket, String srcKey,
//                             String dstBucket, String dstKey, String process) {
//
//        String url = prop.getEndPoint().replaceAll("/$", "") + "/" + dstBucket + "/" + dstKey;
//        HttpPost post = new HttpPost(url);
//
//        // 表单字段
//        List<NameValuePair> form = new ArrayList<>();
//        form.add(new BasicNameValuePair("x-zos-process", process));
//        form.add(new BasicNameValuePair("x-zos-copy-source", srcBucket + "/" + srcKey));
//        post.setEntity(new UrlEncodedFormEntity(form, StandardCharsets.UTF_8));
//
//        // 签名
//        ZosSignableRequestAdapter adapter = new ZosSignableRequestAdapter(post, prop.getEndPoint());
//        AWSCredentials cred = new BasicAWSCredentials(prop.getAccessKey(), prop.getSecretKey());
//        AWS4Signer signer = new AWS4Signer();
//        signer.setServiceName("s3");
//        signer.setRegionName("cn");
//        signer.sign(adapter, cred);
//        adapter.writeBackHeaders();
//
//        try (CloseableHttpClient http = HttpClients.createDefault();
//             CloseableHttpResponse resp = http.execute(post)) {
//
//            int code = resp.getStatusLine().getStatusCode();
//            if (code != 200) {
//                throw new RuntimeException("processImage error " + code +
//                        " - " + EntityUtils.toString(resp.getEntity()));
//            }
//            log.info("processImage success: {} -> {}", srcKey, dstKey);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /* ---------------------------------------------------------
//       批量场景
//       --------------------------------------------------------- */
//    public void batchRestore(String prefix, int days) {
//        batchRestore(prop.getBucketName(), prefix, days);
//    }
//
//    public void batchRestore(String bucket, String prefix, int days) {
//        AmazonS3 s3 = buildClient();
//        ObjectListing list;
//        String marker = null;
//        do {
//            ListObjectsRequest req = new ListObjectsRequest(bucket, prefix, marker, null, 1000);
//            list = s3.listObjects(req);
//            for (S3ObjectSummary s : list.getObjectSummaries()) {
//                if ("GLACIER".equals(s.getStorageClass())) {
//                    restoreObject(bucket, s.getKey(), days);
//                }
//            }
//            marker = list.getNextMarker();
//        } while (list.isTruncated());
//    }
//
//    public void batchCleanMultipartUploads() {
//        batchCleanMultipartUploads(prop.getBucketName());
//    }
//
//    public void batchCleanMultipartUploads(String bucket) {
//        MultipartUploadListing uploads = listMultipartUploads(bucket);
//        for (MultipartUpload u : uploads.getMultipartUploads()) {
//            abortMultipartUpload(bucket, u.getKey(), u.getUploadId());
//        }
//    }
//
//    /* ---------------------------------------------------------
//       服务端加密上传 / 下载
//       --------------------------------------------------------- */
//    public PutObjectResult putObjectEncrypted(String key, File file, SecretKey aesKey) {
//        return putObjectEncrypted(prop.getBucketName(), key, file, aesKey);
//    }
//
//    public PutObjectResult putObjectEncrypted(String bucket, String key, File file, SecretKey aesKey) {
//        AmazonS3EncryptionV2 encClient = buildEncryptionClient(aesKey);
//        return encClient.putObject(bucket, key, file);
//    }
//
//    public void downloadEncrypted(String key, String localPath, SecretKey aesKey) throws IOException {
//        downloadEncrypted(prop.getBucketName(), key, localPath, aesKey);
//    }
//
//    public void downloadEncrypted(String bucket, String key, String localPath, SecretKey aesKey) throws IOException {
//        AmazonS3EncryptionV2 encClient = buildEncryptionClient(aesKey);
//        try (S3Object obj = encClient.getObject(bucket, key);
//             S3ObjectInputStream in = obj.getObjectContent();
//             FileOutputStream out = new FileOutputStream(localPath)) {
//            IOUtils.copy(in, out);
//        }
//    }
//
//    private AmazonS3EncryptionV2 buildEncryptionClient(SecretKey aesKey) {
//        AWSCredentials cred = new BasicAWSCredentials(prop.getAccessKey(), prop.getSecretKey());
//        return AmazonS3EncryptionClientV2Builder.standard()
//                .withEndpointConfiguration(
//                        new AwsClientBuilder.EndpointConfiguration(prop.getEndPoint(), ""))
//                .withCredentials(new AWSStaticCredentialsProvider(cred))
//                .withCryptoConfiguration(new CryptoConfigurationV2()
//                        .withCryptoMode(CryptoMode.StrictAuthenticatedEncryption))
//                .withEncryptionMaterialsProvider(new StaticEncryptionMaterialsProvider(
//                        new EncryptionMaterials(aesKey)))
//                .build();
//    }
//
//    /* ---------------------------------------------------------
//       STS 临时凭证
//       --------------------------------------------------------- */
//    public Credentials assumeRole(String roleArn, String sessionName, int seconds) {
//        AWSCredentials cred = new BasicAWSCredentials(prop.getAccessKey(), prop.getSecretKey());
//        AWSSecurityTokenService sts = AWSSecurityTokenServiceClientBuilder.standard()
//                .withEndpointConfiguration(
//                        new AwsClientBuilder.EndpointConfiguration(prop.getEndPoint(), ""))
//                .withCredentials(new AWSStaticCredentialsProvider(cred))
//                .build();
//        AssumeRoleRequest req = new AssumeRoleRequest()
//                .withRoleArn(roleArn)
//                .withRoleSessionName(sessionName)
//                .withDurationSeconds(seconds);
//        return sts.assumeRole(req).getCredentials();
//    }
//
//    /* ---------------------------------------------------------
//       外链地址（外网 domain）拼接
//       --------------------------------------------------------- */
//    public String getExternalUrl(String key) {
//        if (StringUtils.hasText(prop.getBasePath())) {
//            key = prop.getBasePath().replaceAll("/$", "") + "/" + key.replaceAll("^/", "");
//        }
//        return prop.getDomain() + key;
//    }
//
//    /* ---------------------------------------------------------
//       新增：列出对象
//       --------------------------------------------------------- */
//    public ObjectListing listObjects(String prefix) {
//        return listObjects(prop.getBucketName(), prefix);
//    }
//
//    public ObjectListing listObjects(String bucket, String prefix) {
//        return buildClient().listObjects(bucket, prefix);
//    }
//
//    public ObjectListing listObjects(String bucket, String prefix, String marker, Integer maxKeys) {
//        return buildClient().listObjects(new ListObjectsRequest(bucket, prefix, marker, null, maxKeys));
//    }

}