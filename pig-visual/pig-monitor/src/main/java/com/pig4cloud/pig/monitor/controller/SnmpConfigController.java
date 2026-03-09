package com.pig4cloud.pig.monitor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import com.pig4cloud.pig.common.core.entity.network.NetworkTopologyInfo;
import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import com.pig4cloud.pig.monitor.network.NetworkTopologyService;
import com.pig4cloud.pig.monitor.network.SnmpConfigService;
import com.pig4cloud.pig.monitor.pojo.dto.NetworkTopologyVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/snmpConfig")
@Tag(name = "SNMP 配置接口", description = "用于管理 网络拓扑 SNMP 网络设备配置信息")
public class SnmpConfigController {

    @Resource
    private SnmpConfigService snmpConfigService;

    @Resource
    private NetworkTopologyService topologyService;

    @Operation(summary = "创建 SNMP 配置", description = "添加一个新的 SNMP 网络设备配置信息")
    @PostMapping("/create")
    public SnmpConfigInfo create(
            @RequestBody SnmpConfigInfo configInfo) {
        if (configInfo.getDeviceType().equals("3")) {
            configInfo.setCommunity("public");
            configInfo.setVersion(0);
        }
        return snmpConfigService.create(configInfo);
    }

    @Operation(summary = "删除 SNMP 配置", description = "根据主键 ID 删除 SNMP 配置")
    @DeleteMapping("/delete/{id}")
    public void delete(
            @Parameter(description = "配置 ID", example = "1")
            @PathVariable Long id) {
        snmpConfigService.deleteById(id);
    }

    @Operation(summary = "获取所有 SNMP 配置", description = "返回所有 SNMP 配置信息列表")
    @GetMapping("/all")
    public List<SnmpConfigInfo> getAll() {
        return snmpConfigService.getAll();
    }

    @Operation(summary = "根据 ID 获取 SNMP 配置", description = "根据主键 ID 查询设备配置信息")
    @GetMapping("/{id}")
    public SnmpConfigInfo getById(
            @Parameter(description = "配置 ID", example = "1")
            @PathVariable Long id) {
        return snmpConfigService.getById(id);
    }

    @Operation(summary = "更新 SNMP 配置", description = "更新 SNMP 网络设备配置信息")
    @PutMapping("/update")
    public SnmpConfigInfo update(
            @RequestBody SnmpConfigInfo configInfo) {
        return snmpConfigService.update(configInfo);
    }

    @Operation(summary = "获取运行中的 SNMP 配置", description = "查询标记为运行状态的设备配置")
    @GetMapping("/running")
    public List<SnmpConfigInfo> getRunningConfigs() {
        return snmpConfigService.getRunningConfigs();
    }


    @Operation(summary = "获取所有网络拓扑数据", description = "获取所有网络拓扑数据")
    @GetMapping("/selectNetworkAllInfo")
    public ResponseEntity<List<NetworkTopologyVO>> selectNetworkAllInfo() {
        List<NetworkTopologyVO> restList = new ArrayList<>();
        List<SnmpConfigInfo> runningConfigs = snmpConfigService.getRunningConfigs();
        for (SnmpConfigInfo runningConfig : runningConfigs) {
            List<NetworkTopologyInfo> networkTopologyInfo = topologyService.findByConfigId(String.valueOf(runningConfig.getId()));
            NetworkTopologyVO networkTopologyVO = NetworkTopologyVO.builder()
                    .ip(runningConfig.getIp())
                    .deviceType(runningConfig.getDeviceType())
                    .networkTopologyInfoList(networkTopologyInfo)
                    .build();
            restList.add(networkTopologyVO);
        }
        return ResponseEntity.ok(restList);
    }


}
