package pro;

public class UnpackProtocol {

    /**
     * 提取时间戳
     * 
     * @param appData
     *            应用程序封装数据包
     * @return 时间戳
     */
    public long getTimeStamp(byte[] appData) {
        long timeStamp = 0;
        for (int i = 0; i < 8; i++) {
            timeStamp = timeStamp << 8;
            timeStamp += appData[i];
        }
        return timeStamp;
    }

    /**
     * 提取原始数据
     * 
     * @param appData
     *            应用程序封装数据包
     * @return 原始数据
     */
    public byte[] getSourceData(byte[] appData) {
        byte[] sourceData = new byte[appData.length - 8];
        for (int i = 8; i < appData.length; i++) {
            sourceData[i - 8] = appData[i];
        }
        return sourceData;
    }

    /**
     * 对tcp数据包进行解封
     * 
     * @param tcpData
     *            tcp数据包
     * @return 返回应用程序数据包
     */
    public byte[] unPackTcp(byte[] tcpData) {
        byte[] appData = new byte[tcpData.length - 20];
        for (int i = 0; i < appData.length; i++) {
            appData[i] = tcpData[i + 20];
        }
        return appData;
    }

    /**
     * 提取应用程序数据包
     * 
     * @param udpData
     *            udp协议数据包
     * @return 返回应用程序数据包
     */
    public byte[] unPackUdp(byte[] udpData) {
        byte[] appData = new byte[udpData.length - 8];
        for (int i = 0; i < appData.length; i++) {
            appData[i] = udpData[i + 8];
        }
        return appData;
    }

    /**
     * 提取根据ip协议包封装起来的数据包
     * 
     * @param ipData
     *            ip协议数据包
     * @return 根据ip协议封装的包内封装的数据包
     */
    public byte[] unPackIp(byte[] ipData) {
        byte[] packData = new byte[ipData.length - 20];
        for (int i = 0; i < packData.length; i++) {
            packData[i] = ipData[i + 20];
        }
        return packData;
    }
    
    /**
     * 获取IP数据包中封装数据的上层协议ID
     * @param ipData
     * @return
     */
    public byte getProIdInIpData(byte[] ipData) {
        return ipData[1];
    }

    /**
     * 提取mac帧内的数据包
     * @param macData mac帧
     * @return 网络层数据包
     */
    public byte[] unPackMac(byte[] macData) {
        // 包含可能填充0x00的填充段的网络层数据包
        byte[] netVisualData = new byte[macData.length - 26];
        for (int i = 0; i < netVisualData.length; i++) {
            netVisualData[i] = macData[i + 22];
        }
        // 记录0x00填充位的起始，即实际数据的长度
        int flag = 0;
        for (int i = netVisualData.length-1; i >= 0; i--) {
            if (netVisualData[i] != 0x00) {
                flag = i+1;
                break;
            }
        }
        // 截取实际位长度
        byte[] netData = new byte[flag];
        for (int i = 0; i < flag; i++) {
            netData[i] = netVisualData[i];
        }
        return netData;
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
