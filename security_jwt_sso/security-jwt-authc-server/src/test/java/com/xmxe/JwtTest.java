package com.xmxe;


import com.xmxe.utils.RsaUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class JwtTest {
    private String privateKey = "c:/e/import/id_key_rsa";

    private String publicKey = "c:/e/import/id_key_rsa.pub";

    @Test
    public void test1() throws Exception{
        RsaUtils.generateKey(publicKey,privateKey,"dpb",1024);
    }


    @Test
    public void test2(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("jwt"));

        //PasswordEncoder passwordEncoder = new PasswordEncoder() ;
    }

}