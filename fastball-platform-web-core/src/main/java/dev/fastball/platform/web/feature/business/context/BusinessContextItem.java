package dev.fastball.platform.web.feature.business.context;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Geng Rong
 */
@JsonSerialize(using = BusinessContextSerializer.class)
public interface BusinessContextItem {
    String id();

    String title();
}
