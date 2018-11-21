package ua.kh.lual.sqlcmd.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(new String[] {"application-context.xml"});
        LabRat rat = (LabRat) context.getBean("rat");
        LabRat cat = (LabRat) context.getBean("cat");

        rat.sayHi();
        System.out.println(rat.getName());

        System.out.println("------------------");

        cat.sayHi();
        System.out.println(cat.getName());
    }
}
