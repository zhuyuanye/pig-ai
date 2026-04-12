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

package com.pig4cloud.pig.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.entity.manager.Label;
import com.pig4cloud.pig.monitor.mapper.LabelMapper;
import com.pig4cloud.pig.monitor.service.LabelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Label service implementation.
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelMapper labelMapper;

    @Override
    public void addLabel(Label label) {
        // Verify request data
        Label existing = labelMapper.selectOne(new QueryWrapper<Label>()
                .eq("name", label.getName())
                .eq("tag_value", label.getTagValue()));
        if (existing != null) {
            throw new IllegalArgumentException("The label already exists.");
        }
        label.setType((byte) 1);
        label.setId(null);
        labelMapper.insert(label);
    }

    @Override
    public void modifyLabel(Label label) {
        Label existing = labelMapper.selectById(label.getId());
        if (existing != null) {
            Label existOptional = labelMapper.selectOne(new QueryWrapper<Label>()
                    .eq("name", label.getName())
                    .eq("tag_value", label.getTagValue()));
            if (existOptional != null && !existOptional.getId().equals(label.getId())) {
                throw new IllegalArgumentException("The label with same key and value already exists.");
            }
            label.setTagValue(StringUtils.isEmpty(label.getTagValue()) ? null : label.getTagValue());
            labelMapper.updateById(label);
        } else {
            throw new IllegalArgumentException("The label is not existed");
        }
    }

    @Override
    public IPage<Label> getLabels(String search, Byte type, int pageIndex, int pageSize) {
        LambdaQueryWrapper<Label> queryWrapper = new LambdaQueryWrapper<>();

        // AND conditions
        if (type != null) {
            queryWrapper.eq(Label::getType, type);
        }

        // OR conditions for search
        if (StringUtils.isNotBlank(search)) {
            queryWrapper.and(wrapper -> wrapper
                    .likeRight(Label::getName, search.toLowerCase())
                    .or()
                    .likeRight(Label::getTagValue, search.toLowerCase()));
        }

        Page<Label> page = new Page<>(pageIndex, pageSize);
        return labelMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void deleteLabels(HashSet<Long> ids) {
        if (CollectionUtils.isEmpty(ids)){
            return;
        }
        labelMapper.deleteBatchIds(ids);
    }

    public List<Label> determineNewLabels(Set<Map.Entry<String, String>> originLabels){

        if (originLabels == null || originLabels.isEmpty()) return List.of();

        // Get all labels from the database
        Set<Map.Entry<String, String>> allLabels = labelMapper.selectList(null).stream()
                .map(label -> Map.entry(label.getName(), label.getTagValue()))
                .collect(Collectors.toSet());

        // If the bound label (key:value) does not exist, then add it
        Set<Map.Entry<String, String>> addLabelsKv = originLabels.stream()
                .filter(label -> !allLabels.contains(label))
                .collect(Collectors.toCollection(HashSet::new));

        return addLabelsKv.stream().map(kv -> {
            Label label = new Label();
            label.setId(null);
            label.setName(kv.getKey());
            label.setTagValue(kv.getValue());
            label.setType((byte) 0);
            return label;
        }).toList();
    }
}
