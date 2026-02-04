package com.pig4cloud.pig.common.core.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ValuePlainDTO
 * @Author Administrator
 * @Date 2025/06/17 8:54
 * @Description 指标值
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Monitoring Metrics Data")
public class ValuePlainDTO {

    public ValuePlainDTO(String origin) {
        this.origin = origin;
    }

    public ValuePlainDTO(String origin, long time) {
        this.origin = origin;
        this.time = time;
    }

    @Schema(title = "Monitoring Metrics Field")
    private String field;

    @Schema(title = "Monitoring Metrics Field name")
    private String fieldName;

    @Schema(title = "Origin Value")
    private String origin;

    @Schema(title = "Mean Avg Value")
    private String mean;

    @Schema(title = "Median Value, Not Support Now")
    private String median;

    @Schema(title = "Min Value")
    private String min;

    @Schema(title = "Max Value")
    private String max;

    @Schema(title = "Collect Data Time(effect when history range data)")
    private Long time;

    @Schema(title = "Collect Data Time(effect when history range data)")
    private String timeFormat;
}
