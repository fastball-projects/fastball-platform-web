package dev.fastball.platform.web.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuDTO {
    private Long id;
    private Long parentId;
    private String code;
    private String name;
    private String path;
    private String description;
    private Object params;
}
