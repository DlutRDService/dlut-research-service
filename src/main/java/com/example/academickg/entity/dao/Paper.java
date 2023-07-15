package com.example.academickg.entity.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@TableName(value = "Paper")
public class Paper implements Serializable {
    @TableId(value = "id")
    private Integer id;
    private String Title;
    private Integer Year;
    private String Journal;
    private String esi;
    private String wc;
}
