package io;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UniqueCodeGenerator
{
    public static String createRandomCode()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
        
    public static void main(String[] args)
    {
        UniqueCodeGenerator u = new UniqueCodeGenerator();
        System.out.println(u.createRandomCode());
    }
}
