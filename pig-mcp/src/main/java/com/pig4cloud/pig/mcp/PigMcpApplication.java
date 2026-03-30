package com.pig4cloud.pig.mcp;

import com.pig4cloud.pig.mcp.service.LogService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * MCP Server Application
 */
@SpringBootApplication
public class PigMcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigMcpApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider tools(LogService logService) {
		return MethodToolCallbackProvider.builder()
				.toolObjects(logService)
				.build();
	}

}
