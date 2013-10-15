package controllers;

import securesocial.core.Identity;
import securesocial.core.java.Authorization;
import models.*;

public class WithPrivilegeLevel implements Authorization {

    public boolean isAuthorized(Identity user, String params[]) {
        User localUser = User.findById(user.identityId().userId());
        for(int i = 0; i < params.length; i++)
            if(localUser.privilege.equals(params[i])){
                return true;
            }
        return false;
    }
}