package controllers;

import controllers.api.API;
import play.data.validation.Required;
import play.data.validation.URL;

public class Application extends API {

    public static void index(@Required @URL String id) {
        renderJSON("\n" +
                "{\n" +
                "  \"login\": \"fivesmallq\",\n" +
                "  \"id\": 745063,\n" +
                "  \"avatar_url\": \"https://avatars.githubusercontent.com/u/745063?v=3\",\n" +
                "  \"gravatar_id\": \"\",\n" +
                "  \"url\": \"https://api.github.com/users/fivesmallq\",\n" +
                "  \"html_url\": \"https://github.com/fivesmallq\",\n" +
                "  \"followers_url\": \"https://api.github.com/users/fivesmallq/followers\",\n" +
                "  \"following_url\": \"https://api.github.com/users/fivesmallq/following{/other_user}\",\n" +
                "  \"gists_url\": \"https://api.github.com/users/fivesmallq/gists{/gist_id}\",\n" +
                "  \"starred_url\": \"https://api.github.com/users/fivesmallq/starred{/owner}{/repo}\",\n" +
                "  \"subscriptions_url\": \"https://api.github.com/users/fivesmallq/subscriptions\",\n" +
                "  \"organizations_url\": \"https://api.github.com/users/fivesmallq/orgs\",\n" +
                "  \"repos_url\": \"https://api.github.com/users/fivesmallq/repos\",\n" +
                "  \"events_url\": \"https://api.github.com/users/fivesmallq/events{/privacy}\",\n" +
                "  \"received_events_url\": \"https://api.github.com/users/fivesmallq/received_events\",\n" +
                "  \"type\": \"User\",\n" +
                "  \"site_admin\": false,\n" +
                "  \"name\": \"fivesmallq\",\n" +
                "  \"company\": null,\n" +
                "  \"blog\": \"http://nll.im\",\n" +
                "  \"location\": \"Beijing.China\",\n" +
                "  \"email\": \"fivesmallq@gmail.com\",\n" +
                "  \"hireable\": true,\n" +
                "  \"bio\": \"Complexity is the path to the dark side.\",\n" +
                "  \"public_repos\": 40,\n" +
                "  \"public_gists\": 23,\n" +
                "  \"followers\": 26,\n" +
                "  \"following\": 17,\n" +
                "  \"created_at\": \"2011-04-22T01:56:45Z\",\n" +
                "  \"updated_at\": \"2016-11-16T07:01:49Z\"\n" +
                "}\n");
    }

}
