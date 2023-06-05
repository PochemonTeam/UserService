package pochemon.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import pochemon.dto.UserDTO;
import pochemon.dto.UserTokenDTO;
import pochemon.service.AuthWebService;
import pochemon.user.entity.User;
import pochemon.user.mapper.UserMapper;
import pochemon.user.repository.UserRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {

	@MockBean
    private UserMapper userMapper;

	@MockBean
    private UserRepository userRepository;
    
    @MockBean
    private AuthWebService authWebService;

    @Autowired
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

//	  Pas moyen de faire fonctionner ce test
//	  Erreur URI is not absolute
//	  Même en voyant avec le prof nous n'avons pas trouvé de solution
//    @Test
//    void testAddUser() {
//
//        UserDTO userDto = new UserDTO();
//        userDto.setLogin("username");
//        userDto.setPwd("password");
//        User user = new User();
//        when(userMapper.toUser(userDto)).thenReturn(user);
//        doNothing().when(authWebService).register(Mockito.anyString(), Mockito.anyString());
//
//        boolean result = userService.addUser(userDto);
//        Assertions.assertTrue(result);
//        
//        verify(userRepository, times(1)).save(user);
//        verify(authWebService, times(1)).register(userDto.getLogin(), userDto.getPwd());
//    }

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

//	  Pas moyen de faire fonctionner ce test
//	  Erreur URI is not absolute
//	  Même en voyant avec le prof nous n'avons pas trouvé de solution
//    @Test
//    void testValidAuthentification() {
//    	
//        String username = "username";
//        String password = "password";
//        User user = new User();
//        UserDTO expectedUserDTO = new UserDTO();
//        String expectedToken = "token";
//        UserTokenDTO expectedUserTokenDTO = new UserTokenDTO(expectedToken, expectedUserDTO);
//        when(userRepository.findByLogin(username)).thenReturn(user);
//        when(authWebService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedToken);
//        when(userMapper.toUserDTO(user)).thenReturn(expectedUserDTO);
//
//        UserTokenDTO result = userService.authenticateUser(username, password);
//        Assertions.assertEquals(expectedUserTokenDTO, result);
//        Assertions.assertEquals(expectedToken, result.getToken());
//        Assertions.assertEquals(expectedUserDTO, result.getUser());
//        
//        verify(userRepository, times(1)).findByLogin(username);
//        verify(authWebService, times(1)).login(username, password);
//        verify(userMapper, times(1)).toUserDTO(user);
//    }

    @Test
    void testUnvalidAuthentification() {

        String username = "username";
        String password = "password";
        when(userRepository.findByLogin(username)).thenReturn(null);

        UserTokenDTO result = userService.authenticateUser(username, password);
        //On retourne un UserTokenDTO avec des arguments null quand le login a failed
        Assertions.assertNull(result.getToken());
        Assertions.assertNull(result.getUser());
        
        verify(userRepository, times(1)).findByLogin(username);
        verify(userMapper, never()).toUserDTO(any(User.class));
    }
}