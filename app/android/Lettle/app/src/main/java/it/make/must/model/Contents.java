package it.make.must.model;

import java.io.Serializable;

/**
 * Created by EVOL on 2016-07-04.
 */
public class Contents implements Serializable {

    private String title;
    private String msg;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
