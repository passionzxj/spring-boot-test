package upload.util;


import lombok.Data;

/**
 * 3种创建方式 <br/>
 *      1.通过构造方法创建 <br/>
 *         AjaxResult<Dictionary> result = new AjaxResult<Dictionary>();<br/>
 *         result2.setData(dictionary);<br/>
 *         result2.setSuccess(true);<br/>
 *         result2.setMessage("操作成功");<br/>
 *         result2.setCode(200);<br/>
 *      2.通过success()/fail()方法创建<br/>
 *         AjaxResult<Dictionary> result = AjaxResult.success(dictionary);<br/>
 *         AjaxResult<Dictionary> result = AjaxResult.success("操作成功",dictionary);<br/>
 *      3.通过builder链式操作方式创建<br/>
 *         AjaxResult<Dictionary> result3 = new AjaxResult.Builder<Dictionary>().code(200).data(dictionary).message("操作成功").success(true).build();
 * @param <T>   T为泛型，表示data的数据类型
 */
@Data
public class AjaxResult<T>{

    public static final int CODE_SUCCESS = 200;
    public static final String MESSAGE_SUCCESS = "操作成功";
    public static final String MESSAGE_FAIL = "操作成功";

    private boolean success;
    private String message;
    private T data;
    private Integer code;

    public AjaxResult(){}


    public static<T> AjaxResult success(T data){
        return result(CODE_SUCCESS,true, MESSAGE_SUCCESS, data);
    }

    public static<T> AjaxResult success(String message,T data){
        return result(CODE_SUCCESS,true, message, data);
    }

    public static<T> AjaxResult fail(){
        return result(null,false, null,null);
    }

    public static<T> AjaxResult fail(String message){
        return result(null,false, message,null);
    }

    public static<T> AjaxResult fail(String message,T data){
        return result(null,false, message,data);
    }

    public static<T> AjaxResult fail(Integer code, String message){
        return result(code,false, message, null);
    }

    public static<T> AjaxResult fail(Integer code,String message,T data){
        return result(null,false, message,data);
    }

    private static<T> AjaxResult result(Integer code, boolean flag, String message, T object) {
        AjaxResult result = new AjaxResult();
        result.setCode(code);
        result.setSuccess(flag);
        result.setMessage(message);
        result.setData(object);
        return result;
    }

    private AjaxResult(Builder<T> builder){
        this.success = builder.success;
        this.message = builder.message;
        this.data = builder.data;
        this.code = builder.code;
    }

    public static class Builder<T>{
        private boolean success;
        private String message;
        private T data;
        private Integer code = CODE_SUCCESS;

        public AjaxResult build(){
            return new AjaxResult(this);
        }

        public Builder success(boolean success){
            this.success = success;
            return this;
        }
        public Builder message(String message){
            this.message = message;
            return this;
        }
        public Builder data(T data){
            this.data = data;
            return this;
        }
        public Builder code(Integer code){
            this.code = code;
            return this;
        }
    }


    public static void main(String[] args) {
        String data =  "data";

        AjaxResult<String> result = AjaxResult.success(data);

        AjaxResult<String> result2 = new AjaxResult<String>();
        result2.setData(data);
        result2.setSuccess(true);
        result2.setMessage("操作成功");
        result2.setCode(200);

        AjaxResult<String> result3 = new AjaxResult.Builder<String>().code(200).data(data).message("操作成功").success(true).build();
    }
}