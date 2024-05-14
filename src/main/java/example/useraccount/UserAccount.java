package example.useraccount;

import java.util.List;

/**
 * A model to represent a user account. This is not promoted to Aggregate as there is no need yet.
 */
public record UserAccount(String firstName, String lastName, String email, List<String> roles) {

}
