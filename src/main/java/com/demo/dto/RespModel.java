package com.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA
 * USER: yangzhizhuang
 * Date: 2019/7/4
 * Description:
 */
@Data
@ToString
@NoArgsConstructor
public class RespModel<T> implements Serializable {


    private static final long serialVersionUID = -6311306002112321977L;


    private int code;

    private String msg;

    private T data;

    public RespModel(String msg){
        this.code = 500;
        this.msg = msg;
    }


}
