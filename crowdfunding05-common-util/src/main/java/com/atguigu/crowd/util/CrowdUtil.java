package com.atguigu.crowd.util;


import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.crowd.constant.CrowdConstant;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 判断请求的方式
 * @author ChenCheng
 * @create 2022-05-10 20:12
 */
public class CrowdUtil {


    /**
     *                发送验证码
     * @param host
     * @param path
     * @param method
     * @param appcode
     * @param phoneNum
     * @return
     */
    public static ResultEntity<String> sendCodeByShortMessage(
            String host,
            String path,
            String method,
            String appcode,
            String phoneNum
    ){
//        String host = "https://dfsns.market.alicloudapi.com";
//        String path = "/data/send_sms";
//        String method = "POST";
//        String appcode = "c62c95ce2ec140ec930eac5d306ac6c3";


        // 生成验证码
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++){
            int random = (int)(Math.random()*10);
            builder.append(random);
        }
        String code = "code:"+builder;

        Map<String, String> headers = new HashMap<String, String>();
        // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        // 根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();

        // 要发送的验证码
        bodys.put("content", code);
        // 接收验证码的手机号
        bodys.put("phone_number", phoneNum);
        bodys.put("template_id", "TPL_0000");


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            StatusLine statusLine = response.getStatusLine();
            // 获取响应的状态码
            int statusCode = statusLine.getStatusCode();
            // 成功这个值就是ok
            String reasonPhrase = statusLine.getReasonPhrase();

            // 发送成功,返回验证码返回
            if(statusCode == 200){
                return ResultEntity.successWithData(code);
            }
            return ResultEntity.failed(reasonPhrase);

        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }
    }






    /**
     *  将传入的字符串进行加密
     * @param source 需要加密的参数
     * @return 返回加密后的结果
     */
    public static String md5(String source){

        // 1.查看当前传输的明文密码是否合法
        if(source.length() == 0 && source == null ){

            // 2.密码不合法抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        // 3.对当前密码进行 md5 加密
        String algorithm = "md5";
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);

            // 4.获取明文对应的字节数组
            byte[] input = source.getBytes();

            // 5.执行加密
            byte[] output = messageDigest.digest(input);

            // 6.创建BigInteger对象
            BigInteger bigInteger = new BigInteger(1,output);

            // 7.按照 16 进制将 bigInteger 的值转换为字符串
            Integer radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();

            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return null;

    }












    /**
     *  判断当前的请求式否为ajax请求
     * @param request
     * @return
     *       ture 当前请求是ajax请求
     *       false 当前请求不是ajax请求
     */
    public static boolean judgeRequestType(HttpServletRequest request){

        String accept = request.getHeader("Accept");
        String XMLHttpRequest = request.getHeader("X-Requested-With");

        return (accept != null  && accept.length() > 0 && accept.contains("application/json"))
                ||
                (XMLHttpRequest != null  && XMLHttpRequest.length() > 0 && "X-Requested-With".equals(XMLHttpRequest));
    }


    /**
     *         上传文件
     * @param endPoint
     * @param accessKeyId
     * @param accessKeySecret
     * @param inputStream
     * @param bucketName
     * @param bucketDomain
     * @param originalName        这是需要上传图片的原始位置
     * @return
     */
    public static ResultEntity<String> uploadFileToOSS(
            String endPoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName

    ){


        // 1.创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

        // 2.生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // 3.生成上传文件在OSS服务器上保存的文件名,通过uuid生成随机uuid，将其中的“-”删去（替换成空字符串）
        String fileMainName = UUID.randomUUID().toString().replace("-", "");

        // 4.从原始的文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));

        // 5.使用目录、文件主体名称、文件扩展名拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;


        try {
            // 6.调用OSS客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName,objectName,inputStream);

            // 7.从响应结果中获取具体的响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();

            // 8.根据响应状态判断是否成功
            if (responseMessage == null) {
                // 9.拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;

                // 返回成功，并带上访问路径
                return ResultEntity.successWithData(ossFileAccessPath);
            }else {
                // 10.获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 没有成功 获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();

                return ResultEntity.failed("当前响应状态码=" + statusCode + " 错误消息=" + errorMessage);
            }
        } catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }


    }






}
