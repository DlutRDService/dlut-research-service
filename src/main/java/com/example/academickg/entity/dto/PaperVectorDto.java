package com.example.academickg.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Vector;
@Component
@Data
public class PaperVectorDto implements Serializable {
    public Map<String, Vector<Float>> map;
}
