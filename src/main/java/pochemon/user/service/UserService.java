package pochemon.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pochemon.dto.UserDTO;
import pochemon.user.entity.User;
import pochemon.user.mapper.UserMapper;
import pochemon.user.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	UserRepository userRepository;

	public UserDTO getUserById(Integer id) {
		return userMapper.toUserDTO(userRepository.findById(id).orElse(null));
	}

	public List<UserDTO> getAllUsers() {
		return userMapper.toUserDTOList(userRepository.findAll());
	}

	public Boolean addUser(UserDTO userDto) {
		if (userDto != null) {
			userRepository.save(userMapper.toUser(userDto));
			return true;
		}
		return false;
	}

	public Boolean deleteUser(UserDTO userDto) {
		if (userDto != null) {
			userRepository.delete(userMapper.toUser(userDto));
			return true;
		}
		return false;
	}

	public Boolean changeMoney(User user, Float money) {
		if(user.getAccount() + money < 0f) {
			return false;
		}
 		user.setAccount(user.getAccount() + money);
		userRepository.save(user);
		return true;
	}

	public Boolean authenticateUser(String username, String password) {
		return userRepository.existsByLoginAndPwd(username, password);
	}
}
