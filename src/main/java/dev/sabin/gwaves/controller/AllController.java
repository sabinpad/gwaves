package dev.sabin.gwaves.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sabin.gwaves.model.io.input.CommandInput;
import dev.sabin.gwaves.model.io.output.CommandOutput;
import dev.sabin.gwaves.service.tool.CommandExecuter;
import dev.sabin.gwaves.service.tool.UserManager;

@RestController
public class AllController {
    private UserManager userManager;

    public AllController() {
        this.userManager = UserManager.getInstance();
    }

    @GetMapping("/")
    public String home() { return "Welcome to Gwaves!"; }

    @PostMapping("/command")
    public ArrayList<CommandOutput> processCommand(@RequestBody ArrayList<CommandInput> commandInputs) throws Exception{
        ArrayList<CommandOutput> commandOutputs = new ArrayList<>();

        for (var commandInput : commandInputs) {
            this.userManager.updateAll(commandInput.getTimestamp());

            commandOutputs.add(CommandExecuter.run(commandInput));
        }

        commandOutputs.add(CommandExecuter.run(new CommandInput()));

        return commandOutputs;
    }
}
