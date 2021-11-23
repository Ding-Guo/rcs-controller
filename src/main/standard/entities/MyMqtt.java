package main.standard.entities;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.LinkedList;
import java.util.Queue;


public class MyMqtt{

    private String HOST = "tcp://127.0.0.1:1883";
//    private String HOST = "tcp://192.168.3.141:1883";
    private final String userName = "admin";
    private final String passWord = "password";

    public MqttClient client;
    public String clientID;

    public MyMqtt(String clientId) throws MqttException {

//        在后续项目实际使用中需要获取设备的唯一标识号用来当作clientId，取代目前人工赋值的方法
//        final String clientId = get_UUID()之类的方法;
        clientID = clientId;
        client = new MqttClient(HOST, clientId, new MemoryPersistence());
        connect();
    }

    public String getClientID() {

        return clientID;

    }
//    payload的格式后续还需要再做定义
    public MqttMessage createMessage(String payload, boolean retained, int Qos) {

        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        message.setRetained(retained);
        message.setQos(Qos);
        return message;

    }

    private void connect() {

//        设置连接相关参数
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);
//        设置遗嘱，意外断连时向"LostConnection"主题发送断连消息
        MqttTopic lostConnectionTopic =  client.getTopic("LostConnection");
        String willPayload = clientID + "Lost Connection";
        options.setWill(lostConnectionTopic, willPayload.getBytes(), 2, false);


        try {
            client.setCallback(new PushCallback());
            client.connect(options);
            System.out.println("连接成功");

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {

        try {
            client.disconnect();
            client.close();
            System.out.println("断开成功");

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void publish(String topicString, MqttMessage message) throws MqttException {

        try {
            MqttTopic topic = client.getTopic(topicString);
            MqttDeliveryToken token = topic.publish(message);

            token.waitForCompletion();
//            System.out.println("published completely!");
        }catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void subscribe(String[] topics, int[] QOSs) throws MqttException {

        try {
            client.subscribe(topics, QOSs);
            System.out.println("subscribe completely!");
        }catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public static class PushCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable throwable) {
            //连接丢失后，一般在这里面进行重连
            System.out.println("连接断开，可以做重连");
        }

        //客户端回调
        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

            // subscribe后得到的消息会执行到这里面
//            System.out.println("接收消息主题 : "+topic);
//            System.out.println("接收消息Qos : " +mqttMessage.getQos());
//            System.out.println("接收消息内容 : " +new String(mqttMessage.getPayload()));

            if(InfoQueueMap.infoMap.containsKey(topic)) {
                InfoQueueMap.infoMap.get(topic).offer(new String(mqttMessage.getPayload()));
            }else {
                Queue<String> queue = new LinkedList<>();
                queue.offer(new String(mqttMessage.getPayload()));
                InfoQueueMap.infoMap.put(topic, queue);
            }

        }
        //发送消息后的回调
        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//            System.out.println("deliveryComplete---------" );
        }

    }

}

