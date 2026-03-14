/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.core.config;

import com.pig4cloud.pig.common.core.constants.ConfigConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * common module properties
 */

@ConfigurationProperties(prefix =
        ConfigConstants.FunctionModuleConstants.COMMON)
public class CommonProperties {

    /**
     * secret key for password aes entry, must 16 bits
     */
    private String secret;

    /**
     * data queue impl
     */
    private DataQueueProperties queue;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public DataQueueProperties getQueue() {
        return queue;
    }

    public void setQueue(DataQueueProperties queue) {
        this.queue = queue;
    }

    /**
     * data queue properties
     */
    public static class DataQueueProperties {

        private QueueType type = QueueType.Memory;

        private KafkaProperties kafka;

        private RedisProperties redis;

        public QueueType getType() {
            return type;
        }

        public void setType(QueueType type) {
            this.type = type;
        }

        public KafkaProperties getKafka() {
            return kafka;
        }

        public void setKafka(KafkaProperties kafka) {
            this.kafka = kafka;
        }

        public RedisProperties getRedis() {
            return redis;
        }

        public void setRedis(RedisProperties redis) {
            this.redis = redis;
        }

    }

    /**
     * data queue type
     */
    public enum QueueType {
        /** in memory **/
        Memory,
        /** kafka **/
        Kafka,
        /** with netty connect **/
        Netty,
        /** rabbit mq **/
        Rabbit_Mq,
        /** redis **/
        Redis
    }

    /**
     * redis data queue properties
     */
    public static class RedisProperties {

        /**
         * redis server host.
         */
        private String redisHost;

        /**
         * redis server port.
         */
        private int redisPort;

        /**
         * Queue name for metrics data to alerter
         */
        private String metricsDataQueueNameToAlerter;

        /**
         * Queue name for metrics data to persistent storage
         */
        private String metricsDataQueueNameToPersistentStorage;

        /**
         * Queue name for metrics data to real-time storage
         */
        private String metricsDataQueueNameToRealTimeStorage;

        /**
         * Queue name for service discovery
         */
        private String metricsDataQueueNameForServiceDiscovery;

        /**
         * Queue name for alerts data
         */
        private String alertsDataQueueName;

        public String getRedisHost() {
            return redisHost;
        }

        public void setRedisHost(String redisHost) {
            this.redisHost = redisHost;
        }

        public int getRedisPort() {
            return redisPort;
        }

        public void setRedisPort(int redisPort) {
            this.redisPort = redisPort;
        }

        public String getMetricsDataQueueNameToAlerter() {
            return metricsDataQueueNameToAlerter;
        }

        public void setMetricsDataQueueNameToAlerter(String metricsDataQueueNameToAlerter) {
            this.metricsDataQueueNameToAlerter = metricsDataQueueNameToAlerter;
        }

        public String getMetricsDataQueueNameToPersistentStorage() {
            return metricsDataQueueNameToPersistentStorage;
        }

        public void setMetricsDataQueueNameToPersistentStorage(String metricsDataQueueNameToPersistentStorage) {
            this.metricsDataQueueNameToPersistentStorage = metricsDataQueueNameToPersistentStorage;
        }

        public String getMetricsDataQueueNameToRealTimeStorage() {
            return metricsDataQueueNameToRealTimeStorage;
        }

        public void setMetricsDataQueueNameToRealTimeStorage(String metricsDataQueueNameToRealTimeStorage) {
            this.metricsDataQueueNameToRealTimeStorage = metricsDataQueueNameToRealTimeStorage;
        }

        public String getMetricsDataQueueNameForServiceDiscovery() {
            return metricsDataQueueNameForServiceDiscovery;
        }

        public void setMetricsDataQueueNameForServiceDiscovery(String metricsDataQueueNameForServiceDiscovery) {
            this.metricsDataQueueNameForServiceDiscovery = metricsDataQueueNameForServiceDiscovery;
        }

        public String getAlertsDataQueueName() {
            return alertsDataQueueName;
        }

        public void setAlertsDataQueueName(String alertsDataQueueName) {
            this.alertsDataQueueName = alertsDataQueueName;
        }

    }

    /**
     * kafka data queue properties
     */
    public static class KafkaProperties extends BaseKafkaProperties {

        /**
         * metrics data topic
         */
        private String metricsDataTopic;
        /**
         * metrics data to storage topic
         */
        private String metricsDataToStorageTopic;
        /**
         * service discovery data topic
         */
        private String serviceDiscoveryDataTopic;
        /**
         * alerts data topic
         */
        private String alertsDataTopic;

        public String getMetricsDataTopic() {
            return metricsDataTopic;
        }

        public void setMetricsDataTopic(String metricsDataTopic) {
            this.metricsDataTopic = metricsDataTopic;
        }

        public String getMetricsDataToStorageTopic() {
            return metricsDataToStorageTopic;
        }

        public void setMetricsDataToStorageTopic(String metricsDataToStorageTopic) {
            this.metricsDataToStorageTopic = metricsDataToStorageTopic;
        }

        public String getServiceDiscoveryDataTopic() {
            return serviceDiscoveryDataTopic;
        }

        public void setServiceDiscoveryDataTopic(String serviceDiscoveryDataTopic) {
            this.serviceDiscoveryDataTopic = serviceDiscoveryDataTopic;
        }

        public String getAlertsDataTopic() {
            return alertsDataTopic;
        }

        public void setAlertsDataTopic(String alertsDataTopic) {
            this.alertsDataTopic = alertsDataTopic;
        }
    }
}
