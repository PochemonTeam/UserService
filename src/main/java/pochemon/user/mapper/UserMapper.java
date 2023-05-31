package pochemon.user.mapper;

import org.mapstruct.Mapper;
import pochemon.dto.UserDTO;
import pochemon.user.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	public UserDTO toUserDTO(User user);
	
	public User toUser(UserDTO userDTO);
	
	public List<UserDTO> toUserDTOList(List<User> userList);
		
}
