import com.paulilves.deployer.core.Cli;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

public class CmdTest {

    @Test(expected = NumberFormatException.class)
    public void shouldNotAllowInvalidPortNumber() {
        String[] args = {"Main", "-a", "nothing", "--port", "asd"};
        new Cli(args).parse();
    }

//    @Test(expected = UnrecognizedOptionException.class)
//    public void shouldNotAllowUnrecognizedOptions() {
//        String[] args = {"Main", "-a", "nothing", "--zz", "8080"};
//        new Cli(args).parse();
//    }

}
