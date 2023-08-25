package auth.configs.db;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("db")
public class DatabaseProperties {

    public static final SupportedDatabases DEFAULT_SELECTED = DatabaseProperties.MONGO;
    public static final SupportedDatabases MONGO = SupportedDatabases.MONGO;

    private SupportedDatabases selected = DEFAULT_SELECTED;

    public void setSelected(SupportedDatabases selected) {
        this.selected = selected;
    }
}
