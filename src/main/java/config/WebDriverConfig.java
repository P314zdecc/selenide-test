package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:test.properties"
})
public interface WebDriverConfig extends Config {

    @Key("browserSize")
    @DefaultValue("1920x1080")
    String getBrowserSize();

    @Key("browserName")
    String getBrowserName();

    @Key("baseUrl")
    String getBaseUrl();

    @Key("setTimeTest")
    Integer getTime();

}
