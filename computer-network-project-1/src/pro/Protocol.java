package pro;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Protocol {

    /**
     * ip协议的身份标识
     */
    private short idInIp = 0;

    /**
     * tcp协议的序列号
     */
    private int idInTcp = 0;

    /**
     * 应用层的数据封装 在原始数据前面添加一个8字节长度的时间戳
     * 
     * @param sourceData
     *            原始数据
     * @return 应用层的数据包
     */
    public byte[] packApp(byte[] sourceData) {
        byte[] appData = new byte[sourceData.length + 8];
        long timeStamp = System.currentTimeMillis();
        // 封装时间戳
        for (int i = 0; i < 8; i++) {
            appData[i] = (byte) (timeStamp >> (8 * (7 - i)));
        }
        // 封装原始数据
        for (int i = 0; i < sourceData.length; i++) {
            appData[i + 8] = sourceData[i];
        }
        return appData;
    }

    /**
     * TCP协议封装
     * 
     * @param sourcePort
     *            源端口
     * @param targetPort
     *            目的端口
     * @param appData
     *            应用程序数据包
     * @param sourceIp
     *            源IP
     * @param targetIp
     *            目的IP
     * @return tcp数据包
     */
    public byte[] packTcp(byte[] sourcePort, byte[] targetPort, byte[] appData, byte[] sourceIp, byte[] targetIp) {
        byte[] tcpData = new byte[appData.length + 20];
        byte[] check = new byte[2];
        byte[] visualHead = new byte[12];

        // 伪首部32位原IP与32为目的IP
        for (int i = 0; i < 4; i++) {
            visualHead[i] = sourceIp[i];
            visualHead[i + 4] = targetIp[i];
        }
        // 8位全0
        visualHead[8] = 0x00;
        // 8位协议号
        visualHead[9] = 0x06;
        // 16位tcp数据包总长度
        visualHead[10] = (byte) (tcpData.length > 255 ? tcpData.length >> 8 : 0);
        visualHead[11] = (byte) tcpData.length;

        // 16位源端口与16位目的端口
        tcpData[0] = sourcePort[0];
        tcpData[1] = sourcePort[1];
        tcpData[2] = targetPort[0];
        tcpData[3] = targetPort[1];
        // 32位序列号与32位确认号
        for (int i = 0; i < 4; i++) {
            tcpData[4 + i] = (byte) (idInTcp >> (3 - i));
            tcpData[8 + i] = (byte) ((idInTcp > 1 ? idInTcp - appData.length : idInTcp) >> (3 - i));
        }
        // 前4位为首部长度，中间6位为保留位
        // 后六位分别为URG,ACK,PSH,RST,SYN,FIN
        tcpData[12] = 0x50;
        tcpData[13] = 0x00;
        // 16为窗口大小
        tcpData[14] = 0x00;
        tcpData[15] = 0x00;
        // 16位校验和
        tcpData[16] = tcpData[17] = check[0] = check[1] = 0x00;
        // 16位紧急指针
        tcpData[18] = 0x00;
        tcpData[19] = 0x00;
        // 封装数据包
        for (int i = 0; i < appData.length; i++) {
            tcpData[20 + i] = appData[i];
        }

        // 求伪首部校验和
        for (int i = 0; i < 12; i++) {
            check[0] += visualHead[i];
            i++;
            check[1] += visualHead[i];
        }
        // 求tcp包的校验和
        if (appData.length % 2 == 0) {
            for (int i = 0; i < tcpData.length; i++) {
                check[0] += tcpData[i];
                i++;
                check[1] += tcpData[i];
            }
        } else {
            for (int i = 0; i < tcpData.length - 1; i++) {
                check[0] += tcpData[i];
                i++;
                check[1] += tcpData[i];
            }
            check[0] += tcpData[tcpData.length - 1];
        }
        tcpData[16] = (byte) ~check[0];
        tcpData[17] = (byte) ~check[1];
        return tcpData;
    }

    /**
     * 传输层，UDP协议封装
     * 
     * @param sourcePort
     *            源端口
     * @param targetPort
     *            目的端口
     * @param appData
     *            应用程序数据包
     * @param sourceIp
     *            源IP
     * @param targetIp
     *            目的IP
     * @return 返回UDP数据包
     */
    public byte[] packUdp(byte[] sourcePort, byte[] targetPort, byte[] appData, byte[] sourceIp, byte[] targetIp) {
        byte[] udpData = new byte[appData.length + 8];
        byte[] udpLength = new byte[2];
        byte[] udpCheck = new byte[2];
        byte[] visualHead = new byte[12];
        // 数据包长度
        udpLength[0] = appData.length + 8 > 255 ? (byte) (appData.length + 8 >> 8) : 0;
        udpLength[1] = (byte) (appData.length + 8);
        // 伪头部
        for (int i = 0; i < 3; i++) {
            visualHead[i] = sourceIp[i];
            visualHead[4 + i] = targetIp[i];
        }
        visualHead[8] = 0x00;
        visualHead[9] = 0x11; // 协议类型17
        visualHead[10] = udpLength[0];
        visualHead[11] = udpLength[1];
        // 校验和初始化
        udpCheck[0] = udpCheck[1] = 0x00;
        // udp数据包
        // 源端口
        udpData[0] = sourcePort[0];
        udpData[1] = sourcePort[1];
        // 目的端口
        udpData[2] = targetPort[0];
        udpData[3] = targetPort[1];
        // udp长度
        udpData[4] = udpLength[0];
        udpData[5] = udpLength[1];
        // 校验和
        udpData[6] = udpCheck[0];
        udpData[7] = udpCheck[1];
        // 数据封装
        for (int i = 0; i < appData.length; i++) {
            udpData[i + 8] = appData[i];
        }
        // 求伪首部校验和
        for (int i = 0; i < 12; i++) {
            udpCheck[0] += visualHead[i];
            i++;
            udpCheck[1] += visualHead[i];
        }
        // 求udp包的校验和
        if (appData.length % 2 == 0) {
            for (int i = 0; i < udpData.length; i++) {
                udpCheck[0] += udpData[i];
                i++;
                udpCheck[1] += udpData[i];
            }
        } else {
            for (int i = 0; i < udpData.length - 1; i++) {
                udpCheck[0] += udpData[i];
                i++;
                udpCheck[1] += udpData[i];
            }
            udpCheck[0] += udpData[udpData.length - 1];
        }
        // 将校验和添加到udp数据包
        udpData[6] = (byte) ~udpCheck[0];
        udpData[7] = (byte) ~udpCheck[1];

        return udpData;
    }

    /**
     * 网络层，IP数据包封装
     * 
     * @param sourceIp
     *            源IP
     * @param targetIp
     *            目的IP
     * @param transData
     *            传输层数据包
     * @param mode
     *            上层协议号
     * @return IP数据包
     */
    public byte[] packIp(byte[] sourceIp, byte[] targetIp, byte[] transData, byte upperID, int fecId, boolean ifFec, boolean ifEnd) {
        byte[] ipData = new byte[transData.length + 20];
        byte[] ipHead = new byte[20];
        byte[] check = new byte[2];
        // 4位版本号+4位首部长度
        ipHead[0] = 0x45;
        // 服务类型
        ipHead[1] = upperID;
        // 首位及数据的总长度
        ipHead[2] = ipData.length > 255 ? (byte) (ipData.length >> 8) : 0;
        ipHead[3] = (byte) (ipData.length);
        // IP协议标识
        ipHead[4] = (byte) (idInIp >> 8);
        ipHead[5] = (byte) idInIp;
        // 设置标志位和位偏移
        int flag = (int) ((ifFec&&ifEnd) ? 0x03 : (ifFec ? 0x10 : (ifEnd?0x01:0x00))); 
        flag += (flag << 13) + transData.length * fecId;
        // 3位标志位+13位偏移
        ipHead[6] = (byte) (flag >> 8);
        ipHead[7] = (byte) flag;
        // 生存时间
        ipHead[8] = 0x40;
        // 协议码
        ipHead[9] = 0x11;
        // 16位首部校验和
        ipHead[10] = 0x00;
        ipHead[11] = 0x00;
        // ip标识自增1
        if (!ifFec || !ifEnd)
            idInIp++;
        // 源IP与目的IP
        for (int i = 0; i < 4; i++) {
            ipHead[12 + i] = sourceIp[i];
            ipHead[16 + i] = targetIp[i];
        }
        // 计算校验和
        for (int i = 0; i < 20; i++) {
            check[0] += ~ipHead[i];
            i++;
            check[1] += ~ipHead[i];
        }
        // 将校验和填入ip数据包
        ipHead[10] = (byte) ~check[0];
        ipHead[11] = (byte) ~check[1];
        // 连接首部与数据段
        for (int i = 0; i < 20; i++) {
            ipData[i] = ipHead[i];
        }
        for (int i = 0; i < transData.length; i++) {
            ipData[20 + i] = transData[i];
        }
        return ipData;
    }

    /**
     * mac帧封装
     * 
     * @param netData
     *            网络层数据包
     * @return mac帧数据包
     */
    public byte[] packMac(byte[] netData) {
        byte[] macData = new byte[netData.length < 46 ? 72 : netData.length + 26];
        // mac帧前导信息
        for (int i = 0; i < 8; i++) {
            macData[i] = (byte) 0xAA;
        }
        // mac帧6位目的地址和原地址
        for (int i = 0; i < 6; i++) {
            macData[i + 8] = 0x00;
            macData[i + 14] = 0x00;
        }
        // 16位上层协议类型
        macData[20] = 0x00;
        macData[21] = 0x04;
        // 将上层数据封装
        for (int i = 0; i < netData.length; i++) {
            macData[22 + i] = netData[i];
        }
        // 填充不满46字节的位
        for (int i = 22 + netData.length; i < macData.length; i++) {
            macData[i] = 0x00;
        }
        // 循环冗余校验
        int crc = 0;
        for (int i = 0; i < macData.length - 7; i++) {
            int res = macData[i];
            res = res << 8 + macData[i + 1];
            res = res << 8 + macData[i + 2];
            res = res << 8 + macData[i + 3];
            crc = crc ^ res;
        }
        // 将校验码填入macData
        macData[macData.length - 1] = (byte) crc;
        macData[macData.length - 2] = (byte) (crc >> 8);
        macData[macData.length - 3] = (byte) (crc >> 16);
        macData[macData.length - 4] = (byte) (crc >> 24);
        return macData;

    }

    /**
     * 转换成0 1字符串显示到界面
     * 
     * @param macData
     *            数据链路层数据包
     * @return 0 1 bit字符串，用于展示在界面
     */
    public String getPhy(byte[] macData) {
        String phyData = "";
        for (int i = 0; i < macData.length; i++) {
            for (int j = 0; j < 8; j++) {
                phyData += ((macData[i] << j) & 0x80) == 0x00 ? 0 : 1;
            }
        }
        return phyData;
    }

}
