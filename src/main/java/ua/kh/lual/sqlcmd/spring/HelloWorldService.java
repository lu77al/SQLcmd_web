package ua.kh.lual.sqlcmd.spring;

public class HelloWorldService implements Service {
    @Override
    public String getData() {
        return "Hello world!";
    }
}
