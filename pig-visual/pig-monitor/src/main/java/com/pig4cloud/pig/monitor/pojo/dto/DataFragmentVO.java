package com.pig4cloud.pig.monitor.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;




@Data
@Builder
@Schema(description = "数据碎片返回值实体类")
public class DataFragmentVO {

    @Schema(description = "表模式", example = "public")
    private String schemaName;

    @Schema(description = "表名称")
    private String tableName;

    @Schema(description = "碎片字节数")
    private String fragmentBytes;

    @Schema(description = "碎片百分比")
    private String fragmentPercent;

}
