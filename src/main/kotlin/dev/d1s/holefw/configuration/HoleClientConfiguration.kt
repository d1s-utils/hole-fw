package dev.d1s.holefw.configuration

import dev.d1s.hole.client.factory.holeClient
import dev.d1s.holefw.properties.HoleServerConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HoleClientConfiguration {

    @set:Autowired
    lateinit var properties: HoleServerConfigurationProperties

    @Bean
    fun holeClient() = holeClient(
        properties.baseUrl,
        properties.authorization
    )
}