package com.colourfulchina.bigan.mapper;

import com.colourfulchina.bigan.api.entity.ProjectChannel;

import java.util.List;
import java.util.Map;

public interface ProjectChannelMapper {
	List<ProjectChannel> selectByProjectId(ProjectChannel projectChannel);

    int selectByProjectIdChannelId(Map<String, Integer> map);
}
