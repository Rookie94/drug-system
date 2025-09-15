package com.ruoyi.common.utils.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.config.MinioConfig;
import com.ruoyi.common.core.domain.ObjectItem;
import com.ruoyi.common.utils.StringUtils;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.utils.ServletUtils;

import io.minio.http.Method;

/**
 * Minio 文件存储工具类
 *
 * @author ruoyi
 */
public class MinioUtil
{

    private static MinioClient minioClient=MinioConfig.getMinioClient();

    private static String defaultBucketName=MinioConfig.getBucketName();

    private static String endPointUrl=MinioConfig.getUrl();
    private static String proxyUrl=MinioConfig.getProxyUrl();

    /**
     * @description 判断bucket是否存在，不存在则创建
     * @author ze.chen
     * @date 2022/8/30 14:12
     * @param bucketName 桶名
     **/
    public static boolean existBucket(String bucketName) {
        boolean found = false;
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            found = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    public static Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    public static Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 查看文件对象
     *
     * @param bucketName 存储bucket名称
     * @return 存储bucket内文件对象信息
     */
    public static List<ObjectItem> listObjects(String bucketName) {
        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        List<ObjectItem> objectItems = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                Item item = result.get();
                ObjectItem objectItem = new ObjectItem();
                objectItem.setObjectName(item.objectName());
                objectItem.setSize(item.size());
                objectItems.add(objectItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return objectItems;
    }

    /**
     * 批量删除文件对象
     *
     * @param bucketName 存储bucket名称
     * @param objects 对象名称集合
     */
    public static Iterable<Result<DeleteError>> removeObjects(String bucketName, List<String> objects) {
        List<DeleteObject> dos = objects.stream().map(DeleteObject::new).collect(Collectors.toList());
        return minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos).build());
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储bucket名称
     * @param objectName 文件名称
     */
    public static boolean removeObject(String bucketName, String objectName) throws Exception {
        boolean exist = existBucket(bucketName);
        if (exist) {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        }
        return false;
    }

    /**
     * 上传文件
     *
     * @param fileName 文件名
     * @throws IOException 写入异常
     */
    public static String uploadFile(String fileName, MultipartFile multipartFile) throws IOException
    {
        try (InputStream inputStream = multipartFile.getInputStream())
        {
            minioClient.putObject(PutObjectArgs.builder().bucket(defaultBucketName).object(fileName).stream(inputStream, multipartFile.getSize(), -1).contentType(multipartFile.getContentType()).build());
            String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(defaultBucketName).object(fileName).method(Method.GET).build());
            url = url.replace(endPointUrl,proxyUrl);
            url = url.substring(0, url.indexOf('?'));
            return ServletUtils.urlDecode(url);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 上传文件
     *
     * @param bucketName 桶名称
     * @param fileName 文件名
     * @throws IOException 写入异常
     */
    public static String uploadFile(String bucketName, String fileName, MultipartFile multipartFile) throws IOException
    {
        try (InputStream inputStream = multipartFile.getInputStream())
        {
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(inputStream, multipartFile.getSize(), -1).contentType(multipartFile.getContentType()).build());
            String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(fileName).method(Method.GET).build());
            url = url.replace(endPointUrl,proxyUrl);
            url = url.substring(0, url.indexOf('?'));
            return ServletUtils.urlDecode(url);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * @description 上传文件
     * @param multipartFile 文件资源
     * @return java.lang.String
     */
    public static List<Map<String, Object>> uploadFile(MultipartFile[] multipartFile) {
        List<Map<String, Object>> res = new ArrayList<>(multipartFile.length);
        for (MultipartFile file : multipartFile) {
            String fileName = file.getOriginalFilename();
            String[] split = fileName.split("\\.");
            if (split.length > 1) {
                fileName = split[0] + "_" + System.currentTimeMillis() + "." + split[1];
            } else {
                fileName = fileName + System.currentTimeMillis();
            }
            InputStream in = null;
            try {
                in = file.getInputStream();
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(defaultBucketName).object(fileName).stream(in, in.available(), -1)
                                .contentType(file.getContentType())
                                .build());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Map<String, Object> map = new HashMap<>(16);
            map.put("url", fileName);
            map.put("fileName", fileName);
            map.put("newFileName", FileUtils.getName(fileName));
            map.put("originalFilename", file.getOriginalFilename());
            res.add(map);
        }
        return res;
    }

    /**
     * @description 上传文件
     * @param multipartFile 文件资源
     * @return java.lang.String
     */
    public static List<Map<String, Object>> uploadFile(String bucketName,MultipartFile[] multipartFile) {
        List<Map<String, Object>> res = new ArrayList<>(multipartFile.length);
        for (MultipartFile file : multipartFile) {
            String fileName = file.getOriginalFilename();
            String[] split = fileName.split("\\.");
            if (split.length > 1) {
                fileName = split[0] + "_" + System.currentTimeMillis() + "." + split[1];
            } else {
                fileName = fileName + System.currentTimeMillis();
            }
            InputStream in = null;
            try {
                in = file.getInputStream();
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName).object(fileName).stream(in, in.available(), -1)
                                .contentType(file.getContentType())
                                .build());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Map<String, Object> map = new HashMap<>(16);
            map.put("url", fileName);
            map.put("fileName", fileName);
            map.put("newFileName", FileUtils.getName(fileName));
            map.put("originalFilename", file.getOriginalFilename());
            res.add(map);
        }
        return res;
    }

    /**
     * @description 下载文件
     *
     * @param fileName 文件名称
     * @param bucketName 桶名（传空时从默认配置的桶中查找文件下载）
     * @return org.springframework.http.ResponseEntity<byte[]>
     */
    public static ResponseEntity<byte[]> download(String fileName, String bucketName) {
        ResponseEntity<byte[]> responseEntity = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        String downloadBucketName = StringUtils.isNotBlank(bucketName) ? bucketName : bucketName;
        try {
            in = minioClient.getObject(GetObjectArgs.builder().bucket(downloadBucketName).object(fileName).build());
            out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            // 封装返回值
            byte[] bytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            try {
                headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            headers.setContentLength(bytes.length);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccessControlExposeHeaders(Collections.singletonList("*"));
            responseEntity = new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseEntity;
    }

}
