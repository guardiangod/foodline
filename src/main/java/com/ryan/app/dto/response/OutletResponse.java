package com.ryan.app.dto.response;

import com.ryan.app.persistence.entity.OutletType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutletResponse {

    private String outletId;
    private String name;
    private OutletType outletType;
}
