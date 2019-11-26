package com.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA
 * USER: yangzhizhuang
 * Date: 2019/7/4
 * Description:
 * @author yzz
 */
@Data
@ToString
@NoArgsConstructor
public class TestDTO implements Serializable {

    private static final long serialVersionUID = -7929601154634428142L;

    @NotNull(message = "username不能为空")
    private String username;




}
