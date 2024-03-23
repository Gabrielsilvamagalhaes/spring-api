package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.UserLoginDto;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import com.gabriel.course.projectapi2.jwt.JwtToken;
import com.gabriel.course.projectapi2.jwt.JwtUserDetails;
import com.gabriel.course.projectapi2.jwt.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/auth/")
@RestController
@Slf4j
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUserDetailsService detailsService;

    @PostMapping
    public ResponseEntity<?> authenticate (@RequestBody @Valid  UserLoginDto dto, HttpServletRequest request) {
      log.info("Processo de autenticacao pelo login {}", dto.getUsername());

      try{
          UsernamePasswordAuthenticationToken authenticationToken =
                  new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
          authenticationManager.authenticate(authenticationToken);

          JwtToken token = detailsService.getToken(dto.getUsername());
          return  ResponseEntity.ok(token);

      }catch (AuthenticationException exception) {
          log.warn("Erro na autenticação");
      }

      return  ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais Invalidas!"));
    }

}