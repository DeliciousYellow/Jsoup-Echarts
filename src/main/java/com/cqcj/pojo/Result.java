package com.cqcj.pojo;


import lombok.Getter;

/**
 * @program: ES-furniture
 * @description:
 * @author: 王炸！！
 * @create: 2023-04-13 22:51
 **/
@Getter
public class Result<T> {

    private Integer code;//状态码
    private String message;//状态信息
    private T data;

    private Result() {
    }

    private Result(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
        this.data = null;
    }

    private Result(T data, ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
        this.data = data;
    }

    //    public Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
//        Result<T> result = new Result<>();
//        if (body != null) {
//            result.setData(body);
//        }
//        result.setCode(resultCodeEnum.getCode());
//        result.setMessage(resultCodeEnum.getMessage());
//        return result;
//    }
    //提供一个修改信息的方法
    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public static <T> Result<T> ok() {
        return new Result<>(ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(data, ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> fail(T data) {
        return new Result<>(data, ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> error() {
        return new Result<>(ResultCodeEnum.ERROR);
    }

    public static <T> Result<T> error(T data) {
        return new Result<>(data, ResultCodeEnum.ERROR);
    }

}
