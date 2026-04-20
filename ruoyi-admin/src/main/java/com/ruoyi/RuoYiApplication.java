package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("""
                           (\u2665\u25e0\u203f\u25e0)\uff89\uff9e  \u82e5\u4f9d\u542f\u52a8\u6210\u529f   \u10da(\u00b4\u06a1`\u10da)\uff9e  
                            .-------.       ____     __        
                            |  _ _   \\      \\   \\   /  /    
                            | ( ' )  |       \\  _. /  '       
                            |(_ o _) /        _( )_ .'         
                            | (_,_).' __  ___(_ o _)'          
                            |  |\\ \\  |  ||   |(_,_)'         
                            |  | \\ `'   /|   `-'  /           
                            |  |  \\    /  \\      /           
                            ''-'   `'-'    `-..-'              """);
    }
}
