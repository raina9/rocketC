package com.weblearnex.app.reposatory;


import com.weblearnex.app.setup.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken,Long> {
    public JwtToken findByToken(String token);
    public boolean deleteJwtTokenByToken(String token);
}
