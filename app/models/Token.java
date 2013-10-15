package models;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Token extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public String uuid;

    public String email;

    public Date createdAt;

    public Date expireAt;

    public boolean isSignUp;

    public static Finder<String, Token> find = new Finder<String, Token>(
            String.class, Token.class
    );
}