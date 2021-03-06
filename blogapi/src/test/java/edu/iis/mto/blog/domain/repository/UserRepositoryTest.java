package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0)
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test
    public void shouldFindUserWhenFirstNameIsCorrect() {
        String otherLastName = "Noname";
        String otherEmail = "matthew@edu.p.lodz.pl";
        repository.save(user);
        List<User> userList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), otherLastName, otherEmail);

        assertThat(userList, hasSize(1));
    }

    @Test
    public void shouldFindUserWhenLastNameIsCorrect() {
        String otherFirstName = "Matthew";
        String otherEmail = "matthew@edu.p.lodz.pl";
        repository.save(user);
        List<User> userList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(otherFirstName, user.getLastName(), otherEmail);

        assertThat(userList, hasSize(1));
    }

    @Test
    public void shouldFindUserWhenEmailIsCorrect() {
        String otherFirstName = "Matthew";
        String otherLastName = "Noname";
        repository.save(user);
        List<User> userList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(otherFirstName, otherLastName, user.getEmail());

        assertThat(userList, hasSize(1));
    }

    @Test
    public void shouldNotFindUser() {
        String otherFirstName = "Matthew";
        String otherLastName = "Noname";
        String otherEmail = "matthew@edu.p.lodz.pl";
        repository.save(user);
        List<User> userList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(otherFirstName, otherLastName, otherEmail);

        assertThat(userList, hasSize(0));
    }
}
