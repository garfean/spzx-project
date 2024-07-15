package com.atguigu.exception;

import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import lombok.Data;

/*
 *@Author:Hui
 *
 *@Date:2024/7/1510:22
 *
 *Description: 自定义异常
 */
@Data
public class HuiGeException extends RuntimeException{
    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public HuiGeException(ResultCodeEnum resultCodeEnum){
        this.resultCodeEnum=resultCodeEnum;
        this.code=resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
}
