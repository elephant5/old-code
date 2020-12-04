package com.colourfulchina.mars.api.entity.sync;

import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("chali_book_order_tags")
public class BookOrderTagsPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String tags;


}
