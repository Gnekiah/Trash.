/**
 * 服务器端程序维护一张客户表，表的每个单元为一个客户机信息，每个客户信息格式为：
 *    ID    String，客户机的ID，为任意字母、数字或其组合
 *    IP    String，客户机的IP
 *    port  int，客户机开放的端口
 *    info  描述信息，客户端用户的自我简述
 *    stamp 时间戳，用于超时检测
 * 在线用户名单刷新机制
 *    客户端每隔1秒向服务器端发送心跳包，服务器接收到心跳包后，刷新该客户的信息和时间戳，并返回在线用户列表给客户
 *    当服务器端在设定的超时时间段内没有收到某个客户端的心跳包，就将该客户信息删除
 *    
 * 心跳包格式：
 *    ID|port|info|udpPort
 *    ID、port和info通过“|”符号隔开，ID和info中不得存在“|”符号,udpPort用于将用户列表返回给客户端
 * 服务器接收信息包格式：
 *    发送者ID|接收者ID|Message
 * 服务器转发数据包格式：
 *    发送者ID|接收者ID|Message
 * 服务器程序返回的用户列表格式：
 *    用户1|用户2|用户3|.....|用户n
 *    其中用户n的格式为：ID&IP&port&info
 * 目录/文件共享请求：
 *    request|filename/dirname
 *    
 */

package miola;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;

public class Miola {

    private final int tcplistenport = 10087;
    
    private final int buffSize = 1000;
    
    private LinkedList<HostInfo> hostInfo = null;
    
    private Batanna batanna = null;
    
    private Miola() {
        hostInfo = new LinkedList<HostInfo>();
        batanna = new Batanna(hostInfo);
    }
    
    public void go() {
        ServerSocket server = null;
        Socket client = null;
        try {
            server = new ServerSocket(tcplistenport);
        } catch (IOException e) {
            return;
        }

        batanna.start();
        while(true) {
            try {
                client = server.accept();
                System.out.println("server tcp get message");
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));  
                String sendMsg = br.readLine();
                String[] msg = sendMsg.split("\\|");
              //  System.out.println("get message:" + msg[0] + msg[1]);
                if (msg.length != 3) {
                    continue;
                }
                HostInfo hs = null;
                Iterator iter = hostInfo.iterator();
                while (iter.hasNext()) {
                    HostInfo tmpHost = (HostInfo) iter.next();
                    if (tmpHost.getID().equals(msg[1])) {
                        hs = new HostInfo(tmpHost.getID(), tmpHost.getIP(), tmpHost.getPort(), tmpHost.getInfo());
                        System.out.println(tmpHost.getPort());
                        break;
                    }
                }
                
                if (hs == null) {
                    continue;
                }
                
                System.out.println("send message:" + sendMsg);
                Socket send = new Socket(hs.getIP(), hs.getPort());
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(send.getOutputStream())); 
                bw.write(sendMsg);
                bw.flush();
                bw.close();
                br.close();
                send.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] a) throws SocketException {
        Miola miola = new Miola();
        miola.go();
    }
}




/*
1) 点对点数据交换（P2P）：实现基于服务器转发的任意多点间的数据共享与交换。
类似P2P的QQ聊天系统，有客户端和服务器端。
服务器端记录当前在线客户列表，把客户列表发送给每一个在线客户，并实时刷新（要求基于UDP刷新和管理在线用户）。
任一个用户可以和任意其它的用户进行交互，即从在线客户列表中选择一个或一组其它客户通过服务器转发彼此进行交互；
用户间可进行点对点文件目录共享（指定一个文件目录，可远程查阅，并下载指定文件）。

1、UDP方式实时更新在线用户列表
2、聊天方式：通过服务器转发信息
3、文件共享：我发送一个共享请求，对方接收到共享请求后，将某个目录共享出来，我接收到共享目录后，点哪个文件就下载那个文件



客户端注册协议：
客户端运行后，每隔1秒钟向服务器发送一个心跳包（基于UDP），心跳包封装本机信息标签：
    本机IP
    本机port
    本机ID
    本机描述信息
    
服务器接收到心跳包后，立刻返回一个确认包（基于UDP），封装一个列表信息，列表的每个元素为一个客户机标签

客户端选中一个通信目标后，发送的文本信息直接发送到服务器，服务器负责转发文本

客户端发送文件共享请求后，数据直接发送到目标IP
*/