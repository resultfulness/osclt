package lmp2.oscillate;

import java.util.ArrayList;

public class Config {
    private static final String ARGUMENTS_SCHEME = "b";
    private static final int OTHER_ARGS_EXPECTED_COUNT = 1;
    private final ArrayList<String> otherArgs = new ArrayList<>();
    private boolean isInputFileBinary;
    private final String inputFilePath;

    Config(String[] args) throws IllegalArgumentException {
        for (String arg : args) {
            boolean isArgAnOption = arg.startsWith("-");
            if (isArgAnOption) {
                boolean isOptValid = Config.ARGUMENTS_SCHEME.indexOf(
                    arg.charAt(1)
                ) >= 0;
                boolean isOptValidLength = arg.length() == 2;
                if (isOptValid && isOptValidLength) {
                    switch (arg.charAt(1)) {
                        case 'b':
                            this.isInputFileBinary = true;
                            break;
                        default:
                            throw new RuntimeException("unreachable");
                    }
                } else {
                    throw new IllegalArgumentException(
                        String.format("unknown argument: %s", arg)
                    );
                }
            } else {
                this.otherArgs.add(arg);
            }
        }

        if (this.otherArgs.isEmpty()) {
            throw new IllegalArgumentException(
                "missing required commandline argument"
            );
        } else if (this.otherArgs.size() > Config.OTHER_ARGS_EXPECTED_COUNT) {
            throw new IllegalArgumentException(
                String.format(
                    "too many arguments: expected %s, received %s",
                    Config.OTHER_ARGS_EXPECTED_COUNT,
                    this.otherArgs.size()
                )
            );
        } else {
            this.inputFilePath = this.otherArgs.get(0);
        }
    }

    public boolean getIsInputFileBinary() {
        return this.isInputFileBinary;
    }

    public String getInputFilePath() {
        return this.inputFilePath;
    }

    @Override
    public String toString() {
        return String.format(
            """
            Config {
                isInputFileBinary: %s,
                inputFilePath: %s,
            }\
            """,
            this.isInputFileBinary,
            this.inputFilePath
        );
    }
}
