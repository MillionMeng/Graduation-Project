package com.arvin.pojo;

/**
 * create by Arvin Meng
 * Date: 2019/5/9.
 */

//商品颜色关联
public class ProductColor {
    private int id;
    private int product_id;
    private int color_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }
}
