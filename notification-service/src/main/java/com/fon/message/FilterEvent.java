package com.fon.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fon.entity.Filter;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterEvent extends BaseEvent {
    private Long id;
    private Filter filter;
}
