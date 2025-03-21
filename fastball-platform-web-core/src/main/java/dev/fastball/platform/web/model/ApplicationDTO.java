package dev.fastball.platform.web.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApplicationDTO {
    private String code;
    private String title;
    private String icon;
    private String description;
    private String businessContext;
    private List<MenuDTO> menus;
}
