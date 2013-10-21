package helpers;

import models.User;

public class UnauthorizedException extends RuntimeException {
    
    private User user;
    private String action;     

    public UnauthorizedException(User user, String action) {
        this.user = user;
        this.action = action;
    }
 
    @Override
    public String getMessage() {
        return "User '" + user.displayName() + "' is not authorized to '" + action + "' this object";
    }

}