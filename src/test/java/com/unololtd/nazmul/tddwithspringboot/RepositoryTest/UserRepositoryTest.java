package com.unololtd.nazmul.tddwithspringboot.RepositoryTest;

import com.unololtd.nazmul.tddwithspringboot.model.User;
import com.unololtd.nazmul.tddwithspringboot.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@ActiveProfiles("test") //active bean definition profiles
@DataJpaTest // test JPA repositories
@RunWith(SpringRunner.class) //provide a bridge between Spring Boot test features and JUnit
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createReadAndDelete(){
        //Given
        create();

        // when
        Optional<User> found = userRepository.findUserByEmail("akib@gmail.com");

        // then
        Assertions.assertThat(found.get().getId().longValue());
        Assertions.assertThat(found.get().getUsername().equals("akib"));

        //finally remove and check
        userRepository.deleteById(found.get().getId());
        assertNull(userRepository.findUserByEmail("akib@gmail.com").orElse(null));
    }

    @Test
    public void createReadUpdateReadDelete(){
        //Given
        create();

        // when
        Optional<User> found = userRepository.findUserByEmail("akib@gmail.com");

        // then
        Assertions.assertThat(found.get().getId().longValue());
        Assertions.assertThat(found.get().getUsername().equals("akib"));

        //update
        User oldUser = found.get();
        oldUser.setEmail("akib@yahoo.com");
        entityManager.persist(oldUser);
        entityManager.flush();

        //Again when
        found = userRepository.findUserByEmail("akib@yahoo.com");

        // then
        Assertions.assertThat(found.get().getId().longValue());
        Assertions.assertThat(found.get().getId().equals(oldUser.getId()));
        Assertions.assertThat(found.get().getUsername().equals("akib"));

        //finally remove and check
        userRepository.deleteById(found.get().getId());
        assertNull(userRepository.findUserByEmail("akib@yahoo.com").orElse(null));
    }

    public void create(){
        User user = new User("akib", "akib@gmail.com", LocalDate.of(2019,03,05));
        entityManager.persist(user);
        entityManager.flush();
    }
}
