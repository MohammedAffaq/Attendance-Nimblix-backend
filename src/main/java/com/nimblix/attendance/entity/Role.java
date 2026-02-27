//Updated
package com.nimblix.attendance.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN,
    EMPLOYEE;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
//package com.nimblix.attendance.entity;
//
//public enum Role {
//
//    ADMIN,
//    EMPLOYEE;
//
//    public String getAuthority() {
//        return "ROLE_" + this.name();
//    }
//}
