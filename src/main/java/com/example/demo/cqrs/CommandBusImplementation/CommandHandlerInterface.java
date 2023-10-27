package com.example.demo.cqrs.CommandBusImplementation;

public interface CommandHandlerInterface<T extends Command> {

    void handle(T command);
}
