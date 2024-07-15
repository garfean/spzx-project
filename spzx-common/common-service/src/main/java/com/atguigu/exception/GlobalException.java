package com.atguigu.exception;

import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 *@Author:Hui
 *
 *@Date:2024/7/1510:05
 *
 *Description:
 */
@ControllerAdvice
public class GlobalException {
   // 全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(){
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    //自定义异常
    @ExceptionHandler(HuiGeException.class)
    @ResponseBody
    public Result error1(HuiGeException huiGeException){
        return Result.build(null, huiGeException.getResultCodeEnum());
    }

}
