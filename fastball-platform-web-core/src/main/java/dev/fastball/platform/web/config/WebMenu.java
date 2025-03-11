package dev.fastball.platform.web.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author gr@fastball.dev
 * @since 2022/12/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebMenu {
    private String title;
    private String icon;
    private String component;
    private String description;
    private Object params;
    private Map<String, WebMenu> menus;
}
