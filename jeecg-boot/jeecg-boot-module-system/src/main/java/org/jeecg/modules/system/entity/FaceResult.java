package org.jeecg.modules.system.entity;

import lombok.Data;

@Data
public class FaceResult {
    private String group_id;
    private String user_id;
    private String user_info;
    private double score;
}
