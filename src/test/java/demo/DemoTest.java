package demo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.Test;


public class DemoTest {

    @Data
    @Builder
    @ToString
    static class SignUpRequest {
        private String firstName;
        private String LastName;
        private String name;
    }

    @Data
    @Builder
    @ToString
    static class User {
        private String firstName;
        private String LastName;
        private String name;
    }

    @Test
    void testNullStrings() {
        final User user = User.builder().build();
        final SignUpRequest signUpRequest = SignUpRequest.builder()
                .firstName("Vladimir")
                .LastName("Egorov")
                .name(null)
                .build();

        System.out.println(user);

        user.setName( (signUpRequest.getFirstName() != null && signUpRequest.getLastName() != null) ? signUpRequest.getFirstName() + " " + signUpRequest.getLastName() : signUpRequest.getName() );
        user.setFirstName( signUpRequest.getName() != null ? String.valueOf(signUpRequest.getName()).split(" ")[0] : signUpRequest.getFirstName() );
        user.setLastName( signUpRequest.getName() != null ? String.valueOf(signUpRequest.getName()).split(" ")[1] : signUpRequest.getLastName() );

        System.out.println(user);

    }
}
