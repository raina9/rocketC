package com.weblearnex.app.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@JsonDeserialize(as = UserDetails.class)
public class SecureUserDetails implements UserDetails
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
    private String password;
    private Integer active;
    private boolean isLocked;
    private boolean isExpired;
    private boolean isEnabled;
    private List<GrantedAuthority> grantedAuthorities;

    public SecureUserDetails(String userId, String password,Integer active, boolean isLocked, boolean isExpired, boolean isEnabled, String [] authorities) {
        this.userId = userId;
        this.password = password;
        this.active = active;
        this.isLocked = isLocked;
        this.isExpired = isExpired;
        this.isEnabled = isEnabled;
        this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
    }

    public SecureUserDetails(String userId,  String [] authorities) {
        this.userId = userId;
        this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
    }

    public SecureUserDetails() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active==1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
