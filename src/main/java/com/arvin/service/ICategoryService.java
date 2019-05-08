package com.arvin.service;

import com.arvin.common.Response;
import com.arvin.pojo.Category;

import java.util.List;

/**
 * create by Arvin Meng
 * Date: 2019/4/16.
 */
public interface ICategoryService {

    public Response addCategory(String categoryName, Integer parentId);

    public Response updateCategoryName(Integer categoryId, String categoryName);

    public Response<List<Category>> getChildrenParallelCategory(Integer categoryId);

    public Response<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
