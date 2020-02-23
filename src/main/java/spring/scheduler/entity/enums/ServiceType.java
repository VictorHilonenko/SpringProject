package spring.scheduler.entity.enums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ServiceType {
    NULL("i18n.NULL"),
    HAIRDRESSING("i18n.HAIRDRESSING"),
    MAKEUP("i18n.MAKEUP"),
    COSMETOLOGY("i18n.COSMETOLOGY");

    private String i18n;

    ServiceType(String i18n) {
        this.i18n = i18n;
    }

    public String getI18n() {
        return i18n;
    }

    public static ServiceType lookupNotNull(String name) {
        if (name == null) {
            return ServiceType.NULL;
        }

        try {
            return ServiceType.valueOf(name);
        } catch (IllegalArgumentException e) {
            log.error("wrong service type: " + name);
            return ServiceType.NULL;
        }
    }

    @Override
    public String toString() {
        return name();
    }
}