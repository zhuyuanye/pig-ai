package com.pig4cloud.pig.common.plugin.config;

import com.pig4cloud.pig.common.plugin.runner.DefaultPluginRunner;
import com.pig4cloud.pig.common.plugin.runner.PluginRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Plugin auto configuration.
 * Registers a no-op DefaultPluginRunner when no concrete implementation exists.
 */
@AutoConfiguration
public class PluginAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(PluginRunner.class)
	public PluginRunner pluginRunner() {
		return new DefaultPluginRunner();
	}
}
