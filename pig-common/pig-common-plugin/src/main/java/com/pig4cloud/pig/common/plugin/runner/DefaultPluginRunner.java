package com.pig4cloud.pig.common.plugin.runner;

import com.pig4cloud.pig.common.core.entity.plugin.PluginContext;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Default no-op PluginRunner implementation.
 * Used when no concrete plugin service is available.
 */
@Slf4j
public class DefaultPluginRunner implements PluginRunner {

	@Override
	public <T> void pluginExecute(Class<T> clazz, Consumer<T> execute) {
		log.debug("No plugin service available, skip execute for: {}", clazz.getSimpleName());
	}

	@Override
	public <T> void pluginExecute(Class<T> clazz, BiConsumer<T, PluginContext> execute) {
		log.debug("No plugin service available, skip execute for: {}", clazz.getSimpleName());
	}
}
