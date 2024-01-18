package com.example.webfluxexample.model;

import com.example.webfluxexample.entity.SubItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubItemModel {

    private String name;

    private BigDecimal price;

    public static SubItemModel from(SubItem item) {

        return new SubItemModel(item.getName(), item.getPrice());
    }
}
