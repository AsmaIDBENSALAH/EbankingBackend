package org.sid.ebankingbackend.exception;
//RuntimeException ==exception non surveiller == pa besoin d'utiliser throws ou try/catch
//Exception == exception surveiller
public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(String message){
        super(message);
    }
}
