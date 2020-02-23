package spring.scheduler.entity.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Role implements GrantedAuthority {
    ROLE_ANONYMOUS("all,notAuthenticated,nonstaff", ""),
    ROLE_USER("all,authenticated,nonstaff,front", "i18n.USER"),
    ROLE_MASTER("all,authenticated,staff,front", "i18n.MASTER"),
    ROLE_ADMIN("all,authenticated,staff,admin,front", "i18n.ADMIN"),
    all("", ""),
    notAuthenticated("", ""),
    authenticated("", ""),
    staff("", ""),
    nonstaff("", "");

    private String tags;
    private String i18n;

    Role(String tags, String i18n) {
        this.tags = tags;
        this.i18n = i18n;
    }

    public String getI18n() {
        return i18n;
    }

    public boolean hasTag(String tag) {
        return Arrays.asList(this.tags.split(","))
                .contains(tag);
    }

    public static List<Role> getAllByTag(String tag) {
        return Arrays.stream(values())
                .filter(role -> role.hasTag(tag))
                .collect(Collectors.toList());
    }

    public static List<Role> getAllByRoleTag(Role roleTag) {
        if ("".equals(roleTag.getTags())) {
            return getAllByTag(roleTag.name());
        } else {
            return Collections.singletonList(roleTag);
        }
    }

    public static final Role DEFAULT_ROLE = ROLE_USER;

    public String getTags() {
        return this.tags;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}