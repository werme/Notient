package controllers;

import securesocial.core.Identity;
import securesocial.core.java.Authorization;
import models.*;

public class WithPrivilegeLevel implements Authorization {

    public boolean isAuthorized(Identity user, String params[]) {
        LocalUser localUser = LocalUser.findById(user.identityId().userId());
        return localUser.privilege.equals(params[0]);
    }
}