package com.example.wms_supermarket.controllerAdvice;

import com.example.wms_supermarket.entity.Message;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
//处理各种常见异常，避免Exception暴露到用户面前
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        Message msg=new Message();
        msg.setCode(500);
        msg.setMsg("数据库无法连接。错误描述：" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg.toJson());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        Message msg=new Message();
        msg.setCode(500);
        msg.setMsg("参数异常发生了，您传入的数据参数不合法。错误描述：" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg.toJson());
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        Message msg=new Message();
        msg.setCode(500);
        msg.setMsg("服务器内出错，请联系管理员。错误描述：" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg.toJson());
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        Message msg=new Message();
        msg.setCode(500);
        msg.setMsg("传递的参数类型或数值有误，请重新输入。错误描述：" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg.toJson());
    }
}
