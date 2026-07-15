package com.ryan.app.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "outlets")
@Getter
@Setter
@NoArgsConstructor
public class OutletEntity {

    @Id
    @Column(name = "outlet_id", length = 50, nullable = false)
    private String outletId;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "outlet_type", length = 30, nullable = false)
    private OutletType outletType;

    public OutletEntity(String outletId, String name, OutletType outletType) {
        this.outletId = outletId;
        this.name = name;
        this.outletType = outletType;
    }
}
