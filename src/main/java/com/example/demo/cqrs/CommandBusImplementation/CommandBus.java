package com.example.demo.cqrs.CommandBusImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandBus {
    private final Map<Class<?>, CommandHandlerInterface<?>> commandHandlers;

    @Autowired
    public CommandBus(List<CommandHandlerInterface<?>> commandHandlerList) {
        this.commandHandlers = new HashMap<>();
        for (CommandHandlerInterface<?> handler : commandHandlerList) {
            Class<?> commandClass = getCommandClass(handler);
            if (commandClass != null) {
                commandHandlers.put(commandClass, handler);
            } else {
                throw new IllegalArgumentException("Unable to resolve query type.");
            }
        }
    }

    private Class<?> getCommandClass(CommandHandlerInterface<?> handler) {
        CommandHandler annotation = handler.getClass().getAnnotation(CommandHandler.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

    public void dispatch(Command command) {
        CommandHandlerInterface<Command> handler = (CommandHandlerInterface<Command>) commandHandlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for the command.");
        }
        handler.handle(command);
    }
}