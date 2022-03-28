package utils;

import com.solbeg.BookLibrary.dto.UserRequestDto;
import com.solbeg.BookLibrary.model.Role;
import com.solbeg.BookLibrary.model.entity.User;

import static utils.LibraryTestConstants.ADMIN_USERNAME;
import static utils.LibraryTestConstants.DATE_ZONED_DATA_TIME1;
import static utils.LibraryTestConstants.DATE_ZONED_DATA_TIME2;
import static utils.LibraryTestConstants.FIRST_NAME1;
import static utils.LibraryTestConstants.FIRST_NAME2;
import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID2;
import static utils.LibraryTestConstants.LAST_NAME1;
import static utils.LibraryTestConstants.LAST_NAME2;
import static utils.LibraryTestConstants.PASSWORD1;
import static utils.LibraryTestConstants.PASSWORD2;
import static utils.LibraryTestConstants.USER_USERNAME1;
import static utils.LibraryTestConstants.USER_USERNAME2;
import static utils.LibraryTestConstants.USER_USERNAME3;

public class TestUserFactory {

    public static User createUser1() {
        User user = new User();
        user.setId(ID1);
        user.setUsername(USER_USERNAME1);
        user.setPassword(PASSWORD1);
        user.setFirstName(FIRST_NAME1);
        user.setLastName(LAST_NAME1);
        user.setCreatedAt(DATE_ZONED_DATA_TIME1);
        user.setUpdatedAt(DATE_ZONED_DATA_TIME1);
        user.setRole(Role.USER);
        return user;
    }

    public static User createUser2() {
        User user = new User();
        user.setId(ID2);
        user.setUsername(USER_USERNAME2);
        user.setPassword(PASSWORD2);
        user.setFirstName(FIRST_NAME2);
        user.setLastName(LAST_NAME2);
        user.setCreatedAt(DATE_ZONED_DATA_TIME2);
        user.setUpdatedAt(DATE_ZONED_DATA_TIME2);
        user.setRole(Role.USER);
        return user;
    }

    public static User createUser_ADMIN() {
        User user = new User();
        user.setId(ID1);
        user.setUsername(ADMIN_USERNAME);
        user.setPassword(PASSWORD1);
        user.setFirstName(FIRST_NAME1);
        user.setLastName(LAST_NAME1);
        user.setCreatedAt(DATE_ZONED_DATA_TIME1);
        user.setUpdatedAt(DATE_ZONED_DATA_TIME1);
        user.setRole(Role.ADMIN);
        return user;
    }

    public static UserRequestDto createUserRequestDto() {
        return new UserRequestDto(USER_USERNAME1, PASSWORD1, FIRST_NAME1, LAST_NAME1);
    }

    public static UserRequestDto createUserRequestDto3() {
        return new UserRequestDto(USER_USERNAME3, PASSWORD1, FIRST_NAME1, LAST_NAME1);
    }
}


