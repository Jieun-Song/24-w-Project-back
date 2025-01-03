package org.project.exchange.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDao {
    @Autowired//필드 인젝션이라 개별로인거 아는데 포스팅 그대로 따라가느라 이케 함 나중에 생성자 주입으로 바꿀게여
    private UserRepository repository;

    public User save(User user) {
        return repository.save(user);// repository함수가 자동으로 해줌
    }

    public List<User> getAllUSers(){
        List<User> users = new ArrayList<>();
        Streamable.of(repository.findAll()).forEach(users::add);

        return users;
    }
    public void delete(int id){
        repository.deleteById(id);
    }
}
