import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

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

    public static class ParsedCommand {
        public final Command command;
        public final String description; 
        public final Integer index;      
        public final String by;          
        public final String from, to; 
        public final LocalDateTime byTime;
        public final LocalDateTime fromTime, toTime;   

        public ParsedCommand(Command c, String d, Integer i, String by, String from, String to, 
            LocalDateTime byTime, LocalDateTime fromTime, LocalDateTime toTime) {
            this.command = c; 
            this.description = d; 
            this.index = i;
            this.by = by; 
            this.from = from; 
            this.to = to;
            this.byTime = byTime;
            this.fromTime = fromTime;
            this.toTime = toTime;
        }

        public static ParsedCommand of(Command c) {
            return new ParsedCommand(c, null, null, null, null, null, null, null, null);
        }
    }

    public static LocalDateTime parseDateTimeFlexible(String s) {
        for (DateTimeFormatter f : DATE_TIME_FORMATS) {
            try {
                return LocalDateTime.parse(s, f);
            } catch (DateTimeParseException ignored) {}
        }

        for (DateTimeFormatter f : DATE_ONLY_FORMATS) {
            try {
                return LocalDate.parse(s, f).atStartOfDay();
            } catch (DateTimeParseException ignored) {}
        }

        return null;
    }

    /** Convert a string to an integer, throwing an exception if it is not a valid integer. */
    public static int intFromString(String index) throws InvalidNomzArgumentException {
        try { 
            return Integer.parseInt(index); 
        } catch (NumberFormatException e) { 
            throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_INTEGER_ARGUMENT); 
        }
    }

    public static Task parseTaskFileContent(String f) throws NomzException{
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

    public static ParsedCommand parse(String input) throws NomzException {
        String[] args = input.trim().split("\\s+");
        Command cmd = Command.fromString(args[0]); 
        switch (cmd) {
        case LIST: 
        case BYE:
            return ParsedCommand.of(cmd);

        case MARK:
        case UNMARK:
        case DELETE: {
            if (args.length < 2) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_INDEX_ARGUMENT);
            }
            int idx = intFromString(args[1]);
            return new ParsedCommand(cmd, null, idx, null, null, null, null, null, null);
        }

        case TODO: {
            if (args.length < 2) {
                throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_DESCRIPTION_ARGUMENT);
            }
            String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            return new ParsedCommand(cmd, description, null, null, null, null, null, null, null);
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
                return new ParsedCommand(cmd, description, null, null, null, null, by, null, null);
            }
            return new ParsedCommand(cmd, description, null, byRaw, null, null, null, null, null);
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
                return new ParsedCommand(cmd, description, null, null, null, null, null, from, to);
            }
            return new ParsedCommand(cmd, description, null, null, fromRaw, toRaw, null, null, null);
        }

        default:
            throw new InvalidNomzCommandException();
        }
    }

}
