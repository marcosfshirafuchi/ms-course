package com.marcosfshirafuchi.hroauth.feign;

import com.marcosfshirafuchi.hroauth.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hr-user")
public interface UserFeignClient {
    @GetMapping(value = "/users/search")
    UserDTO findByEmailQuery(@RequestParam("email") String email);

    @GetMapping(value = "/users/email/{email}")
    UserDTO findByEmailPath(@PathVariable("email") String email);
}
