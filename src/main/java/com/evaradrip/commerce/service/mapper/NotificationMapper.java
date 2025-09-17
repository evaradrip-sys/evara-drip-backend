package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Notification;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.service.dto.NotificationDTO;
import com.evaradrip.commerce.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    NotificationDTO toDto(Notification s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
