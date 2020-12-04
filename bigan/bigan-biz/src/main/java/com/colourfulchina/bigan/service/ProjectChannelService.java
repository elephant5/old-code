package com.colourfulchina.bigan.service;

import com.colourfulchina.bigan.api.entity.ProjectChannel;

import java.util.List;
import java.util.Map;

public interface ProjectChannelService {

	List<ProjectChannel> selectByProjectId(ProjectChannel projectChannel);

    int selectByProjectIdChannelId(Map<String, Integer> map);
}
