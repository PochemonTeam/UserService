package pochemon.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.dto.UserDTO;
import com.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	public UserDTO toUserDTO(User user);
	
	public User toUser(UserDTO userDTO);
	
	public List<UserDTO> toUserDTOList(List<User> userList);
		
}
