package org.example.quizengine.auth;

import org.example.quizengine.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = MapStructConfig.class)
public interface AuthMapper {
    @Mapping(target = "UserEntity.password", ignore = true)
    UserEntity toEntity(AuthDto.Register registerDto);

    default UserEntity toEntity(AuthDto.Register registerDto, PasswordEncoder encoder) {
        var entity = toEntity(registerDto);
        entity.setPassword(encoder.encode(registerDto.password()));
        return entity;
    }

    AuthDto.Response toResponse(UserEntity entity);
}
