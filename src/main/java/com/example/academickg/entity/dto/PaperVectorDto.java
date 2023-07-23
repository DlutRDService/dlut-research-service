package com.example.academickg.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Vector;

@Data
public class PaperVectorDto implements Serializable {
    public Map<String, Vector<Float>> map;
}
