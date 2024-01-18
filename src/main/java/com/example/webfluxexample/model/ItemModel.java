package com.example.webfluxexample.model;
import com.example.webfluxexample.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemModel {

    private String id;

    private String name;

    private Integer count;

    private List<SubItemModel> subItems;

    public static ItemModel from(Item item) {

        ItemModel itemModel = new ItemModel();
        itemModel.setId(item.getId());
        itemModel.setName(item.getName());
        itemModel.setCount(item.getCount());

        if (item.getSubItems() != null) {
            itemModel.setSubItems(item.getSubItems()
                    .stream()
                    .map(SubItemModel::from)
                    .collect(Collectors.toList()));
        }

        return itemModel;
    }
}
