package ServiceTest;

import com.store.musicstore.models.User;
import com.store.musicstore.models.enums.Role;
import com.store.musicstore.repositories.UserRepository;
import com.store.musicstore.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void testCreateUser_Failure_EmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setEmail("existinguser@example.com");
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

        boolean result = userService.createUser(existingUser);

        assertFalse(result);
        verify(userRepository).findByEmail(existingUser.getEmail());
        verify(userRepository, never()).save(existingUser);
    }

    @Test
    public void testListUsers() {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User());
        mockUsers.add(new User());

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.list();

        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    public void testBanUser_Ban() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.banUser(userId);

        assertFalse(user.isActive());
        verify(userRepository).save(user);
        verify(userRepository).findById(userId);
    }

    @Test
    public void testBanUser_Unban() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.banUser(userId);

        assertTrue(user.isActive());
        verify(userRepository).save(user);
        verify(userRepository).findById(userId);
    }

    @Test
    public void testChangeUserRoles() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("testuser@example.com");
        user.setRoles(new HashSet<>());

        String newRole = "ROLE_ADMIN";

        userService.changeUserRoles(user, newRole);

        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(Role.ROLE_ADMIN));
        verify(userRepository).save(user);
    }
}