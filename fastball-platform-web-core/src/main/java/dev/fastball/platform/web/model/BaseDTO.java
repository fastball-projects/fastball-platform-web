package dev.fastball.platform.web.model;

import dev.fastball.core.annotation.Field;
import dev.fastball.meta.basic.DisplayType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
public class BaseDTO {

    @Field(display = DisplayType.Hidden)
    private Long id;

    @Field(display = DisplayType.Hidden)
    private Date createdAt;

    @Field(display = DisplayType.Hidden)
    private Long createdBy;

    @Field(display = DisplayType.Hidden)
    private Date lastUpdatedAt;

    @Field(display = DisplayType.Hidden)
    private Long lastUpdatedBy;
}
