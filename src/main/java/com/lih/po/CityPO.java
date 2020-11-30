package com.lih.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/26 20:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityPO implements Serializable {
    private String name;
    private String value;
}
