package com.github.mtdp.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @Description 获取系统参数工具类
 * @author wangguoqing
 * @date 2016年8月1日上午9:10:24
 *
 */
public class SystemUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SystemUtil.class);
	
    // 获得系统属性集
	public static Properties props = System.getProperties();

	// tomcat 容器的标识，在启动虚拟机时，通过 -D加入
    public static String TOMCAT_ID = getPropertery("tomcat.id");

	/**
	 * 根据系统的类型获取本服务器的ip地址
	 * <p/>
	 * InetAddress inet = InetAddress.getLocalHost(); 但是上述代码在Linux下返回127.0.0.1。
	 * 主要是在linux下返回的是/etc/hosts中配置的localhost的ip地址，
	 * 而不是网卡的绑定地址。后来改用网卡的绑定地址，可以取到本机的ip地址：）：
	 * 
	 * @throws java.net.UnknownHostException
	 */
    public static InetAddress getSystemLocalIp(){
        InetAddress inet = null;
        String osname = getSystemOSName();
        try {
			//window系统
            if (osname.toLowerCase().indexOf("Windows".toLowerCase()) != -1) {
                inet = getWinLocalIp();
			//linux系统
            } else if (osname.toLowerCase().indexOf("Linux".toLowerCase()) != -1) {
//                inet = getUnixLocalIp();
                inet = getLinuxIP();
            }
            if (null == inet) {
				throw new RuntimeException("主机的ip地址未知");
            }
        } catch (Exception e) {
			logger.info("获取本机ip错误", e);
			throw new RuntimeException("获取本机ip错误" + e);
        } 
        return inet;
    }

	/**
	 * 获取属性的值
	 * 
	 * @param propertyName
	 * @return
	 */
    public static String getPropertery(String propertyName) {
        return props.getProperty(propertyName);
    }

	/**
	 * 获取window 本地ip地址
	 * @return
	 * @throws UnknownHostException
	 */
    private static InetAddress getWinLocalIp() throws UnknownHostException {
        InetAddress inet = InetAddress.getLocalHost();
        return inet;
    }

	/**
	 * 可能多多个ip地址只获取一个ip地址 获取Linux 本地IP地址
	 * @return
	 * @throws SocketException
	 */
    private static InetAddress getUnixLocalIp() throws SocketException {
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
            ip = (InetAddress) ni.getInetAddresses().nextElement();
            if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                return ip;
            }
        }
        return ip;
    }
    
    /**
     * 根据网卡取本机配置的IP
     * @return
     * @throws SocketException
     */
    public static InetAddress getLinuxIP() throws SocketException {
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ipAddress = null;
//        String ip = "";
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
            if (!ni.getName().equals("eth0")) {
                continue;
            } else {
                Enumeration<?> e2 = ni.getInetAddresses();
                while (e2.hasMoreElements()) {
                    ipAddress = (InetAddress) e2.nextElement();
                    if (ipAddress instanceof Inet6Address){
                        continue;
                    }
//                    ip = ipAddress.getHostAddress();
                }
                break;
            }
        }
        return ipAddress;
    }

	/**
	 * 取单号明细时的唯一Id
	 * 
	 * @return
	 */
    public static Long getLockId() {
        long _threadId = Thread.currentThread().getId();
        long _tomcatId = StringUtil.isEmpty(TOMCAT_ID) ? 0 : Long.valueOf(TOMCAT_ID);
		// 由于timestamp的前几位变更不是很平凡故把前几位去掉
        long _time = Long.valueOf(String.valueOf(System.currentTimeMillis()).substring(3));
		// lockId 算法
        Long lockId = _time * 1000 + _threadId * 100 + _tomcatId;
        return lockId;
    }
	
    /**
	 * 获取当前运行程序的内存信息
	 * 
	 * @return
	 */
    public static final String getRAMInfo() {
        Runtime rt = Runtime.getRuntime();
        return "RAM: " + rt.totalMemory() + " bytes total, " + rt.freeMemory() + " bytes free.";
    }
    
	/**
	 * 获取jvm部署的系统名称
	 * @return
	 */
	public static String getSystemOSName(){
		return System.getProperty("os.name");
	}
	
	
	public static void main(String[] args) throws Exception {
		System.out.println(getSystemOSName());
		System.out.println(getSystemLocalIp().toString());
		System.out.println(getSystemLocalIp().getHostAddress());
		System.out.println(getSystemLocalIp().getHostName());
	}
}
