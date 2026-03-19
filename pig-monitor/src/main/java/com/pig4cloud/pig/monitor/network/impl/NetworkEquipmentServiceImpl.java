package com.pig4cloud.pig.monitor.network.impl;

import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.monitor.network.NetworkEquipmentService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
public class NetworkEquipmentServiceImpl implements NetworkEquipmentService {

    @Override
    public Map<String, String> scanSubnet(String subnet, int start, int end) throws Exception {
        Map<String, String> ipMacMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(50);

        List<Future<?>> futures = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            String ip = subnet + "." + i;
            futures.add(executor.submit(() -> {
                try {
                    InetAddress inet = InetAddress.getByName(ip);
                    if (inet.isReachable(3000)) { // ping超时3秒
                        String mac = getMacAddress(ip);
                        ipMacMap.put(ip, mac == null ? "未知MAC" : mac);
                    }
                } catch (Exception e) {
                 //GG 啥也没有 - -!!!!
                    log.error("Error while scanning subnet: {}: ", ip, e);
                }
            }));
        }
        for (Future<?> f : futures) {
            f.get();
        }
        executor.shutdown();
        return ipMacMap;
    }


    /**
     * 根据IP调用系统arp命令解析对应MAC地址
     */
    private  String getMacAddress(String ip) {
        String os = System.getProperty("os.name").toLowerCase();
        String mac = null;
        try {
            Process p;
            if (os.contains("win")) {
                // Windows执行arp -a 命令，匹配IP
                p = Runtime.getRuntime().exec("arp -a " + ip);
            } else {
                // Linux/Mac执行arp -n IP 命令
                p = Runtime.getRuntime().exec("arp -n " + ip);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(ip)) {
                    mac = parseMacFromArpLine(line);
                    break;
                }
            }
            reader.close();
        } catch (Exception e) {
            // 异常忽略
        }
        return mac;
    }

    /**
     * 解析ARP命令行中的MAC地址字符串（兼容Windows和Linux格式）
     */
    private  String parseMacFromArpLine(String line) {
        // Windows MAC格式：xx-xx-xx-xx-xx-xx
        // Linux/Mac MAC格式：xx:xx:xx:xx:xx:xx
        String regex = "([0-9a-fA-F]{2}([-:])){5}[0-9a-fA-F]{2}";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group().toUpperCase().replace('-', ':');
        }
        return null;
    }










}
