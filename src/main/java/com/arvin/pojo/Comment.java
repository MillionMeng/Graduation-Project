package com.arvin.pojo;

/**
 * create by Arvin Meng
 * Date: 2019/5/9.
 */

//评论表实体
public class Comment {

    private int id;
    private Integer productId;
    private Integer userId;
    private String content;//评论内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
