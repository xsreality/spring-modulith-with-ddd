package example.useraccount.web;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Optional;

import example.useraccount.UserAccount;

/**
 * {@link HandlerMethodArgumentResolver} to inject the {@link UserAccount} of the currently logged-in user
 * into REST controller method parameters annotated with {@link Authenticated}. The parameter can
 * also use {@link Optional} as wrapper for {@link UserAccount} to indicate that an anonymous invocation is
 * possible.
 */
@Component
class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver, WebMvcConfigurer {

    private static final String USER_EXPECTED = "Expected to find a current user but none available! If the user does not necessarily have to be logged in, use Optional<User> instead!";
    private static final ResolvableType USER = ResolvableType.forClass(UserAccount.class);
    private static final ResolvableType OPTIONAL_OF_USER =
            ResolvableType.forClassWithGenerics(Optional.class, UserAccount.class);

    @SuppressWarnings("NullableProblems")
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var user = getCurrentUser();
        var parameterType = ResolvableType.forMethodParameter(parameter);

        if (OPTIONAL_OF_USER.isAssignableFrom(parameterType)) {
            return user;
        }

        return hasAuthorizationAnnotation(parameter)
                ? user.orElse(null)
                : user.orElseThrow(() -> new ServletRequestBindingException(USER_EXPECTED));
    }

    private Optional<UserAccount> getCurrentUser() {

        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(it -> (JwtAuthenticationToken) it)
                .map(token -> {
                    Jwt jwt = (Jwt) token.getPrincipal();
                    var email = jwt.getClaimAsString("email");
                    var firstName = jwt.getClaimAsString("given_name");
                    var lastName = jwt.getClaimAsString("family_name");
                    var roles = token.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

                    return new UserAccount(firstName, lastName, email, roles);
                });
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        if (!parameter.hasParameterAnnotation(Authenticated.class)) {
            return false;
        }

        var type = ResolvableType.forMethodParameter(parameter);

        return USER.isAssignableFrom(type) || OPTIONAL_OF_USER.isAssignableFrom(type);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this);
    }

    private static boolean hasAuthorizationAnnotation(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(PreAuthorize.class);
    }
}
