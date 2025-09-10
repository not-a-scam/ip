package nomz.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import nomz.commands.AddDeadlineCommand;
import nomz.commands.AddEventCommand;
import nomz.commands.AddTodoCommand;
import nomz.commands.ByeCommand;
import nomz.commands.Command;
import nomz.commands.CommandType;
import nomz.commands.DeleteCommand;
import nomz.commands.FindCommand;
import nomz.commands.ListCommand;
import nomz.commands.MarkCommand;
import nomz.common.Messages;
import nomz.data.exception.InvalidNomzArgumentException;
import nomz.data.exception.InvalidNomzCommandException;
import nomz.data.exception.NomzException;
import nomz.data.tasks.Deadline;
import nomz.data.tasks.Event;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskType;
import nomz.data.tasks.Todo;

/**
 * Parses user input into commands and arguments.
 */
public class Parser {

    private static final DateTimeFormatter[] DATE_TIME_FORMATS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
        DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
        DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    };

    private static final DateTimeFormatter[] DATE_ONLY_FORMATS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("d/M/yyyy"),
        DateTimeFormatter.ISO_LOCAL_DATE
    };

    private static LocalDateTime parseDateTimeFlexible(String s) {
        assert s != null : "Date string should not be null";
        for (DateTimeFormatter f : DATE_TIME_FORMATS) {
            try {
                return LocalDateTime.parse(s, f);
            } catch (DateTimeParseException ignored) {
                // Continue to next format
            }
        }

        for (DateTimeFormatter f : DATE_ONLY_FORMATS) {
            try {
                return LocalDate.parse(s, f).atStartOfDay();
            } catch (DateTimeParseException ignored) {
                // Continue to next format
            }
        }

        return null;
    }

    private static int intFromString(String index) throws InvalidNomzArgumentException {
        assert index != null : "Index string should not be null";
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException e) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_INTEGER_ARGUMENT);
        }
    }

    /**
     * Parses a task from the file content.
     * @param f The file content string.
     * @return The parsed Task object.
     * @throws NomzException If the file content is invalid.
     */
    public static Task parseTaskFileContent(String f) throws NomzException {
        String[] args = f.split("[\\|]");
        assert args.length > 0 : "Input should not be empty";

        TaskType type = TaskType.fromSymbol(args[0]);
        assert type != null : "TaskType should not be null";

        boolean done = args[1].equals("1");
        switch (type) {
        case TODO:
            assert args.length >= 3 : "Todo save string should have 3 arguments";
            Todo todo = new Todo(args[2]);
            if (done) {
                todo.mark();
            }
            return todo;

        case DEADLINE:
            assert args.length >= 4 : "Deadline save string should have 4 arguments";
            LocalDateTime by = parseDateTimeFlexible(args[3]);
            Deadline deadline;
            if (by == null) {
                deadline = new Deadline(args[2], args[3]);
            } else {
                deadline = new Deadline(args[2], by);
            }
            if (done) {
                deadline.mark();
            }
            return deadline;

        case EVENT:
            assert args.length >= 5 : "Event save string should have 5 arguments";
            LocalDateTime from = parseDateTimeFlexible(args[3]);
            LocalDateTime to = parseDateTimeFlexible(args[4]);
            Event event;
            if (from == null || to == null) {
                event = new Event(args[2], args[3], args[4]);
            } else {
                event = new Event(args[2], from, to);
            }
            if (done) {
                event.mark();
            }
            return event;

        default:
            throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_FORMAT);
        }
    }

    /**
     * Joins the arguments from the specified range into a single string.
     * @param from The starting index (inclusive).
     * @param to The ending index (exclusive).
     * @param args The array of arguments.
     * @return The joined string.
     */
    private static String joinArgs(int from, int to, String... args) {
        assert args != null : "Arguments array should not be null";
        assert from >= 0 && to <= args.length && from <= to : "Invalid joinArgs indices";
        return String.join(" ", Arrays.copyOfRange(args, from, to));
    }

    /**
     * Parses a user command from the input string.
     *
     * @param input The input string to parse.
     * @return The corresponding Command object.
     * @throws NomzException If the input is invalid.
     */
    public static Command parse(String input) throws NomzException {
        String[] args = input.trim().split("\\s+");
        assert args.length > 0 : "Input should not be empty";

        CommandType cmd = CommandType.fromString(args[0]);
        assert cmd != null : "CommandType should not be null";
        switch (cmd) {
        case LIST:
            return new ListCommand();

        case BYE:
            return new ByeCommand();

        case MARK:
        // Fallthrough
        case UNMARK: {
            if (args.length < 2) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_INDEX_ARGUMENT);
            }
            int idx = intFromString(args[1]);
            boolean toMark = (cmd == CommandType.MARK);
            return new MarkCommand(idx, toMark);
        }

        case DELETE: {
            if (args.length < 2) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_INDEX_ARGUMENT);
            }
            int idx = intFromString(args[1]);
            return new DeleteCommand(idx);
        }

        case FIND: {
            if (args.length < 2) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_ARGUMENTS);
            }
            String keyword = joinArgs(1, args.length, args);
            return new FindCommand(keyword);
        }

        case TODO: {
            if (args.length < 2) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_DESCRIPTION_ARGUMENT);
            }
            String description = joinArgs(1, args.length, args);
            return new AddTodoCommand(description);
        }

        case DEADLINE: {
            if (args.length < 4) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_ARGUMENTS);
            }
            int byPos = -1;
            for (int i = 2; i < args.length; i++) {
                if (args[i].equals("/by")) {
                    byPos = i;
                    break;
                }
            }
            if (byPos == -1) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_BY_KEYWORD);
            }
            String description = joinArgs(1, byPos, args);
            String byRaw = joinArgs(byPos + 1, args.length, args);
            LocalDateTime by = parseDateTimeFlexible(byRaw);
            if (by != null) {
                return new AddDeadlineCommand(description, by);
            }
            return new AddDeadlineCommand(description, byRaw);
        }

        case EVENT: {
            int fromIndex = -1;
            int toIndex = -1;
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("/from")) {
                    fromIndex = i;
                } else if (args[i].equals("/to")) {
                    toIndex = i;
                }
            }
            if (fromIndex <= 1) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_WRONG_FROM_KEYWORD);
            }
            if (toIndex <= fromIndex || toIndex <= 3 || toIndex == args.length - 1) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_WRONG_TO_KEYWORD);
            }
            String description = joinArgs(1, fromIndex, args);
            String fromRaw = joinArgs(fromIndex + 1, toIndex, args);
            String toRaw = joinArgs(toIndex + 1, args.length, args);
            LocalDateTime from = parseDateTimeFlexible(fromRaw);
            LocalDateTime to = parseDateTimeFlexible(toRaw);
            if (from != null && to != null) {
                return new AddEventCommand(description, from, to);
            }
            return new AddEventCommand(description, fromRaw, toRaw);
        }

        default:
            throw new InvalidNomzCommandException();
        }
    }

}
