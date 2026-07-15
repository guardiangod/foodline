package com.ryan.app.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import com.ryan.app.domain.CatalogType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
public class CartEntity {

    @Id
    @Column(name = "cart_id", length = 50, nullable = false)
    private String cartId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outlet_id")
    private OutletEntity outlet;

    @Enumerated(EnumType.STRING)
    @Column(name = "catalog_type", length = 20)
    private CatalogType catalogType;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> items = new ArrayList<>();

    public CartEntity(String cartId, UserEntity user) {
        this.cartId = cartId;
        this.user = user;
    }

    public void clearItems() {
        items.clear();
    }
}
