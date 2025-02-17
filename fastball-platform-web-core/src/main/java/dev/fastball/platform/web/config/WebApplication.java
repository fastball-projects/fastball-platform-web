package dev.fastball.platform.web.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**
 * @author Geng Rong
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebApplication {
    private String name;
    private String icon;
    private String description;
    private String businessContext;
    private Map<String, WebMenu> menus;
}
