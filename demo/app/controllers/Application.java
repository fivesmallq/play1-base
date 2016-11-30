package controllers;

import controllers.api.API;

public class Application extends API {

    public static void index() {
        renderJSON("hello world");
    }

}
