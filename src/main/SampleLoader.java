package main;

import main.algorithm.PathPlanning;
import main.algorithm.SamplePathPlanning;
import main.config.Config;
import main.config.ConfigInitializer;
import main.info.WorldInfo;
import main.registry.SampleWorldInitializer;
import main.standard.entities.InfoQueueMap;
import main.standard.messages.Input;
import main.standard.messages.RollerMessageInput;
import main.standard.entities.MyMqtt;
import main.standard.messages.RollerMessageOutput;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.alibaba.fastjson.*;

import java.util.*;

public class SampleLoader {

    public static void main(String[] args) throws MqttException {
        //第一步:从配置文件读取参数
        Config config = ConfigInitializer.getConfig(null);
        //第二步:根据参数初始化世界模型，即创建对象
        WorldInfo worldInfo = new SampleWorldInitializer().buildWorld(config);
        PathPlanning pathPlanning = new SamplePathPlanning(worldInfo);
        worldInfo.init();
//        创建通信模块
        MyMqtt mqttDemo = new MyMqtt("control");
        mqttDemo.subscribe(new String[]{"car1_info"},new int[]{1});

        mqttDemo.subscribe(new String[] {"car1_info","car2_info","car3_info","car4_info"}, new int[] {1,1,1,1});

        Input input = new Input(worldInfo);

        Long timeStamp = new Date().getTime();
        while(true) {
//            以1HZ的频率生成并发送小车car0轨迹信息
            Long curTime = new Date().getTime();
            if(curTime - timeStamp > 1000) {

                timeStamp = curTime;
                worldInfo.merge(input);
                pathPlanning.calc();
                for (int i =1;i<=worldInfo.getRollerNum();i++){
                    RollerMessageOutput rollerMessageOutput = pathPlanning.getOutput().getRollerMessageOutputByIndex(i);
                    if (i==3){
                        System.out.println("小车3的y坐标"+worldInfo.getRollerByIndex(i).getY());
                    }
                    System.out.println();
                    System.out.println("输出小车"+i+" 位置："+rollerMessageOutput.getX()+", "+rollerMessageOutput.getY()+" 方向:"+rollerMessageOutput.getDirection());

                    if ((i==2||i==4)&&worldInfo.isReRollerBegin()==false){
                        continue;
                    }
//                    if (i==1){
//                        rollerMessageOutput.setX(0.9);
//                        rollerMessageOutput.setY(10);
//                    }
                    MqttMessage message = mqttDemo.createMessage(JSON.toJSONString(
                            pathPlanning.getOutput().getRollerMessageOutputByIndex(i)),false,1);
                    mqttDemo.publish("car"+i+"_trajectory", message);

                }
            }
//            监听小车car0发送来的车辆信息，小车端的发送频率为2HZ
//            InfoQueueMap储存的是小车发过来消息的队列
            for (int i=1;i<=worldInfo.getRollerNum();i++){
                String rollerInfo = "car"+i+"_info";
                if(InfoQueueMap.infoMap.containsKey(rollerInfo)) {
                    if(InfoQueueMap.infoMap.get(rollerInfo).peek() != null) {
                        String x = InfoQueueMap.infoMap.get(rollerInfo).poll();
                        JSONObject jsonObject=JSONObject.parseObject(x);
                        List<String> position = (List<String>) jsonObject.get("position");
                        String direction = String.valueOf(jsonObject.get("direction"));
                        input.setPositionXOfRoller(Double.valueOf(String.valueOf(position.get(0))),i);
                        input.setPositionYOfRoller(Double.valueOf(String.valueOf(position.get(1))),i);
                        input.setDirectionOfRoller(direction,i);
                        System.out.println("输入小车"+i+"   位置："+position+",  "+" 方向："+direction);
                    }
                }
            }
            //
        }
    }

}

