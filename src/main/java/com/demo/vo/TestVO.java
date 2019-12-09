package com.demo.vo;

import com.demo.annotation.ToJson;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA
 *
 * @author: yangzhizhuang
 * @date: 2019/12/6
 * @description:
 */
@Data
@ToString
public class TestVO implements Serializable {

    private static final long serialVersionUID = -5770271740858042352L;

    @ToJson
    @JsonRawValue
     public String jsonStr;

}
