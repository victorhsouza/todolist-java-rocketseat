package com.vitech.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.vitech.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

//Toda classe que o spring tem qeu gerenciar tem que colocar esse component. É a classe mais generica de gerenciamento
@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks")) {
            //Pegar a autenticação (usuario e senha)
            var authorization = request.getHeader("Authorization");
            System.out.println("Authorization");
//        System.out.println(authorization);
            // Quebrando o nome basic e pegando so a senha criptografada. trim remove os espaços em branco. substring divide a string
            var authEncoded = authorization.substring("Basic".length()).trim();
            //decodifcando acesso
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
            //passando para striung
            var authString = new String(authDecoded);
            //convertendo para um array divindo o usuario e a senha
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];
//        System.out.println(username);
//        System.out.println(password);

            //validar Usuario
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401);
            } else {
                //validar Senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("idUser",user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
                //Segue viagem


            }
        } else{
            filterChain.doFilter(request, response);
        }


    }
}

