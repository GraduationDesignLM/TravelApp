package com.mao.travelapp.sdk;

/**
 * Created by mao on 2016/11/9.
 */
class HttpManager {

    final static int port = 8080;
    final static String BASE_URL = "http://112.74.81.157:" + port + "/";
   // final static String BASE_URL = "http://10.0.2.2:" + port + "/";
 //   final static String BASE_URL = "http://192.168.191.1:" + port + "/";
    //final static String BASE_URL = "http://10.242.7.115:" + port + "/";
    //final static String BASE_URL = "http://10.242.10.144:" + port + "/";
 //   final static String BASE_URL = "http://192.168.43.171:" + port + "/";
    final static String DB_SERVLET_URL = "TravelServer/servlet/CommonDBServlet";

    /** 上传一个文件URL后缀 */
    final static String UPLOADFILE_SERVLET_URL = "TravelServer/servlet/UploadFileServlet";

}
