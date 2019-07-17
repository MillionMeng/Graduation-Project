package com.arvin.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * create by Arvin Meng
 * Date: 2019/5/10.
 */
public class ProductCollectVo {

    private String productImage;
    private String productName;
    private BigDecimal productPrice;
    private Date collectTime;//收藏商品的时间

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}
