package controllers.v1;

import controllers.api.API;
import models.User;
import play.data.validation.Min;
import play.data.validation.Required;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/11/24 下午11:45
 */
//@With({Secure.class})
public class Users extends API {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private static Map<Long, User> users = new LinkedHashMap<>();

    static {
        for (int i = 0; i < 10; i++) {
            users.put(counter.longValue(), randomUser());
        }
    }

    public static void get(@Required @Min(1) Long id) {
        forbiddenAccess(id);
        User user = users.get(id);
        if (user != null) {
            renderJSON(user);
        } else {
            notFoundBy(id);
        }
    }

    public static void save() {
        //List<User> users2 = readBodyList(User.class);
        User user = readBody(User.class);
        users.put(111L, user);
        //created(user.toPrettyJson());
        created(user);
    }


    public static void list() {
        renderJSON(users.values());
    }

    private static User randomUser() {
        User user = new User();
        user.id = counter.longValue();
        counter.incrementAndGet();
        user.userName = "userName-" + user.id;
        user.password = "password-" + user.id;
        user.createDate = new Date();
        user.state = new Random().nextInt();
        return user;
    }
}
