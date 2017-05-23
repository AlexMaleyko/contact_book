package com.itechart.maleiko.contact_book.business.command;

import java.sql.Connection;

/**
 * Created by Alexey on 13.04.2017.
 */
public class CommandFactory {
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(SaveContact.class);

    public Command getCommand(Connection conn, String commandName){
        LOGGER.info("getCommand({})", commandName);
        Command command = null;
        if (commandName == null) {
            command = new GetContactList(conn);
        }
        else {
            switch (commandName) {
                case "SendEmail": {
                    command = new SendEmail(conn);
                    break;
                }
                case "EditContact":                 {
                    command = new EditContact(conn);
                    break;
            }
                case "SaveContact": {
                    command = new SaveContact(conn);
                    break;
                }
                case "SearchContacts": {
                    command = new SearchContacts(conn);
                    break;
                }
                case "CreateContact": {
                    command = new CreateContact(conn);
                    break;
                }
                case "GetContact": {
                    command = new GetContact(conn);
                    break;
                }
                case "DeleteContact": {
                    command = new DeleteContact(conn);
                    break;
                }
                case "ContactList": {
                    command = new GetContactList(conn);
                    break;
                }
                case "GetContactList": {
                    command = new GetContactList(conn);
                    break;
                }
                case "UpdateContact": {
                    command = new UpdateContact(conn);
                    break;
                }
            }
        }
        return command;
    }
}
