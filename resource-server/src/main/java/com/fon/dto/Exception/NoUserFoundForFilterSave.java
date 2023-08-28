package com.fon.dto.Exception;


import com.fon.entity.Filter;
import lombok.Data;

@Data
public class NoUserFoundForFilterSave extends RuntimeException {

    private Filter filter;

    public NoUserFoundForFilterSave(String message, Throwable cause, Filter filter) {
        super(message, cause);
        this.filter = filter;
    }

}
