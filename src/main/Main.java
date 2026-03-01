package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;

import checker.Checker;
import checker.CheckerConstants;

import fileio.input.LibraryInput;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

import gwaves.storage.DataBase;
import gwaves.tools.UserManager;
import gwaves.tools.CommandExecuter;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                                                               + "library/library.json"),
                                                               LibraryInput.class);

        DataBase database;
        UserManager userManager;

        DataBase.changeInstance();
        database = DataBase.getInstance();
        database.loadDataBase(library);
        UserManager.changeInstance();
        userManager = UserManager.getInstance();
        userManager.loadUsers(database.queryAllNormalUsers());

        ArrayList<CommandInput> commandInputs;
        ArrayList<CommandOutput> commandOutputs;

        commandInputs = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                                               new TypeReference<ArrayList<CommandInput>>() { });
        commandOutputs = new ArrayList<>();

        for (var commandInput : commandInputs) {
            userManager.updateAll(commandInput.getTimestamp());

            commandOutputs.add(CommandExecuter.run(commandInput));
        }

        commandOutputs.add(CommandExecuter.run(new CommandInput()));

        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(filePath2), commandOutputs);
    }
}
