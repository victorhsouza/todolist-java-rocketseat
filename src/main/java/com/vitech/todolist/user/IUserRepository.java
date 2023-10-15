package com.vitech.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
//    Metodo do proprio spring que ja vai procurar na coluna de usuario se tem um ususuario com esse nome
    UserModel findByUsername(String username);
}
