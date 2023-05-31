package pochemon.user.repository;

import org.springframework.data.repository.CrudRepository;
import pochemon.user.entity.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findAll();
    Boolean existsByLoginAndPwd(String login, String password);
}
