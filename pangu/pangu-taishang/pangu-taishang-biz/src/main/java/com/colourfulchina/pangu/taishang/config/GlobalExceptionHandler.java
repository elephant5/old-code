package com.colourfulchina.pangu.taishang.config;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResultVo<String> methodArgNotException(Exception ex, HttpServletRequest httpServletRequest){
        log.error("异常信息 ex={}", ex.getMessage(), ex);
        CommonResultVo<String> resultVo = new CommonResultVo<String>();
        MethodArgumentNotValidException c = (MethodArgumentNotValidException) ex;
        List<ObjectError> errors = c.getBindingResult().getAllErrors();
        StringBuffer errorMsg = new StringBuffer();
        errors.stream().forEach(x -> {
            errorMsg.append(x.getDefaultMessage()).append(";");
        });
        resultVo.setCode(400);
        resultVo.setMsg(errorMsg.toString());
        return resultVo;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResultVo<String> exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("异常信息 ex={}", e.getMessage(), e);
        CommonResultVo<String> resultVo = new CommonResultVo<String>();
        resultVo.setCode(500);
        resultVo.setMsg(e.getMessage());
        return resultVo;
    }
}
