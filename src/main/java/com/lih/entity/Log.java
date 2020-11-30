package com.lih.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "yx_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log implements Serializable {
    @Id
    private String id;

    private String logname;
    @Column(name = "log_times")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date logTimes;
    @Column(name = "log_option")
    private String logOption;

    private String status;

}