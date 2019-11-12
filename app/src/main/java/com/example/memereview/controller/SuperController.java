package com.example.memereview.controller;

public class SuperController {
    private static SuperController controller;

    public AccountController accountController;

    private SuperController(){
        accountController = new AccountController();
    }

    public static SuperController getInstance()
    {
        if(controller == null)
        {
            controller = new SuperController();
        }
        return controller;
    }
}
