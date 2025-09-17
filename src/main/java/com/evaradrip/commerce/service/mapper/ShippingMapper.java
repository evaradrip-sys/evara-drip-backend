package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Order;
import com.evaradrip.commerce.domain.Shipping;
import com.evaradrip.commerce.service.dto.OrderDTO;
import com.evaradrip.commerce.service.dto.ShippingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Shipping} and its DTO {@link ShippingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShippingMapper extends EntityMapper<ShippingDTO, Shipping> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    ShippingDTO toDto(Shipping s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
