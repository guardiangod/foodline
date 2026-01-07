package com.ryan.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Restaurant extends Outlet{

    @Builder
    public Restaurant(String name, String description, String outletId) {
        super(name, description, outletId);
    }
}
