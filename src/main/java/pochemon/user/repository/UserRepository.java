package pochemon.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import pochemon.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();
    User findByLogin(String login);
}
