package com.itechart.maleiko.contact_book.business.service;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 10.04.2017.
 */
public class TemplateMessage {
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(TemplateMessage.class);
    private String name;
    private String template;
    private ST stTemplate;
    public TemplateMessage(String name){
        STGroup messageTemplates = new STGroupFile("messageTemplates.stg");
        ST template1 = messageTemplates.getInstanceOf(name);
        this.stTemplate = messageTemplates.getInstanceOf(name);
        if(name.equals("birthday")) {
            template1.add("name", "<Имя>");
            template1.add("patronymic", "<Отчество>");
        }
        if(name.equals("birthdayPlans")){
            template1.add("age", "< N >");
        }
        this.name=name;
        this.template = template1.render();
    }
    public static List<TemplateMessage> getAllTemplates(){
        List<TemplateMessage> templateMessages = new ArrayList<>();
        templateMessages.add(new TemplateMessage("birthday"));
        templateMessages.add(new TemplateMessage("birthdayPlans"));
        templateMessages.add(new TemplateMessage("meeting"));
        return templateMessages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public ST getStTemplate() {
        return stTemplate;
    }

    public void setStTemplate(ST stTemplate) {
        this.stTemplate = stTemplate;
    }
}
