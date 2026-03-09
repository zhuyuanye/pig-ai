package com.pig4cloud.pig.monitor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import com.pig4cloud.pig.common.core.entity.fragment.DataFragmentConfig;
import com.pig4cloud.pig.monitor.fragment.DataFragmentConfigService;
import com.pig4cloud.pig.monitor.pojo.dto.DataFragmentVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * 数据碎片配置接口
 * 用于管理数据库数据碎片检测的配置信息
 */
@RestController
@RequestMapping("/api/dataFragment")
@Tag(name = "数据碎片配置接口", description = "用于管理 数据库数据碎片配置信息")
public class DataFragmentConfigController {

    @Resource
    private DataFragmentConfigService dataFragmentConfigService;

    @Operation(summary = "创建数据碎片配置", description = "添加一个新的数据碎片配置信息")
    @PostMapping("/create")
    public DataFragmentConfig create(
            @RequestBody DataFragmentConfig config) {
        return dataFragmentConfigService.save(config);
    }

    @Operation(summary = "删除数据碎片配置", description = "根据主键 ID 删除数据碎片配置")
    @DeleteMapping("/delete/{id}")
    public void delete(
            @Parameter(description = "配置 ID", example = "1")
            @PathVariable Long id) {
        dataFragmentConfigService.deleteById(id);
    }

    @Operation(summary = "获取所有数据碎片配置", description = "返回所有数据碎片配置信息列表")
    @GetMapping("/all")
    public List<DataFragmentConfig> getAll() {
        return dataFragmentConfigService.findAll();
    }

    @Operation(summary = "根据 ID 获取数据碎片配置", description = "根据主键 ID 查询数据碎片配置信息")
    @GetMapping("/{id}")
    public ResponseEntity<DataFragmentConfig> getById(
            @Parameter(description = "配置 ID", example = "1")
            @PathVariable Long id) {
        return dataFragmentConfigService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "更新数据碎片配置", description = "更新数据碎片配置信息")
    @PutMapping("/update")
    public DataFragmentConfig update(
            @RequestBody DataFragmentConfig config) {
        return dataFragmentConfigService.save(config);
    }

    @Operation(summary = "根据配置主键查询数据碎片", description = "根据主键 ID 查询数据碎片信息")
    @GetMapping("/getDataFragmentInfo/{id}")
    public ResponseEntity<List<DataFragmentVO>> getDataFragmentInfo(
            @Parameter(description = "配置 ID", example = "1")
            @PathVariable Long id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(dataFragmentConfigService.getDataFragmentInfo(id));
    }


}
