package adventurepack.mods.smab.smab;

public class NbtFormater {
    boolean open_string;
    boolean escaped;
    int indention_level = 0;
    StringBuilder output;
    char prev = '{';
    private boolean disable_newline;
    static final char WHITESPACE = '\u0009';
    boolean withhold_newline = false;
    boolean held_newline = false;

    public String accept(String string) {
        output = new StringBuilder(string.length());
        for (char c : string.toCharArray()) {
            this.react(c);
        }
        return output.toString();
    }

    private void append(char c) {
        withhold_newline = false;
        if (held_newline) {
            newLine();
        }
        output.append(c);
    }

    private void append(String s) {
        withhold_newline = false;
        if (held_newline) {
            newLine();
        }
        output.append(s);
    }

    private void newLine() {
        if (disable_newline) return;
        else if (withhold_newline) {
            held_newline = true;
            withhold_newline = false;
            return;
        }
        output.append("\n");
        for (int i = 0; i < indention_level; i++) {
            output.append(WHITESPACE);
        }
    }


    private void react(char c) {
        switch(c) {
            case '{':
            case '[':
                if (open_string) {
                    append(c);
                } else {
                    indention_level++;
                    append(c);
                    if (c == '[') {
                        withhold_newline = true;
                        newLine();
                    }
                }
                break;
            case '}':
            case ']':
                if (open_string) {
                    append(c);
                } else {
                    indention_level--;
                    newLine();
                    append(c);
                }
                if (disable_newline) {
                    disable_newline = false;
                }
                break;
            case '"':
                if (open_string) {
                    if (escaped) {
                        append('\\');
                        append('"');
                        escaped = false;
                    } else {
                        open_string = false;
                        append(c);
                    }
                } else {
                    open_string = true;
                    append(c);
                }
                break;
            case '\\':
                if (escaped) {
                    escaped = false;
                    append(c);
                } else {
                    escaped = true;
                }
                break;
            case ',':
                if (open_string) {
                    append(c);
                } else {
                    append(c);
                    newLine();
                }
                break;
            case 'I': // int array? // Issue. It needs to remove newLine from behind it....
                if (open_string) {
                    append(c);
                } else if(prev == '[') {
                    held_newline = false;
                    disable_newline = true;
                }
                append(c);
                break;
            default:
            append(c);
            break;
        }
        prev = c;
    }
}    