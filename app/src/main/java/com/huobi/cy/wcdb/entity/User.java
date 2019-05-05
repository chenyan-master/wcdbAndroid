package com.huobi.cy.wcdb.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by johnwhe on 2017/7/12.
 */

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;

    public String firstName;
    public String lastName;
    public String account;
    public String mobile;
    public String email;
    public String summary;
    public String logo;
    public String biglogo;
    public int isfriend;
    public int version;
    public String modifytime;
    public int champflag;
    public int crowntype;
    public int authtype;
    public String legalizetag;
    public String fullspell;
    public String initspell;

    @Override
    public String toString() {
        return new StringBuilder().append("{ firstName: ").append(firstName)
                .append(",lastName: ").append(lastName)
                .append(",account: ").append(account)
                .append(",mobile: ").append(mobile)
                .append(",email: ").append(email)
                .append(",summary: ").append(summary)
                .append(",logo: ").append(logo)
                .append(",biglogo: ").append(biglogo)
                .append(",isfriend: ").append(isfriend)
                .append(",version: ").append(version)
                .append(",modifytime: ").append(modifytime)
                .append(",champflag: ").append(champflag)
                .append(",crowntype: ").append(crowntype)
                .append(",authtype: ").append(authtype)
                .append(",legalizetag: ").append(legalizetag)
                .append(",fullspell: ").append(fullspell)
                .append(",initspell: ").append(initspell)
                .append(" }").toString();
    }
}
