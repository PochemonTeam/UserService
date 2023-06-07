package pochemon.user.controller;

import org.springframework.web.bind.annotation.*;
import pochemon.dto.AuthDTO;
import pochemon.dto.UserDTO;
import pochemon.dto.UserTokenDTO;
import pochemon.user.service.UserService;

import java.util.List;

@RestController 
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public UserDTO getUser(@PathVariable Integer id) {
		return userService.getUserById(id);
	}
	
	@PutMapping
	public Boolean editUser(@RequestBody UserDTO userDto) {
		return userService.addUser(userDto);
	}
	
	@DeleteMapping
	public Boolean removeUser(@RequestBody UserDTO userDto) {
		return userService.deleteUser(userDto);
	}
	
	@PostMapping
	public Boolean addUser(@RequestBody UserDTO userDto) {
		return userService.addUser(userDto);
	}
	
	@PostMapping("/auth")
	public UserTokenDTO authentication(@RequestBody AuthDTO authDTO) {
		return userService.authenticateUser(authDTO.getUsername(), authDTO.getPassword());
	}
	
	@GetMapping()
	public List<UserDTO> getAllUsers() {
		return userService.getAllUsers();
	}

}
