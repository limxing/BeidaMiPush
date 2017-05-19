package me.leefeng;

import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;
import com.xiaomi.xmpush.server.TargetedMessage;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String MY_PACKAGE_NAME = "me.leefeng.beida";
    private static final java.lang.String APP_SECRET_KEY = "sAl7N0C+tpiUm14UGryanA==";

    public static void main(String[] args) {
        // write your code here
        new Thread() {
            @Override
            public void run() {


                String pathUrl = "http://www.pkudl.cn/xwzx.asp?aid=449&act=2";
// 建立连接
                try {
                    URL url = new URL(pathUrl);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
// //设置连接属性
                    httpConn.setDoOutput(true);// 使用 URL 连接进行输出
                    httpConn.setDoInput(true);// 使用 URL 连接进行输入
                    httpConn.setUseCaches(false);// 忽略缓存
                    httpConn.setRequestMethod("GET");// 设置URL请求方法
                    int responseCode = httpConn.getResponseCode();
                    if (responseCode == 200) {

                        StringBuffer sb = new StringBuffer();
                        String readLine;
                        BufferedReader responseReader;
// 处理响应流，必须与服务器响应流输出的编码一致
                        responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
                        while ((readLine = responseReader.readLine()) != null) {
                            sb.append(readLine).append("\n");
                        }
                        responseReader.close();
                        int start = sb.indexOf("<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"2\">");
                        sb.delete(0, start);
                        int end = sb.indexOf("</table>");

                        String s = sb.substring(0, end);

                        s = "<!doctype html><html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body>" + s + "</table></body></html>";

                        System.out.print(s);
                        testSendToAccount(s);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }

    /**
     * 创建一个MessageDemo
     *
     * @return
     * @throws Exception
     */
    private static Message buildMessage() throws Exception {
        String PACKAGENAME = "com.xiaomi.mipushdemo";
        String messagePayload = "This is a message";
        String title = "notification title";
        String description = "notification description";
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(MY_PACKAGE_NAME)
                .passThrough(1)  //消息使用透传方式
                .notifyType(1)     // 使用默认提示音提示
                .extra("flow_control", "4000")     // 设置平滑推送, 推送速度4000每秒(qps=4000)
                .build();
        return message;
    }


    private void sendMessage() throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(APP_SECRET_KEY);
        String messagePayload = "This is a message";
        String title = "notification title";
        String description = "notification description";
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(MY_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        sender.send(message, "regId", 0); //根据regID，发送消息到指定设备上，不重试。
    }


    private void sendMessages() throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(APP_SECRET_KEY);
        List<TargetedMessage> messages = new ArrayList<>();
        TargetedMessage targetedMessage1 = new TargetedMessage();
        targetedMessage1.setTarget(TargetedMessage.TARGET_TYPE_ALIAS, "alias1");
        String messagePayload1 = "This is a message1";
        String title1 = "notification title1";
        String description1 = "notification description1";
        Message message1 = new Message.Builder()
                .title(title1)
                .description(description1).payload(messagePayload1)
                .restrictedPackageName(MY_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        targetedMessage1.setMessage(message1);
        messages.add(targetedMessage1);
        TargetedMessage targetedMessage2 = new TargetedMessage();
        targetedMessage1.setTarget(TargetedMessage.TARGET_TYPE_ALIAS, "alias2");
        String messagePayload2 = "This is a message2";
        String title2 = "notification title2";
        String description2 = "notification description2";
        Message message2 = new Message.Builder()
                .title(title2)
                .description(description2).payload(messagePayload2)
                .restrictedPackageName(MY_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        targetedMessage2.setMessage(message2);
        messages.add(targetedMessage2);
        sender.send(messages, 0); //根据alias，发送消息到指定设备上，不重试。

    }

    private void sendMessageToAlias() throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(APP_SECRET_KEY);
        String messagePayload = "This is a message";
        String title = "notification title";
        String description = "notification description";
        String alias = "testAlias";    //alias非空白，不能包含逗号，长度小于128。
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(MY_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        sender.sendToAlias(message, alias, 0); //根据alias，发送消息到指定设备上，不重试。

    }

    private void sendMessageToAliases() throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(APP_SECRET_KEY);
        String messagePayload = "This is a message";
        String title = "notification title";
        String description = "notification description";
        List<String> aliasList = new ArrayList<String>();
        aliasList.add("testAlias1");  //alias非空白，不能包含逗号，长度小于128。
        aliasList.add("testAlias2");  //alias非空白，不能包含逗号，长度小于128。
        aliasList.add("testAlias3");  //alias非空白，不能包含逗号，长度小于128。
        Message message = new Message.Builder()
                .title(title)

                .description(description).payload(messagePayload)
                .restrictedPackageName(MY_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        sender.sendToAlias(message, aliasList, 0); //根据aliasList，发送消息到指定设备上，不重试。
    }

    private void sendBroadcast() throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(APP_SECRET_KEY);
        String messagePayload = "This is a message";
        String title = "notification title";
        String description = "notification description";
        String topic = "testTopic";
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(MY_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        sender.broadcast(message, topic, 0); //根据topic，发送消息到指定一组设备上，不重试。
    }

    private static void testSendToAccount(String content) throws IOException, ParseException {
        Sender sender = new Sender(APP_SECRET_KEY);
        String messagePayload = "This is a message";
        String title = "notification title";
        String description = "notification description";
        String topic = "testTopic";
        Message message = new Message.Builder()
                .title("title")
                .description("description").payload(content)
                .restrictedPackageName(MY_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        sender.sendToUserAccount(message,"fee75863de",0);
    }
}
