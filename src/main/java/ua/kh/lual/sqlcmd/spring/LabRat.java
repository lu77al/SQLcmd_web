package ua.kh.lual.sqlcmd.spring;

public class LabRat {

    private Service service;
    private String text;
    private String name;

    public LabRat(String text) {
        this.text = text;
    }

    public void init() {
        //
    }

    public String getText() {
        return text + " {" + service.getData() + "}";
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name + " {" + service.getData() + "}";
    }

    public void sayHi(){
        System.out.println(getText());
    }

    public void setService(Service service) {
        this.service = service;
    }
}