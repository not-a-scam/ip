package nomz.parser;

import static nomz.common.Messages.MESSAGE_INVALID_FORMAT;
import static nomz.common.Messages.MESSAGE_INVALID_INTEGER_ARGUMENT;
import static nomz.common.Messages.MESSAGE_NO_ARGUMENTS;
import static nomz.common.Messages.MESSAGE_NO_BY_KEYWORD;
import static nomz.common.Messages.MESSAGE_NO_DESCRIPTION_ARGUMENT;
import static nomz.common.Messages.MESSAGE_NO_INDEX_ARGUMENT;
import static nomz.common.Messages.MESSAGE_WRONG_FROM_KEYWORD;
import static nomz.common.Messages.MESSAGE_WRONG_TO_KEYWORD;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
import nomz.commands.TagCommand;
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

    private static LocalDateTime tryParseDateTime(String s, DateTimeFormatter f) {
        try {
            return LocalDateTime.parse(s, f);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static LocalDate tryParseDate(String s, DateTimeFormatter f) {
        try {
            return LocalDate.parse(s, f);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static LocalDateTime parseDateTimeFlexible(String s) {
        assert s != null : "Date string should not be null";
        for (DateTimeFormatter f : DATE_TIME_FORMATS) {
            LocalDateTime result = tryParseDateTime(s, f);
            if (result != null) {
                return result;
            }
        }

        for (DateTimeFormatter f : DATE_ONLY_FORMATS) {
            LocalDate result = tryParseDate(s, f);
            if (result != null) {
                return result.atStartOfDay();
            }
        }
        return null;
    }

    private static int intFromString(String index) throws InvalidNomzArgumentException {
        assert index != null : "Index string should not be null";
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException e) {
            throw new InvalidNomzArgumentException(MESSAGE_INVALID_INTEGER_ARGUMENT);
        }
    }

    private static ArrayList<String> parseTags(String rawTags) {
        ArrayList<String> tags;
        if (rawTags.length() <= 6) {
            return new ArrayList<>();
        }
        assert rawTags.length() > 6 : "Tag string is too short";
        String[] strArr = rawTags.substring(6, rawTags.length() - 1).trim().split("[,]");
        tags = new ArrayList<>(Arrays.asList(strArr));

        return tags;
    }

    private static Todo handleTodoString(String... args) {
        assert args.length >= 3 : "Todo save string should have at least 3 arguments";

        boolean done = args[1].equals("1");

        String rawTags = args.length >= 4 ? args[3].trim() : "";
        ArrayList<String> tags = parseTags(rawTags);

        Todo todo = new Todo(args[2], tags);
        if (done) {
            todo.mark();
        }
        return todo;
    }

    private static Deadline handleDeadlineString(String... args) {
        assert args.length >= 4 : "Deadline save string should have at least 4 arguments";

        boolean done = args[1].equals("1");
        String description = args[2];
        String rawBy = args[3];
        String rawTags = args.length >= 5 ? args[4].trim() : "";

        ArrayList<String> tags = parseTags(rawTags);

        LocalDateTime by = parseDateTimeFlexible(rawBy);
        Deadline deadline;

        if (by == null) {
            deadline = new Deadline(description, rawBy, tags);
        } else {
            deadline = new Deadline(description, by, tags);
        }

        if (done) {
            deadline.mark();
        }

        return deadline;
    }

    private static Event handleEventString(String... args) {
        assert args.length >= 6 : "Event save string should have 6 arguments";

        boolean done = args[1].equals("1");
        String description = args[2];
        String rawFrom = args[3];
        String rawTo = args[4];
        String rawTags = args.length >= 6 ? args[5].trim() : "";

        LocalDateTime from = parseDateTimeFlexible(rawFrom);
        LocalDateTime to = parseDateTimeFlexible(rawTo);
        Event event;
        ArrayList<String> tags = parseTags(rawTags);

        if (from == null || to == null) {
            event = new Event(description, rawFrom, rawTo, tags);
        } else {
            event = new Event(description, from, to, tags);
        }
        if (done) {
            event.mark();
        }
        return event;
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

        switch (type) {
        case TODO:
            return handleTodoString(args);

        case DEADLINE:
            return handleDeadlineString(args);

        case EVENT:
            return handleEventString(args);

        default:
            throw new InvalidNomzArgumentException(MESSAGE_INVALID_FORMAT);
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
        case UNMARK:
            return handleMarkingCommand(cmd, args);

        case DELETE:
            return handleDeleteCommand(args);

        case FIND:
            return handleFindCommand(args);

        case TODO:
            return handleAddTodoCommand(args);

        case DEADLINE:
            return handleAddDeadlineCommand(args);

        case EVENT:
            return handleAddEventCommand(args);

        case TAG:
            return handleTagCommand(args);

        default:
            throw new InvalidNomzCommandException();
        }
    }

    private static Command handleTagCommand(String[] args) throws InvalidNomzArgumentException {
        if (args.length < 3) {
            throw new InvalidNomzArgumentException(MESSAGE_NO_ARGUMENTS);
        }

        int idx = intFromString(args[1]);
        String tag = joinArgs(2, args.length, args);
        return new TagCommand(idx, tag);
    }

    private static Command handleAddEventCommand(String[] args) throws InvalidNomzArgumentException {
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
            throw new InvalidNomzArgumentException(MESSAGE_WRONG_FROM_KEYWORD);
        }
        boolean isValidToIndex = toIndex > fromIndex && toIndex > 3 && toIndex <= args.length - 1;
        if (!isValidToIndex) {
            throw new InvalidNomzArgumentException(MESSAGE_WRONG_TO_KEYWORD);
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

    private static Command handleAddDeadlineCommand(String[] args) throws InvalidNomzArgumentException {
        if (args.length < 4) {
            throw new InvalidNomzArgumentException(MESSAGE_NO_ARGUMENTS);
        }
        int byPos = -1;
        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("/by")) {
                byPos = i;
                break;
            }
        }
        if (byPos == -1) {
            throw new InvalidNomzArgumentException(MESSAGE_NO_BY_KEYWORD);
        }
        String description = joinArgs(1, byPos, args);
        String byRaw = joinArgs(byPos + 1, args.length, args);
        LocalDateTime by = parseDateTimeFlexible(byRaw);
        if (by != null) {
            return new AddDeadlineCommand(description, by);
        }
        return new AddDeadlineCommand(description, byRaw);
    }

    private static MarkCommand handleMarkingCommand(CommandType cmd, String... args) throws NomzException {
        if (args.length < 2) {
            throw new InvalidNomzArgumentException(MESSAGE_NO_INDEX_ARGUMENT);
        }

        int idx = intFromString(args[1]);
        boolean toMark = (cmd == CommandType.MARK);
        return new MarkCommand(idx, toMark);
    }

    private static DeleteCommand handleDeleteCommand(String... args) throws NomzException {
        if (args.length < 2) {
            throw new InvalidNomzArgumentException(MESSAGE_NO_INDEX_ARGUMENT);
        }
        int idx = intFromString(args[1]);
        return new DeleteCommand(idx);
    }

    private static Command handleAddTodoCommand(String[] args) throws InvalidNomzArgumentException {
        if (args.length < 2) {
            throw new InvalidNomzArgumentException(MESSAGE_NO_DESCRIPTION_ARGUMENT);
        }
        String description = joinArgs(1, args.length, args);
        return new AddTodoCommand(description);
    }

    private static Command handleFindCommand(String[] args) throws InvalidNomzArgumentException {
        if (args.length < 2) {
            throw new InvalidNomzArgumentException(MESSAGE_NO_ARGUMENTS);
        }
        String keyword = joinArgs(1, args.length, args);
        return new FindCommand(keyword);
    }

}
