package com.colourfulchina.bigan.service.impl;

import com.colourfulchina.bigan.api.entity.ProjectChannel;
import com.colourfulchina.bigan.mapper.ProjectChannelMapper;
import com.colourfulchina.bigan.service.ProjectChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class ProjectChannelServiceImpl implements ProjectChannelService {
	@Autowired
	private ProjectChannelMapper projectChannelMapper;
	@Override
	public List<ProjectChannel> selectByProjectId(ProjectChannel projectChannel) {
		return projectChannelMapper.selectByProjectId(projectChannel);
	}

	@Override
	public int selectByProjectIdChannelId(Map<String, Integer> map) {
		return projectChannelMapper.selectByProjectIdChannelId(map);
	}
}
