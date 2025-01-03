package org.project.exchange;

import org.junit.jupiter.api.Test;
import org.project.exchange.model.user.User;
import org.project.exchange.model.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ExchangeApplicationTests {

    @Autowired
    private UserDao userDao;
    @Test
    void contextLoads() {
        User user = new User();
        user.setName("test");
        user.setPhone_num("010-1234-1234");
        user.setGender(0);
        user.setEmail("test@test.com");
        user.setUsername("test");
        user.setPassword("123456");
        user.setBirth_date("12/12/1999");

        userDao.save(user);
    }

//    @Test
    void getALlUserAndDeleteThe(){
        List<User> users = userDao.getAllUSers();
        for (User user : users){
//            userDao.delete(user);
        }

    }

}
