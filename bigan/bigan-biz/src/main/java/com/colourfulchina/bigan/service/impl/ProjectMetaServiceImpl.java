package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ProjectMeta;
import com.colourfulchina.bigan.mapper.ProjectMetaMapper;
import com.colourfulchina.bigan.service.ProjectMetaService;
import org.springframework.stereotype.Service;

@Service
public class ProjectMetaServiceImpl extends ServiceImpl<ProjectMetaMapper,ProjectMeta> implements ProjectMetaService {
}
