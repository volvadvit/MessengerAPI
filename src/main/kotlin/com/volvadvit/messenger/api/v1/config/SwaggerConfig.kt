package com.volvadvit.messenger.api.v1.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig : WebMvcConfigurer {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.volvadvit.messenger.api.v1.controllers"))
            .paths(PathSelectors.any()) // regex for url
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Messanger RESTful API. v1")
            .contact(
                Contact("Vadim V.",
                    "https://www.github.com/volvadvit",
                    "volvadvit@gmail.com"))
            .description("Для всех запросов необходимо получить токен")
            .version("1.0")
            .build()
    }

//    override fun addViewControllers(registry: ViewControllerRegistry) {
//        with(registry) {
//            addRedirectViewController("/api/v1/v2/api-docs", "/v2/api-docs").setKeepQueryParams(true)
//            addRedirectViewController(
//                "/api/v1/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui"
//            )
//            addRedirectViewController(
//                "/api/v1/swagger-resources/configuration/security", "/swagger-resources/configuration/security"
//            )
//            addRedirectViewController("/api/v1/swagger-resources", "/swagger-resources")
//            addRedirectViewController("/api/v1/webjars/", "/webjars/")
//            addRedirectViewController("/api/v1/swagger-ui", "/swagger-ui")
//            addRedirectViewController("/api/v1/swagger-ui/index.html", "/swagger-ui/index.html")
//        }
//    }
//
//    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
//        registry.addResourceHandler("/v1/**").addResourceLocations("classpath:/META-INF/resources/")
//    }
}