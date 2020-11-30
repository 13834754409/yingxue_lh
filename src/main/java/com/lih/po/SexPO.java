package com.lih.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/26 20:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SexPO implements Serializable {
    private String sex;
    private List<CityPO> cityPOS;
}
