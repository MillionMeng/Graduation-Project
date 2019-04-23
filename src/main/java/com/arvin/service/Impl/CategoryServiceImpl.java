package com.arvin.service.Impl;

import com.arvin.common.Response;
import com.arvin.dao.CategoryMapper;
import com.arvin.pojo.Category;
import com.arvin.service.ICategoryService;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * create by Arvin Meng
 * Date: 2019/4/16.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    public Response addCategory(String categoryName,Integer parentId) {
        if (parentId == null || StringUtils.isBlank((categoryName))) {
            return Response.createByErrorMessage("添加商品参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是有用的
        int rowCount  =categoryMapper.insert(category);
        if(rowCount > 0){
            return Response.createBySuccess("添加品类成功");
        }
        return Response.createByErrorMessage("添加品类失败");
    }

    public Response updateCategoryName(Integer categoryId, String categoryName){
        if(categoryId == null || StringUtils.isBlank((categoryName))){
            return Response.createByErrorMessage("更新商品类别参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return Response.createBySuccess("更新品类名字成功");
        }
        return Response.createByErrorMessage("更新品类名字失败");
    }


    //递归查询本节点的id及孩子节点的id
    public Response<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList = new ArrayList<>();
        if(categoryId != null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return Response.createBySuccess(categoryIdList);
    }



    /**
     * 递归算法，算出子节点  使用set 要重写hashCode()  和equals()方法
     *
     * 两个对象相同 equals返回true   hasCode也相同
     * 如果两个对象 hashCode相同，他们不一定相同  hashCode=true  equals可能返回false
     * 例如hash只取了 id  而equals比较了其他属性
     *
     * 使用set 将equals 和hashCode都重写 保证里面判断因子是一样的
     *
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        //查找子节点,递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categoryItem : categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
