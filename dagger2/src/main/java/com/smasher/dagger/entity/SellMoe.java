package com.smasher.dagger.entity;

import javax.inject.Inject;

/**
 * @author matao
 * @date 2019/5/15
 */
public class SellMoe {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @Inject
    public SellMoe() {
    }


}
