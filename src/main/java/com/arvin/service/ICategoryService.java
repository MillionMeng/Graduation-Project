package com.arvin.service;

import com.arvin.common.Response;

import java.util.List;

/**
 * create by Arvin Meng
 * Date: 2019/4/16.
 */
public interface ICategoryService {

    public Response addCategory(String categoryName, Integer parentId);

    public Response updateCategoryName(Integer categoryId, String categoryName);

    public Response<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
