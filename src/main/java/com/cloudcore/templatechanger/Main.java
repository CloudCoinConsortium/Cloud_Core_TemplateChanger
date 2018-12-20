package com.cloudcore.templatechanger;

public class Main {



    /* Methods */
    /**
     * Creates an Template Changer instance and runs it.
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.println("Hello");

            TemplateChanger templateChanger = new TemplateChanger();

            templateChanger.template();
        } catch (Exception e) {
            System.out.println("Uncaught exception - " + e.getLocalizedMessage());
        }
    }


}

