package com.itechart.maleiko.contact_book.web.command;

import com.itechart.maleiko.contact_book.business.service.ContactController;

public class CommandFactory {
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(CommandFactory.class);

    public Command getCommand(String commandName){
        LOGGER.info("Command requested by path: {} ", commandName);
        Command command;
        if (commandName == null || commandName.equals("/") || commandName.equals("/contacts")) {
            command = new GetContactList();
        }else if(commandName.matches("/contacts/\\d+")){
            command = new GetContact();
        }
        else {
            switch (commandName) {
                case "/SendEmail": {//+
                    command = new SendEmail();
                    break;
                }
                case "/EditContact": {//+
                    command = new EditContact();
                    break;
            }
                case "/SaveContact": {//+
                    command = new SaveContact();
                    break;
                }
                case "/Search": {//+
                    command = new SearchContacts();
                    break;
                }
                case "/contacts/new": {//+
                    command = new CreateContact();
                    break;
                }
                case "/DeleteContact": {//+
                    command = new DeleteContact();
                    break;
                }
                case "/UpdateContact": {
                    command = new UpdateContact();
                    break;
                }
                case "/image":{
                    command = new GetImage(new ContactController());
                    break;
                }
                case "/attachment":{
                    command = new GetAttachment(new ContactController());
                    break;
                }
                default:{
                    LOGGER.error("Unknown command");
                    command = new UnknownCommand();
                    break;
                }
            }
        }
        return command;
    }
}
