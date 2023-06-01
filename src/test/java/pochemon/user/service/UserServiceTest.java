package pochemon.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pochemon.dto.UserDTO;
import pochemon.user.entity.User;
import pochemon.user.mapper.UserMapper;
import pochemon.user.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {

        Integer userId = 1;
        User user = new User();
        UserDTO expectedUserDTO = new UserDTO();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(expectedUserDTO);

       
        UserDTO result = userService.getUserById(userId);
        Assertions.assertEquals(expectedUserDTO, result);
        
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toUserDTO(user);
    }

    @Test
    void testGetAllUsers() {

        List<User> users = Arrays.asList(new User(), new User());
        List<UserDTO> expectedUserDTOs = Arrays.asList(new UserDTO(), new UserDTO());
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toUserDTOList(users)).thenReturn(expectedUserDTOs);

        List<UserDTO> result = userService.getAllUsers();
        Assertions.assertEquals(expectedUserDTOs, result);
        
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toUserDTOList(users);
    }

    @Test
    void testAddUser() {
    	
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userMapper.toUser(userDTO)).thenReturn(user);

        Boolean result = userService.addUser(userDTO);
        Assertions.assertTrue(result);
        
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toUser(userDTO);
    }

    @Test
    void testDeleteUser() {

        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userMapper.toUser(userDTO)).thenReturn(user);

        Boolean result = userService.deleteUser(userDTO);
        Assertions.assertTrue(result);
        
        verify(userRepository, times(1)).delete(any(User.class));
        verify(userMapper, times(1)).toUser(userDTO);
    }

    @Test
    void testChangeMoney_withEnoughMoney() {

        User user = new User();
        user.setAccount(100.0f);
        Float money = 50.0f;

        Boolean result = userService.changeMoney(user, money);
        Assertions.assertTrue(result);
        Assertions.assertEquals(150.0f, user.getAccount());
        
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangeMoney_withNotEnoughMoney() {
        
        User user = new User();
        user.setAccount(100.0f);
        Float money = -200.0f;
      
        Boolean result = userService.changeMoney(user, money);
        Assertions.assertFalse(result);
        Assertions.assertEquals(100.0f, user.getAccount());
        
        verify(userRepository, never()).save(user);
    }

    @Test
    void testAuthenticateUser_withValidCredentials() {
        
        String username = "john.doe";
        String password = "password";
        when(userRepository.existsByLoginAndPwd(username, password)).thenReturn(true);

        
        Boolean result = userService.authenticateUser(username, password);
        Assertions.assertTrue(result);
        
        verify(userRepository, times(1)).existsByLoginAndPwd(username, password);
    }

    @Test
    void testAuthenticateUser_withInvalidCredentials() {
        
        String username = "john.doe";
        String password = "password";
        when(userRepository.existsByLoginAndPwd(username, password)).thenReturn(false);

        Boolean result = userService.authenticateUser(username, password);    
        Assertions.assertFalse(result);
        
        verify(userRepository, times(1)).existsByLoginAndPwd(username, password);
    }
}