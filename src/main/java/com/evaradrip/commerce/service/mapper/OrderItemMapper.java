package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Order;
import com.evaradrip.commerce.domain.OrderItem;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.ProductVariant;
import com.evaradrip.commerce.service.dto.OrderDTO;
import com.evaradrip.commerce.service.dto.OrderItemDTO;
import com.evaradrip.commerce.service.dto.ProductDTO;
import com.evaradrip.commerce.service.dto.ProductVariantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "variant", source = "variant", qualifiedByName = "productVariantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderItemDTO toDto(OrderItem s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("productVariantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductVariantDTO toDtoProductVariantId(ProductVariant productVariant);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
