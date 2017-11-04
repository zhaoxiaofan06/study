package com.shop.www.shopapplication.common;

/**
 * Created by think on 2017/10/14.
 */

public class GoodsEvent extends AbstractEvent {
    public enum Type
    {
        GOTO_GOODS_HOME,
        GOTO_GOODS_LIST,
        GOTO_GOODS_DETAIL
    }

    private int _resultCode;
    private Object _object;

    public GoodsEvent(
            Type type,
            int resultCode,
            Object object
    ) {

        super(type);

        this._resultCode = resultCode;
        this._object = object;
    }

    public int getResultCode()
    {
        return _resultCode;
    }

    public Object getObject()
    {
        return _object;
    }
}
