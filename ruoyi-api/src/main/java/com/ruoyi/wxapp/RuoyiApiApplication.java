package com.ruoyi.wxapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ruoyi.framework")
@ComponentScan("com.ruoyi.common")
@ComponentScan("com.ruoyi.system")
@ComponentScan("com.ruoyi.chat")
@ComponentScan("com.ruoyi.cms")
public class RuoyiApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuoyiApiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  DTCMSAPIService启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }

}
