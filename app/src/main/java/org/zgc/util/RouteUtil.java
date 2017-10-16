package org.zgc.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zgc.app.URL;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 2017/10/2
 */
public class RouteUtil {
    private static RouteUtil routeUtil = null;
    private static Connection.Response response = null;

    private final String DEFAULT_MAC_ADDRESS = "58:44:98:EF:77:EF F0:43:47:A2:F8:0F 64:CC:2E:23:AB:EC 88:1F:A1:11:F1:46";

    private RouteUtil() {
        try {
            login();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RouteUtil getInstance() {
        if (routeUtil == null) {
            synchronized (RouteUtil.class) {
                if (routeUtil == null) {
                    routeUtil = new RouteUtil();
                }
            }
        }
        return routeUtil;
    }

    public void addMac(String macAdd) throws IOException {
        if (StringUtils.isEmpty(macAdd)) {
            return;
        }
        String macList = getMacList();
        macList += " " + macAdd;
        Document document = Jsoup.connect(URL.MAC_SUBMIT)
                .data("GO", "wireless_filter.asp", "maclist", macList, "ssidIndex", "0", "FilterMode", "allow")
                .cookies(response.cookies())
                .post();

        System.out.println(document.html());
    }

    public String getMacList() {
//        List<String> list = new ArrayList<>();
        String macList = null;
        Map<String, String> cookies = response.cookies();
        Document objectDoc = null;
        try {
            objectDoc = Jsoup.connect(URL.FILTER_LIST)
                    .cookies(cookies)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = objectDoc.getElementsByTag("script");
        for (Element e : elements) {
            //取得JS变量数组
            String[] data = e.data().toString().split("var");

            //取得单个JS变量
            for (String variable : data) {
                //过滤variable为空的数据
                if (variable.contains("res = ")) {

                    Pattern p = Pattern.compile("res = \"(.+?)\"");
                    Matcher m = p.matcher(variable);
                    m.find();
//                    String macList = m.group(1);
//                    if (StringUtils.isNotEmpty(macList)) {
//                        list.addAll(Arrays.asList(macList.split(" ")));
//                    }
                    macList = m.group(1);
                }
            }
        }

        return macList;
    }

    private void login() throws IOException {
        response = Jsoup.connect(URL.LOGIN_URL)
                .data("Username", "admin", "Password", "123456", "checkEn", "0")
                .method(Connection.Method.POST)
                .execute();
    }

    public boolean resetMac() {
        try {
            Document document = Jsoup.connect(URL.MAC_SUBMIT)
                    .data("GO", "wireless_filter.asp", "maclist", DEFAULT_MAC_ADDRESS, "ssidIndex", "0", "FilterMode", "allow")
                    .cookies(response.cookies())
                    .post();
            System.out.println(document.html());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
