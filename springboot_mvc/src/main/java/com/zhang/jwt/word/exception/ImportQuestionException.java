package com.zhang.jwt.word.exception;

public class ImportQuestionException extends Exception {

    public ImportQuestionException(){

    }
    public ImportQuestionException(String str){
        //此处传入的是抛出异常后显示的信息提示
        super(str);
    }
}
