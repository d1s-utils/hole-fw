package dev.d1s.holefw.properties

import dev.d1s.holefw.constant.HOLE_SERVER_PROPERTIES
import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

@Validated
@ConstructorBinding
@ConfigurationProperties(HOLE_SERVER_PROPERTIES)
data class HoleServerConfigurationProperties(

    @URL
    val baseUrl: String,

    val authorization: String?
)