package com.colourfulchina.mars.api.entity.sync;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2019-12-11
 */
@Data
public class BookOrderTags implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String tags;
    /**
    * 每页显示条数
    */
    private Long pageSize;
    /**
    * 当前页码
    */
    private Long pageIndex;


}
