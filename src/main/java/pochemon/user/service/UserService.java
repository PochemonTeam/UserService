package pochemon.user.service;

import java.net.URISyntaxException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import pochemon.dto.UserDTO;
import pochemon.dto.UserTokenDTO;
import pochemon.service.AuthWebService;
import pochemon.user.entity.User;
import pochemon.user.mapper.UserMapper;
import pochemon.user.repository.UserRepository;

@Slf4j
@Service
public class UserService {
	UserMapper userMapper;
	UserRepository userRepository;
	AuthWebService authWebService;

	public UserService(UserMapper userMapper, UserRepository userRepository) {
		this.userMapper = userMapper;
		this.userRepository = userRepository;
		this.authWebService = new AuthWebService();
	}

	public UserDTO getUserById(Integer id) {
		return userMapper.toUserDTO(userRepository.findById(id).orElse(null));
	}

	public List<UserDTO> getAllUsers() {
		return userMapper.toUserDTOList(userRepository.findAll());
	}

	public Boolean addUser(UserDTO userDto) {
		if (userDto != null) {
			// Save le user dans la base de données
			userRepository.save(userMapper.toUser(userDto));
			// Appel le AuthService et enregistre l'association user/password
			try {
				authWebService.register(userDto.getLogin(), userDto.getPwd());
			} catch (URISyntaxException e) {
				log.error(e.getMessage());
				return false;
			}
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

	public UserTokenDTO authenticateUser(String username, String password) {
		UserTokenDTO userTokenDTO = new UserTokenDTO();
		User user = userRepository.findByLogin(username);
		if(user != null) {
			String token = authWebService.login(username, password);
			log.info("TEMPORARY: token : ", token);
			userTokenDTO.setUser(userMapper.toUserDTO(user));
			userTokenDTO.setToken(token);
		}
		return userTokenDTO;
	}
}
