package nomz.parser;

import java.util.Arrays;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import nomz.commands.ListCommand;
import nomz.commands.MarkCommand;
import nomz.commands.CommandType;
import nomz.commands.DeleteCommand;
import nomz.commands.AddDeadlineCommand;
import nomz.commands.AddEventCommand;
import nomz.commands.AddTodoCommand;
import nomz.commands.ByeCommand;
import nomz.commands.Command;

import nomz.common.Messages;

import nomz.data.exception.InvalidNomzArgumentException;
import nomz.data.exception.InvalidNomzCommandException;
import nomz.data.exception.NomzException;

import nomz.data.tasks.TaskType;
import nomz.data.tasks.Deadline;
import nomz.data.tasks.Event;
import nomz.data.tasks.Todo;
import nomz.data.tasks.Task;


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

    /** Convert a string to an integer, throwing an exception if it is not a valid integer. */
    private static int intFromString(String index) throws InvalidNomzArgumentException {
        try { 
            return Integer.parseInt(index); 
        } catch (NumberFormatException e) { 
            throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_INTEGER_ARGUMENT); 
        }
    }

    public static Task parseTaskFileContent(String f) throws NomzException{
        // input should take the form 
        // <TASK_TYPE_SYMBOL>|<DONE>|<DESCRIPTION>[|<DEADLINE/EVENT_START>|<EVENT_END>]
        String[] args = f.split("[\\|]");
        TaskType type = TaskType.fromSymbol(args[0]);
        boolean done = args[1].equals("1");
        switch(type){
        case TODO:
            Todo todo = new Todo(args[2]);
            if (done) {
                todo.mark();
            }
            return todo;

        case DEADLINE:
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
     * Parses a user command from the input string.
     * @param input The input string to parse.
     * @return The corresponding Command object.
     * @throws NomzException If the input is invalid.
     */
    public static Command parse(String input) throws NomzException {
        String[] args = input.trim().split("\\s+");
        CommandType cmd = CommandType.fromString(args[0]); 
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

        case TODO: {
            if (args.length < 2) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_DESCRIPTION_ARGUMENT);
            }
            String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
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
            String description = String.join(" ", Arrays.copyOfRange(args, 1, byPos));
            String byRaw = String.join(" ", Arrays.copyOfRange(args, byPos + 1, args.length));
            LocalDateTime by = parseDateTimeFlexible(byRaw);
            if (by != null) {
                return new AddDeadlineCommand(description, by);
            }
            return new AddDeadlineCommand(description, byRaw);
        }

        case EVENT: {
            int fromIndex = -1, toIndex = -1;
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
            String description = String.join(" ", Arrays.copyOfRange(args, 1, fromIndex));
            String fromRaw = String.join(" ", Arrays.copyOfRange(args, fromIndex + 1, toIndex));
            String toRaw = String.join(" ", Arrays.copyOfRange(args, toIndex + 1, args.length));
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
